package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.api.PlayerAnimationAccess;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.threetag.palladium.client.animation.PalladiumAnimationManager;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.network.SyncEntityPowersPacket;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.*;

public class EntityPowerHandler extends PalladiumEntityData<LivingEntity, EntityPowerHandler> {

    public static final MapCodec<EntityPowerHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            CompoundTag.CODEC.optionalFieldOf("power_data", new CompoundTag()).forGetter(EntityPowerHandler::savePowerDataTag)
    ).apply(instance, EntityPowerHandler::new));

    private final Map<ResourceLocation, PowerHolder> powers = new LinkedHashMap<>();
    private CompoundTag powerData;

    public EntityPowerHandler(CompoundTag powerData) {
        this.powerData = powerData;
    }

    public Map<ResourceLocation, PowerHolder> getPowerHolders() {
        return ImmutableMap.copyOf(this.powers);
    }

    @Override
    public void copyFrom(PalladiumEntityData<LivingEntity, EntityPowerHandler> source) {
        if (source instanceof EntityPowerHandler old) {
            this.powerData = old.powerData.copy();
        }
    }

    @Override
    public MapCodec<EntityPowerHandler> codec() {
        return CODEC;
    }

    @Override
    public void tick() {
        if (!this.getEntity().level().isClientSide()) {
            List<PowerHolder> invalidPowers = new ArrayList<>();
            PowerCollector collector = new PowerCollector(this.getEntity(), this, invalidPowers);

            // Find invalid
            for (PowerHolder holder : this.powers.values()) {
                if (holder.isInvalid()) {
                    invalidPowers.add(holder);
                }
            }

            // Get new ones
            for (PowerProvider provider : PalladiumRegistries.POWER_PROVIDER) {
                provider.providePowers(this.getEntity(), this, collector);
            }

            // Remove old ones
            for (PowerHolder holder : invalidPowers) {
                this.removePowerHolder(holder.getPower());
            }

            // Add new ones
            List<PowerHolder> added = new ArrayList<>();
            for (PowerCollector.PowerHolderCache holderCache : collector.getAdded()) {
                var holder = holderCache.make(this.getEntity(), this.powerData);
                this.addPowerHolder(holder);
                added.add(holder);
            }

            // Sync
            if (!invalidPowers.isEmpty() || !collector.getAdded().isEmpty()) {
                var msg = SyncEntityPowersPacket.create(this.getEntity(), invalidPowers, added);
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(this.getEntity(), msg);
            }
        }

        // Tick
        for (PowerHolder holder : this.powers.values()) {
            holder.tick();
        }

        if (this.getEntity() instanceof Avatar) {
            ArrayList<AbilityInstance<?>> animatingAbilities = new ArrayList<>();
            this.powers.values().forEach(powerHolder -> animatingAbilities.addAll(powerHolder.getAbilities().values().stream().filter(a -> a.isEnabled() && a.getAbility().getProperties().getAnimation().isPresent()).toList()));

            //Cut off animations if the ability that triggered it is disabled
            if(animatingAbilities.stream().filter(abilityInstance -> abilityInstance.getAbility().getProperties().getAnimationLayer() == 0).findFirst().isEmpty())
                ((PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((Avatar) this.getEntity(), PalladiumAnimationManager.COSMETIC_ANIMATION)).stopTriggeredAnimation();

            if(animatingAbilities.stream().filter(abilityInstance -> abilityInstance.getAbility().getProperties().getAnimationLayer() == 1).findFirst().isEmpty())
                ((PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((Avatar) this.getEntity(), PalladiumAnimationManager.IDLE_ANIMATION)).stopTriggeredAnimation();

            if(animatingAbilities.stream().filter(abilityInstance -> abilityInstance.getAbility().getProperties().getAnimationLayer() == 2).findFirst().isEmpty())
                ((PlayerAnimationController) PlayerAnimationAccess.getPlayerAnimationLayer((Avatar) this.getEntity(), PalladiumAnimationManager.ACTIVE_ANIMATION)).stopTriggeredAnimation();
        }
    }

    public void addPowerHolder(PowerHolder holder) {
        if (!this.hasPower(holder.getPowerId())) {
            this.powers.put(holder.getPowerId(), holder);
            holder.firstTick();
        }
    }

    public void removePowerHolder(Holder<Power> power) {
        var powerId = power.unwrapKey().orElseThrow().location();
        if (this.powers.containsKey(powerId)) {
            var holder = this.powers.get(powerId);
            boolean hasPersistentData = holder.getPower().value().hasPersistentData();
            holder.lastTick();

            if (hasPersistentData) {
                this.powerData.put(holder.getPowerId().toString(), holder.save());
            }

            this.powers.remove(powerId);

            if (!hasPersistentData) {
                this.powerData.remove(powerId.toString());
            }
        }
    }

    public PowerHolder getPowerHolder(ResourceLocation powerId) {
        return this.powers.get(powerId);
    }

    public boolean hasPower(ResourceLocation powerId) {
        return this.powers.containsKey(powerId);
    }

    public CompoundTag savePowerDataTag() {
        for (PowerHolder holder : this.powers.values()) {
            this.powerData.put(holder.getPowerId().toString(), holder.save());
        }
        this.cleanPowerData();
        return this.powerData;
    }

    public void cleanPowerData() {
        List<String> toRemove = new ArrayList<>();
        for (String key : this.powerData.keySet()) {
            if (!this.getEntity().registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).containsKey(ResourceLocation.parse(key))) {
                toRemove.add(key);
            }
        }
        for (String key : toRemove) {
            this.powerData.remove(key);
        }
    }

}
