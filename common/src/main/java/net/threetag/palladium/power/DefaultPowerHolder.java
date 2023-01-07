package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.HashMap;
import java.util.Map;

public class DefaultPowerHolder implements IPowerHolder {

    public final LivingEntity entity;
    private final Power power;
    private final Map<String, AbilityEntry> entryMap = new HashMap<>();
    private IPowerValidator validator;

    public DefaultPowerHolder(LivingEntity entity, Power power, IPowerValidator validator) {
        this.entity = entity;
        this.power = power;
        for (AbilityConfiguration ability : this.getPower().getAbilities()) {
            AbilityEntry entry = new AbilityEntry(ability, this);
            entry.id = ability.getId();
            this.entryMap.put(ability.getId(), entry);
        }
        this.validator = validator;
    }

    @Override
    public Power getPower() {
        return this.power;
    }

    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Override
    public void fromNBT(CompoundTag tag) {
        for (Map.Entry<String, AbilityEntry> entry : this.entryMap.entrySet()) {
            if (tag.contains(entry.getKey())) {
                CompoundTag abData = tag.getCompound(entry.getKey());
                entry.getValue().fromNBT(abData);
            }
        }
    }

    @Override
    public CompoundTag toNBT() {
        CompoundTag tag = new CompoundTag();

        for (Map.Entry<String, AbilityEntry> entry : this.entryMap.entrySet()) {
            CompoundTag abData = entry.getValue().toNBT();
            tag.put(entry.getKey(), abData);
        }

        return tag;
    }

    @Override
    public Map<String, AbilityEntry> getAbilities() {
        return ImmutableMap.copyOf(this.entryMap);
    }

    @Override
    public void tick() {
        this.entryMap.forEach((id, entry) -> entry.tick(entity, this.getPower(), this));
    }

    @Override
    public void firstTick() {
        this.entryMap.forEach((id, entry) -> entry.getConfiguration().getAbility().firstTick(entity, entry, this, entry.isEnabled()));
    }

    @Override
    public void lastTick() {
        this.entryMap.forEach((id, entry) -> entry.getConfiguration().getAbility().lastTick(entity, entry, this, entry.isEnabled()));
    }

    @Override
    public boolean isInvalid() {
        return this.power.isInvalid() || !this.validator.stillValid(this.entity, this.power);
    }

    @Override
    public void switchValidator(IPowerValidator validator) {
        this.validator = validator;
    }
}
