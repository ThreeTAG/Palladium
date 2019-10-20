package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.Entity;
import net.threetag.threecore.util.scripts.accessors.EntityAccessor;
import net.threetag.threecore.util.scripts.accessors.ScriptAccessor;
import net.threetag.threecore.util.scripts.accessors.WorldAccessor;

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
