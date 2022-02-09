package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.SyncPowerHolder;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.HashMap;
import java.util.Map;

public class EntityPowerHolder implements IPowerHolder {

    private final LivingEntity entity;
    private Power power;
    private final Map<String, AbilityEntry> entryMap = new HashMap<>();

    public EntityPowerHolder(LivingEntity entity) {
        this.entity = entity;
    }

    @Override
    public Power getPower() {
        return this.power;
    }

    @Override
    public void setPower(Power power) {
        this.power = power;
        this.entryMap.clear();
        if (this.power != null) {
            for (AbilityConfiguration ability : this.power.getAbilities()) {
                this.entryMap.put(ability.getId(), new AbilityEntry(ability));
            }
        }

        if(!this.entity.level.isClientSide) {
            new SyncPowerHolder(entity.getId(), this.toNBT()).sendToLevel((ServerLevel) entity.level);
        }
    }

    @Override
    public Map<String, AbilityEntry> getAbilities() {
        return ImmutableMap.copyOf(this.entryMap);
    }

    @Override
    public void tick(LivingEntity entity) {
        if (this.power != null && this.power.isInvalid() && !entity.level.isClientSide) {
            Power newPower = PowerManager.getInstance().getPower(this.power.getId());

            if (newPower != null) {
                CompoundTag tag = this.toNBT();
                this.setPower(power);
                this.fromNBT(tag);
            } else {
                this.setPower(null);
            }

            new SyncPowerHolder(entity.getId(), this.toNBT()).sendToLevel((ServerLevel) entity.level);
        }

        this.entryMap.forEach((id, entry) -> entry.tick(entity, this.getPower(), this));
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();

        if (this.power != null) {
            nbt.putString("Power", this.power.getId().toString());

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
        this.setPower(PowerManager.getInstance().getPower(new ResourceLocation(nbt.getString("Power"))));

        CompoundTag abilitiesTag = nbt.getCompound("Abilities");
        this.entryMap.forEach((id, entry) -> entry.fromNBT(abilitiesTag.getCompound(id)));
    }
}
