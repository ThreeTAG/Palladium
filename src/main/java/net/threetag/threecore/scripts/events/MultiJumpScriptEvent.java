package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.ability.Ability;

public class MultiJumpScriptEvent extends AbilityScriptEvent {

    private int jump;

    public MultiJumpScriptEvent(LivingEntity livingEntity, Ability ability, int jump) {
        super(livingEntity, ability);
        this.jump = jump;
    }

    public int getJump() {
        return this.jump;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
