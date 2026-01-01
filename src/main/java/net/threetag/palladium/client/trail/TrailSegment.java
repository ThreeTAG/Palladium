package net.threetag.palladium.client.trail;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public class TrailSegment<T> {

    private final Vec3 pos;
    private final EntityDimensions dimensions;
    private final Trail trail;
    private final TrailRenderer<T> trailRenderer;
    private final T trailData;
    private int tickCount = 0;

    public TrailSegment(Entity entity, Trail trail, TrailRenderer<T> trailRenderer) {
        this.pos = entity.getPosition(0F);
        this.dimensions = entity.getDimensions(entity.getPose());
        this.trail = trail;
        this.trailRenderer = trailRenderer;
        this.trailData = trailRenderer.createData(entity);
    }

    public void tick() {
        this.tickCount++;
    }

    public Vec3 getPosition() {
        return pos;
    }

    public T getTrailData() {
        return trailData;
    }

    public boolean isObsolete() {
        return this.tickCount >= this.trail.lifetime();
    }

    public float getOpacity() {
        return 1F - (this.tickCount / (float) this.trail.lifetime());
    }
}
