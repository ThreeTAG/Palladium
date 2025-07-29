package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.energybar.EnergyBarConfiguration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultPowerHolder implements IPowerHolder {

    public final LivingEntity entity;
    private final Power power;
    private final ImmutableMap<String, AbilityInstance> abilityInstances;
    private final ImmutableMap<String, EnergyBar> energyBars;
    private IPowerValidator validator;

    public DefaultPowerHolder(LivingEntity entity, Power power, IPowerValidator validator) {
        this.entity = entity;
        this.power = power;
        this.validator = validator;

        final Map<String, AbilityInstance> abilities = new HashMap<>();
        for (AbilityConfiguration ability : this.getPower().getAbilities()) {
            AbilityInstance entry = new AbilityInstance(ability, this);
            entry.id = ability.getId();
            abilities.put(ability.getId(), entry);
        }
        this.abilityInstances = ImmutableMap.copyOf(abilities);

        final Map<String, EnergyBar> energyBars = new LinkedHashMap<>();
        for (EnergyBarConfiguration energyBar : this.getPower().getEnergyBars()) {
            energyBars.put(energyBar.getName(), new EnergyBar(this, energyBar));
        }
        this.energyBars = ImmutableMap.copyOf(energyBars);
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
        for (Map.Entry<String, AbilityInstance> entry : this.abilityInstances.entrySet()) {
            if (tag.contains(entry.getKey())) {
                CompoundTag abData = tag.getCompound(entry.getKey());
                entry.getValue().fromNBT(abData);
            }
        }

        if (tag.contains("_EnergyBars", 10)) {
            var energies = tag.getCompound("_EnergyBars");
            for (String key : energies.getAllKeys()) {
                if (this.energyBars.containsKey(key)) {
                    this.energyBars.get(key).fromNBT(energies.getCompound(key));
                }
            }
        }
    }

    @Override
    public CompoundTag toNBT(boolean toDisk) {
        CompoundTag tag = new CompoundTag();

        for (Map.Entry<String, AbilityInstance> entry : this.abilityInstances.entrySet()) {
            CompoundTag abData = entry.getValue().toNBT(toDisk);
            tag.put(entry.getKey(), abData);
        }

        CompoundTag energies = new CompoundTag();
        for (Map.Entry<String, EnergyBar> entry : this.energyBars.entrySet()) {
            energies.put(entry.getKey(), entry.getValue().toNBT());
        }
        tag.put("_EnergyBars", energies);

        return tag;
    }

    @Override
    public ImmutableMap<String, AbilityInstance> getAbilities() {
        return this.abilityInstances;
    }

    @Override
    public ImmutableMap<String, EnergyBar> getEnergyBars() {
        return this.energyBars;
    }

    @Override
    public void tick() {
        for (AbilityInstance abilityInstance : abilityInstances.values()) {
            abilityInstance.tick(entity, this);
        }

        if (!this.getEntity().level().isClientSide) {
            for (EnergyBar energyBar : energyBars.values()) {
                energyBar.tick(entity);
            }
        }
    }

    @Override
    public void firstTick() {
        this.abilityInstances.forEach((id, entry) -> entry.getConfiguration().getAbility().firstTick(entity, entry, this, entry.isEnabled()));
    }

    @Override
    public void lastTick() {
        this.abilityInstances.forEach((id, entry) -> entry.getConfiguration().getAbility().lastTick(entity, entry, this, entry.isEnabled()));
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
