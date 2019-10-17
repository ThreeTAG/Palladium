package net.threetag.threecore.util.scripts.events;

import net.minecraftforge.event.entity.EntityEvent;
import net.threetag.threecore.util.scripts.accessors.LivingEntityAccessor;

public class LivingScriptEvent extends EntityScriptEvent {

    public LivingScriptEvent(EntityEvent event) {
        super(event);
    }

    public LivingEntityAccessor getLivingEntity() {
        return this.getEntity() instanceof LivingEntityAccessor ? (LivingEntityAccessor) this.getEntity() : null;
    }

}
