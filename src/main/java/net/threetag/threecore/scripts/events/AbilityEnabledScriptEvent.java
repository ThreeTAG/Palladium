package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.ability.Ability;

public class AbilityEnabledScriptEvent extends AbilityScriptEvent {

    public AbilityEnabledScriptEvent(LivingEntity livingEntity, Ability ability) {
        super(livingEntity, ability);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
