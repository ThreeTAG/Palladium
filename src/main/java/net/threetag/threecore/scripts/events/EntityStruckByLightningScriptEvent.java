package net.threetag.threecore.scripts.events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.threetag.threecore.scripts.accessors.EntityAccessor;
import net.threetag.threecore.scripts.accessors.ScriptAccessor;

public class EntityStruckByLightningScriptEvent extends EntityScriptEvent {

    private final EntityAccessor lightningBolt;

    public EntityStruckByLightningScriptEvent(Entity entity, LightningBoltEntity lightningBoltEntity) {
        super(entity);
        this.lightningBolt = (EntityAccessor) ScriptAccessor.makeAccessor((Entity) lightningBoltEntity);
    }

    public EntityAccessor getLightningBoltEntity() {
        return lightningBolt;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
