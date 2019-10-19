package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.abilities.Ability;

public class AbilityDisabledScriptEvent extends AbilityScriptEvent {

    public AbilityDisabledScriptEvent(LivingEntity livingEntity, Ability ability) {
        super(livingEntity, ability);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
