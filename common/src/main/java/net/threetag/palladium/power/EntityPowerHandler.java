package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.data.PalladiumEntityData;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.SyncEntityPowersPacket;
import net.threetag.palladium.power.provider.PowerProvider;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityPowerHandler extends PalladiumEntityData<LivingEntity> {

    private final Map<ResourceLocation, PowerHolder> powers = new HashMap<>();
    private CompoundTag powerData = new CompoundTag();

    public EntityPowerHandler(LivingEntity entity) {
        super(entity);
    }

    public Map<ResourceLocation, PowerHolder> getPowerHolders() {
        return ImmutableMap.copyOf(this.powers);
    }

    @Override
    public void copyFrom(PalladiumEntityData<LivingEntity> source) {
        if (source instanceof EntityPowerHandler old) {
            this.powerData = old.powerData.copy();
        }
    }

    @Override
    public void tick() {
        if (!this.getEntity().level().isClientSide) {
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
                PalladiumNetwork.sendToTrackingAndSelf(this.getEntity(), msg);
            }
        }

        // Tick
        for (PowerHolder holder : this.powers.values()) {
            holder.tick();
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

    @Override
    public void load(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        this.powerData = nbt;
    }

    @Override
    public CompoundTag save(HolderLookup.Provider registryLookup) {
        for (PowerHolder holder : this.powers.values()) {
            this.powerData.put(holder.getPowerId().toString(), holder.save());
        }
        this.cleanPowerData();
        return this.powerData;
    }

    public void cleanPowerData() {
        List<String> toRemove = new ArrayList<>();
        for (String key : this.powerData.getAllKeys()) {
            if (!this.getEntity().registryAccess().lookupOrThrow(PalladiumRegistryKeys.POWER).containsKey(ResourceLocation.parse(key))) {
                toRemove.add(key);
            }
        }
        for (String key : toRemove) {
            this.powerData.remove(key);
        }
    }

}
