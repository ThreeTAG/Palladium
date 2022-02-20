package net.threetag.palladium.power.ability;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.SyncAbilityStateMessage;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.condition.Condition;
import net.threetag.palladium.util.property.PalladiumProperty;

public class AbilityEntry {

    private final AbilityConfiguration abilityConfiguration;
    private final IPowerHolder holder;
    private boolean unlocked = true;
    private boolean enabled = true;
    private int ticks = 0;

    public AbilityEntry(AbilityConfiguration abilityConfiguration, IPowerHolder holder) {
        this.abilityConfiguration = abilityConfiguration;
        this.holder = holder;
    }

    public AbilityConfiguration getConfiguration() {
        return abilityConfiguration;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getTicks() {
        return ticks;
    }

    public void setClientState(LivingEntity entity, IPowerHolder powerHolder, boolean unlocked, boolean enabled) {
        this.unlocked = unlocked;

        if (this.enabled != enabled) {
            this.enabled = enabled;

            if (this.enabled) {
                this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
            } else {
                this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, this.isEnabled());
            }
        }
    }

    public void tick(LivingEntity entity, Power power, IPowerHolder powerHolder) {
        if (!entity.level.isClientSide) {
            boolean unlocked = true;
            boolean sync = false;

            for (Condition unlockingCondition : this.abilityConfiguration.getUnlockingConditions()) {
                if (!unlockingCondition.active(entity, this, power, powerHolder)) {
                    unlocked = false;
                    break;
                }
            }

            boolean enabled = this.unlocked;

            if (this.unlocked) {
                for (Condition enablingCondition : this.abilityConfiguration.getEnablingConditions()) {
                    if (!enablingCondition.active(entity, this, power, powerHolder)) {
                        enabled = false;
                        break;
                    }
                }
            }

            if (this.unlocked != unlocked) {
                this.unlocked = unlocked;
                sync = true;
            }

            if (this.enabled != enabled) {
                this.enabled = enabled;
                sync = true;

                if (this.enabled) {
                    this.abilityConfiguration.getAbility().firstTick(entity, this, powerHolder, this.isEnabled());
                } else {
                    this.abilityConfiguration.getAbility().lastTick(entity, this, powerHolder, this.isEnabled());
                }
            }

            if (sync || ticks == 0) {
                new SyncAbilityStateMessage(entity.getId(), this.holder.getPowerProvider(), this.abilityConfiguration.getId(), this.unlocked, this.enabled).sendToLevel((ServerLevel) entity.getLevel());
            }
        }

        this.abilityConfiguration.getAbility().tick(entity, this, powerHolder, this.isEnabled());
        this.ticks++;
    }

    public <T> T getProperty(PalladiumProperty<T> property) {
        return this.abilityConfiguration.get(property);
    }

}
