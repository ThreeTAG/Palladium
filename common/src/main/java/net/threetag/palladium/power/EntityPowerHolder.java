package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.network.SyncPowerHolder;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.HashMap;
import java.util.Map;

public class EntityPowerHolder implements IPowerHolder {

    private final LivingEntity entity;
    private ResourceLocation powerId;
    private Power powerCached;
    private final Map<String, AbilityEntry> entryMap = new HashMap<>();

    public EntityPowerHolder(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public Power getPower() {
        if (this.powerCached == null && this.powerId != null) {
            this.powerCached = PowerManager.getInstance(this.entity.getLevel()).getPower(this.powerId);
        }
        return this.powerCached;
    }

    @Override
    public void setPower(Power power) {
        this.powerId = power != null ? power.getId() : null;
        this.powerCached = null;
        this.entryMap.clear();
        if (this.getPower() != null) {
            for (AbilityConfiguration ability : this.getPower().getAbilities()) {
                this.entryMap.put(ability.getId(), new AbilityEntry(ability));
            }
        }

        if (!this.entity.level.isClientSide) {
            new SyncPowerHolder(entity.getId(), this.toNBT()).sendToLevel((ServerLevel) entity.level);
        }
    }

    @Override
    public Map<String, AbilityEntry> getAbilities() {
        return ImmutableMap.copyOf(this.entryMap);
    }

    @Override
    public void tick() {
        if (this.getPower() != null && this.getPower().isInvalid() && !this.entity.level.isClientSide) {
            Power newPower = PowerManager.getInstance(this.entity.getLevel()).getPower(this.getPower().getId());

            if (newPower != null) {
                CompoundTag tag = this.toNBT();
                this.setPower(newPower);
                this.fromNBT(tag);
            } else {
                this.setPower(null);
            }

            new SyncPowerHolder(this.entity.getId(), this.toNBT()).sendToLevel((ServerLevel) this.entity.level);
        }

        this.entryMap.forEach((id, entry) -> entry.tick(entity, this.getPower(), this));
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();

        if (this.getPower() != null) {
            nbt.putString("Power", this.powerId.toString());

            if (!this.entryMap.isEmpty()) {
                CompoundTag abilitiesTag = new CompoundTag();
                this.entryMap.forEach((id, entry) -> abilitiesTag.put(id, entry.toNBT()));
                nbt.put("Abilities", abilitiesTag);
            }
        }

        return nbt;
    }

    @Override
    public void fromNBT(CompoundTag nbt) {
        this.setPower(PowerManager.getInstance(this.entity.getLevel()).getPower(new ResourceLocation(nbt.getString("Power"))));

        CompoundTag abilitiesTag = nbt.getCompound("Abilities");
        this.entryMap.forEach((id, entry) -> entry.fromNBT(abilitiesTag.getCompound(id)));
    }
}
