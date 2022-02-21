package net.threetag.palladium.power.provider;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SuperpowerPowerProvider extends PowerProvider {

    public static final PalladiumProperty<ResourceLocation> SUPERPOWER_ID = new ResourceLocationProperty("superpower");

    @Override
    public IPowerHolder createHolder(LivingEntity entity, @Nullable Power power) {
        power = power == null ? PowerManager.getInstance(entity.level).getPower(SUPERPOWER_ID.get(entity)) : power;
        return power != null ? new PowerHolder(entity, power, this) : null;
    }

    public static class PowerHolder implements IPowerHolder {

        private final LivingEntity entity;
        private final Power power;
        private final PowerProvider provider;
        private final Map<String, AbilityEntry> entryMap = new HashMap<>();

        public PowerHolder(LivingEntity entity, Power power, PowerProvider provider) {
            this.entity = entity;
            this.power = power;
            this.provider = provider;
            for (AbilityConfiguration ability : this.getPower().getAbilities()) {
                AbilityEntry entry = new AbilityEntry(ability, this);
                entry.id = ability.getId();
                this.entryMap.put(ability.getId(), entry);
            }
        }

        @Override
        public Power getPower() {
            return this.power;
        }

        @Override
        public PowerProvider getPowerProvider() {
            return this.provider;
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
            ResourceLocation powerId = SuperpowerPowerProvider.SUPERPOWER_ID.get(this.entity);
            return this.power.isInvalid() || powerId == null || !powerId.equals(this.power.getId());
        }
    }
}
