package net.threetag.threecore.util.scripts.events;

import net.minecraftforge.event.entity.EntityEvent;
import net.threetag.threecore.util.scripts.accessors.EntityAccessor;
import net.threetag.threecore.util.scripts.accessors.WorldAccessor;

public class EntityScriptEvent extends ScriptEvent {

    private final EntityAccessor entityAccessor;

    public EntityScriptEvent(EntityEvent event) {
        super(event);
        this.entityAccessor = EntityAccessor.create(event.getEntity());
    }

    private EntityEvent getEntityEvent() {
        return (EntityEvent) this.event;
    }

    public EntityAccessor getEntity() {
        return this.entityAccessor;
    }

    public WorldAccessor getWorld() {
        return getEntity().world;
    }

}
