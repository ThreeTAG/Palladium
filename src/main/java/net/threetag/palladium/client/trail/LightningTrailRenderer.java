package net.threetag.palladium.client.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.LaserRenderer;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class LightningTrailRenderer extends TrailRenderer<LightningTrailRenderer.Data> {

    public static final MapCodec<LightningTrailRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("amount", 10).forGetter(r -> r.amount),
            LaserRenderer.codec(1).fieldOf("render_settings").forGetter(r -> r.laserRenderer)
    ).apply(instance, LightningTrailRenderer::new));

    private final int amount;
    private final LaserRenderer laserRenderer;

    public LightningTrailRenderer(int amount, LaserRenderer laserRenderer) {
        this.amount = amount;
        this.laserRenderer = laserRenderer;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LivingEntityRenderState ownerRenderState, TrailSegment<LightningTrailRenderer.Data> trailSegment, int packedLight, float partialTick) {
        var nextSegment = trailSegment.getNextSegment();
        var nextPos = (nextSegment != null ? nextSegment.getPosition() : new Vec3(ownerRenderState.x, ownerRenderState.y, ownerRenderState.z)).subtract(trailSegment.getPosition());
        var nextDimensions = nextSegment != null ? nextSegment.getDimensions() : EntityDimensions.fixed(ownerRenderState.boundingBoxWidth, ownerRenderState.boundingBoxHeight);
        var opacity = trailSegment.getOpacity();

        for (int i = 0; i < this.amount; i++) {
            var offsets = trailSegment.getTrailData().offsets[i];
            var nextOffsets = nextSegment != null ? nextSegment.getTrailData().offsets[i] : offsets;

            var start = getOffsetPos(nextPos, nextOffsets, nextDimensions);
            var end = getOffsetPos(Vec3.ZERO, offsets, trailSegment.getDimensions()).subtract(start);

            poseStack.pushPose();
            poseStack.translate(start.x, start.y, start.z);
            this.laserRenderer.faceAndRender(poseStack, submitNodeCollector,
                    Vec3.ZERO,
                    end,
                    (int) ownerRenderState.ageInTicks, ownerRenderState.partialTick,
                    1F, opacity, opacity);
            poseStack.popPose();
        }
    }

    private static Vec3 getOffsetPos(Vec3 origin, Vec3 offset, EntityDimensions dimensions) {
        return origin.subtract(0, dimensions.height() / -2F, 0).add(offset.multiply(dimensions.width(), dimensions.height(), dimensions.width()));
    }

    @Override
    public Data createData(Entity parent) {
        var trailData = new Data();
        var random = RandomSource.create();
        trailData.offsets = new Vec3[this.amount];

        for (int i = 0; i < this.amount; i++) {
            var spacingY = (1D / this.amount);
            trailData.offsets[i] = new Vec3((random.nextDouble() - 0.5D), ((spacingY * this.amount) / -2D) + (spacingY * i) + (spacingY / 2D) + ((random.nextDouble() - 0.5F) * spacingY / 1.5D), (random.nextDouble() - 0.5D));
        }

        return trailData;
    }

    @Override
    public TrailRendererSerializer<?> getSerializer() {
        return TrailRendererSerializers.LIGHTNING;
    }

    public static class Data {

        private Vec3[] offsets;

    }

    public static class Serializer extends TrailRendererSerializer<LightningTrailRenderer> {

        @Override
        public MapCodec<LightningTrailRenderer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<TrailRenderer<?>, LightningTrailRenderer> builder, HolderLookup.Provider provider) {

        }
    }
}
