package net.threetag.threecore.scripts.accessors;

import net.minecraft.util.math.EntityRayTraceResult;

/**
 * Created by Nictogen on 2020-06-23.
 */
public class EntityRayTraceResultAccessor extends ScriptAccessor<EntityRayTraceResult> {

    protected EntityRayTraceResultAccessor(EntityRayTraceResult value) {
        super(value);
    }

    public Vector3dAccessor getHitVec() {
        return new Vector3dAccessor(this.value.getHitVec());
    }

    public String getType() {
        return this.value.getType().toString();
    }

    public EntityAccessor getEntity() {
        return new EntityAccessor(this.value.getEntity());
    }
}
