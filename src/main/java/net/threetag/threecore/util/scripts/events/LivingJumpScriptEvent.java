package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;

public class LivingJumpScriptEvent extends LivingScriptEvent {

    public LivingJumpScriptEvent(LivingEntity livingEntity) {
        super(livingEntity);
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
