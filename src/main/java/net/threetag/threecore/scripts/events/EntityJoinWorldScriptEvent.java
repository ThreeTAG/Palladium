package net.threetag.threecore.scripts.events;

import net.minecraft.entity.Entity;

public class EntityJoinWorldScriptEvent extends EntityScriptEvent {

    public EntityJoinWorldScriptEvent(Entity entity) {
        super(entity);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

}
