package net.threetag.palladium.client.renderer.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.entity.TrailSegmentEntityRenderer;
import net.threetag.palladium.entity.TrailSegmentEntity;

import java.awt.*;

public abstract class TrailRenderer<T extends TrailRenderer.SegmentCache> {

    @Environment(EnvType.CLIENT)
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, TrailSegmentEntityRenderer trailRenderer, LivingEntity livingEntity, TrailSegmentEntity<T> segment, float partialTick, float entityYaw) {

    }

    public SegmentCache createCache() {
        return new SegmentCache();
    }

    public Color getColor() {
        return Color.WHITE;
    }

    public abstract float getSpacing();

    public abstract int getLifetime();

    public static class SegmentCache {

    }

}
