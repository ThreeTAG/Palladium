package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;

public class LivingUpdateScriptEvent extends LivingScriptEvent {

    public LivingUpdateScriptEvent(LivingEntity livingEntity) {
        super(livingEntity);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
