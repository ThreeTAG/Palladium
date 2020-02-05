package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.scripts.accessors.LivingEntityAccessor;

public abstract class LivingScriptEvent extends EntityScriptEvent {

    public LivingScriptEvent(LivingEntity livingEntity) {
        super(livingEntity);
    }

    public LivingEntityAccessor getLivingEntity() {
        return this.getEntity() instanceof LivingEntityAccessor ? (LivingEntityAccessor) this.getEntity() : null;
    }

}
