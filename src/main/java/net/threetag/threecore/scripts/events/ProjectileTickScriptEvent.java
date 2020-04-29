package net.threetag.threecore.scripts.events;

import net.minecraft.entity.Entity;

public class ProjectileTickScriptEvent extends EntityScriptEvent{

    public ProjectileTickScriptEvent(Entity entity) {
        super(entity);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
