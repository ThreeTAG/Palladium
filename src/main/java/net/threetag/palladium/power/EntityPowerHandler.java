package net.threetag.palladium.power;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.logic.triggers.PalladiumCriteriaTriggers;
import net.threetag.palladium.network.SyncEntityPowersPacket;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.*;
import java.util.stream.Collectors;

public class EntityPowerHandler extends PalladiumEntityData<LivingEntity, EntityPowerHandler> {

    public static final Codec<EntityPowerHandler> CODEC = CompoundTag.CODEC.xmap(EntityPowerHandler::new, EntityPowerHandler::savePowerDataTag);

    private final Map<Identifier, PowerInstance> powers = new LinkedHashMap<>();
    private CompoundTag powerData;

    public EntityPowerHandler(CompoundTag powerData) {
        this.powerData = powerData;
    }

    public Collection<PowerInstance> getPowers() {
        return this.powers.values().stream().sorted((o1, o2) -> o2.getPriority() - o1.getPriority()).collect(Collectors.toList());
    }

    @Override
    public void copyFrom(PalladiumEntityData<LivingEntity, EntityPowerHandler> source) {
        if (source instanceof EntityPowerHandler old) {
            this.powerData = old.powerData.copy();
        }
    }

    @Override
    public Codec<EntityPowerHandler> codec() {
        return CODEC;
    }

    @Override
    public void tick() {
        if (!this.getEntity().level().isClientSide()) {
            List<PowerInstance> invalidPowers = new ArrayList<>();
            PowerCollector collector = new PowerCollector(this.getEntity(), this, invalidPowers);

            // Find invalid
            for (PowerInstance instance : this.powers.values()) {
                if (instance.isInvalid()) {
                    invalidPowers.add(instance);
                }
            }

            // Get new ones
            for (PowerProvider provider : PalladiumRegistries.POWER_PROVIDER) {
                provider.providePowers(this.getEntity(), this, collector);
            }

            // Remove old ones
            for (PowerInstance instance : invalidPowers) {
                this.removePowerInstance(instance.getPower());
            }

            // Add new ones
            List<PowerInstance> added = new ArrayList<>();
            for (PowerCollector.PowerInstanceCache instanceCache : collector.getAdded()) {
                var instance = instanceCache.make(this.getEntity(), this.powerData);
                this.addPowerInstance(instance);
                added.add(instance);
            }

            // Sync
            if (!invalidPowers.isEmpty() || !collector.getAdded().isEmpty()) {
                var msg = SyncEntityPowersPacket.create(this.getEntity(), invalidPowers, added);
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(this.getEntity(), msg);
            }
        }

        // Tick
        for (PowerInstance instance : this.powers.values()) {
            instance.tick();
        }
    }

    public void addPowerInstance(PowerInstance instance) {
        if (!this.hasPower(instance.getPowerId())) {
            this.powers.put(instance.getPowerId(), instance);
            instance.firstTick();

            if (this.getEntity() instanceof ServerPlayer serverPlayer) {
                PalladiumCriteriaTriggers.POWER_GAINED.get().trigger(serverPlayer, instance.getPowerId());
            }
        }
    }

    public void removePowerInstance(Holder<Power> power) {
        var powerId = power.unwrapKey().orElseThrow().identifier();
        if (this.powers.containsKey(powerId)) {
            var instance = this.powers.get(powerId);
            boolean hasPersistentData = instance.getPower().value().hasPersistentData();
            instance.lastTick();

            if (this.getEntity() instanceof ServerPlayer serverPlayer) {
                PalladiumCriteriaTriggers.POWER_LOST.get().trigger(serverPlayer, instance.getPowerId());
            }

            if (hasPersistentData) {
                this.powerData.put(instance.getPowerId().toString(), instance.save());
            }

            this.powers.remove(powerId);

            if (!hasPersistentData) {
                this.powerData.remove(powerId.toString());
            }
        }
    }

    public PowerInstance getPowerInstance(Identifier powerId) {
        return this.powers.get(powerId);
    }

    public boolean hasPower(Identifier powerId) {
        return this.powers.containsKey(powerId);
    }

    public boolean hasPower(Holder<Power> powerHolder) {
        for (PowerInstance instance : this.powers.values()) {
            if (instance.getPower().value() == powerHolder.value()) {
                return true;
            }
        }

        return false;
    }

    public CompoundTag savePowerDataTag() {
        for (PowerInstance instance : this.powers.values()) {
            this.powerData.put(instance.getPowerId().toString(), instance.save());
        }
        this.cleanPowerData();
        return this.powerData;
    }

    public void cleanPowerData() {
        List<String> toRemove = new ArrayList<>();
        for (String key : this.powerData.keySet()) {
            if (!this.getEntity().registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).containsKey(Identifier.parse(key))) {
                toRemove.add(key);
            }
        }
        for (String key : toRemove) {
            this.powerData.remove(key);
        }
    }

}
