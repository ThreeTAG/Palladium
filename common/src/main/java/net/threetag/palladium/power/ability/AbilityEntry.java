package net.threetag.palladium.power.ability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.condition.Condition;

public class AbilityEntry {

    private final AbilityConfiguration abilityConfiguration;
    private boolean unlocked = true;
    private boolean enabled = true;

    public AbilityEntry(AbilityConfiguration abilityConfiguration) {
        this.abilityConfiguration = abilityConfiguration;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void tick(LivingEntity entity, Power power, IPowerHolder powerHolder) {
        this.unlocked = true;

        for (Condition unlockingCondition : this.abilityConfiguration.getUnlockingConditions()) {
            if (!unlockingCondition.active(entity, this.abilityConfiguration, power, powerHolder)) {
                this.unlocked = false;
                break;
            }
        }

        this.enabled = this.unlocked;

        if (this.unlocked) {
            for (Condition enablingCondition : this.abilityConfiguration.getEnablingConditions()) {
                if (!enablingCondition.active(entity, this.abilityConfiguration, power, powerHolder)) {
                    this.enabled = false;
                    break;
                }
            }
        }

        if (this.enabled) {
            this.abilityConfiguration.getAbility().tick(entity, this.abilityConfiguration, power, powerHolder, this.isEnabled());
        }
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("AbilityID", this.abilityConfiguration.getId());
        return nbt;
    }

    public void fromNBT(CompoundTag nbt) {
        // TODO extra storage
    }

}
