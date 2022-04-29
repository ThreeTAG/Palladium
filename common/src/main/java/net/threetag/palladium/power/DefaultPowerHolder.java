package net.threetag.palladium.power;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DefaultPowerHolder implements IPowerHolder {

    public final LivingEntity entity;
    private final Power power;
    private final Map<String, AbilityEntry> entryMap = new HashMap<>();
    private final Function<DefaultPowerHolder, Boolean> invalidChecker;

    public DefaultPowerHolder(LivingEntity entity, Power power, Function<DefaultPowerHolder, Boolean> invalidChecker) {
        this.entity = entity;
        this.power = power;
        for (AbilityConfiguration ability : this.getPower().getAbilities()) {
            AbilityEntry entry = new AbilityEntry(ability, this);
            entry.id = ability.getId();
            this.entryMap.put(ability.getId(), entry);
        }
        this.invalidChecker = invalidChecker;
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
        return this.power.isInvalid() || this.invalidChecker.apply(this);
    }
}
