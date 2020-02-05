package net.threetag.threecore.scripts.events;

import net.minecraft.entity.Entity;
import net.threetag.threecore.scripts.accessors.EntityAccessor;
import net.threetag.threecore.scripts.accessors.ScriptAccessor;
import net.threetag.threecore.scripts.accessors.WorldAccessor;

public abstract class EntityScriptEvent extends ScriptEvent {

    private final EntityAccessor entityAccessor;

    public EntityScriptEvent(Entity entity) {
        this.entityAccessor = (EntityAccessor) ScriptAccessor.makeAccessor(entity);
    }

    public EntityAccessor getEntity() {
        return this.entityAccessor;
    }

    public WorldAccessor getWorld() {
        return getEntity().world;
    }

}
