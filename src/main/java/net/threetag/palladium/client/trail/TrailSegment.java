package net.threetag.palladium.client.trail;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TrailSegment<T> {

    private final Vec3 pos;
    private final float yRot;
    private final float xRot;
    private final EntityDimensions dimensions;
    private final AABB boundingBox;
    private final Trail trail;
    private final T trailData;
    private int tickCount = 0;
    protected TrailSegment<T> previousSegment;
    protected TrailSegment<T> nextSegment;

    public TrailSegment(Entity entity, Trail trail, TrailRenderer<T> trailRenderer) {
        this.pos = entity.getPosition(0F);
        this.yRot = entity.getPreciseBodyRotation(0F);
        this.xRot = entity.getXRot(0F);
        this.dimensions = entity.getDimensions(entity.getPose());
        this.boundingBox = this.dimensions.makeBoundingBox(this.pos);
        this.trail = trail;
        this.trailData = trailRenderer.createData(entity);
    }

    public void tick() {
        this.tickCount++;
    }

    public Vec3 getPosition() {
        return this.pos;
    }

    public float getXRot() {
        return xRot;
    }

    public float getYRot() {
        return yRot;
    }

    public T getTrailData() {
        return this.trailData;
    }

    public boolean isObsolete() {
        return this.tickCount >= this.trail.lifetime();
    }

    public float getOpacity() {
        return 1F - (this.tickCount / (float) this.trail.lifetime());
    }

    public EntityDimensions getDimensions() {
        return this.dimensions;
    }

    public AABB getBoundingBox() {
        return this.boundingBox;
    }

    @Nullable
    public TrailSegment<T> getPreviousSegment() {
        return this.previousSegment;
    }

    @Nullable
    public TrailSegment<T> getNextSegment() {
        return this.nextSegment;
    }
}
