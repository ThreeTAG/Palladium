package net.threetag.threecore.scripts.events;

import net.minecraft.entity.Entity;

public class ProjectileImpactScriptEvent  extends EntityScriptEvent{

    public ProjectileImpactScriptEvent(Entity entity) {
        super(entity);
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
