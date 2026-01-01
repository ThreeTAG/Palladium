package net.threetag.palladium.client.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.Entity;

public abstract class TrailRenderer<T> {

    public static Codec<TrailRenderer<?>> CODEC = TrailRendererSerializer.TYPE_CODEC.dispatch(TrailRenderer::getSerializer, TrailRendererSerializer::codec);

    public abstract void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TrailSegment<T> trailSegment, int packedLight, float partialTick);

    public abstract T createData(Entity parent);

    public abstract TrailRendererSerializer<?> getSerializer();

}
