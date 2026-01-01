package net.threetag.palladium.client.trail;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.PalladiumCodecs;

import java.awt.*;

public class AfterImageTrailRenderer extends TrailRenderer<AfterImageTrailRenderer.Data> {

    public static MapCodec<AfterImageTrailRenderer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            PalladiumCodecs.COLOR_CODEC.optionalFieldOf("color", Color.WHITE).forGetter(r -> r.color),
            PalladiumCodecs.FLOAT_0_TO_1.optionalFieldOf("opacity", 0.5F).forGetter(r -> r.opacity)
    ).apply(instance, AfterImageTrailRenderer::new));

    private final Color color;
    private final float opacity;

    public AfterImageTrailRenderer(Color color, float opacity) {
        this.color = color;
        this.opacity = opacity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, TrailSegment<AfterImageTrailRenderer.Data> trailSegment, int packedLight, float partialTick) {
        AfterImageTrailRenderer.Data trailData = trailSegment.getTrailData();
        trailData.renderState.setRenderData(PalladiumRenderStateKeys.OPACITY, trailSegment.getOpacity() * this.opacity);
        trailData.renderer.submit(trailData.renderState, poseStack, submitNodeCollector, Minecraft.getInstance().gameRenderer.getLevelRenderState().cameraRenderState);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Data createData(Entity entity) {
        var data = new Data();
        PalladiumRenderStateKeys.IGNORE_TRAILS = true;
        data.renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        data.renderState = data.renderer.createRenderState(entity, 1F);
        data.renderState.setRenderData(PalladiumRenderStateKeys.TINT, this.color.getRGB());
        PalladiumRenderStateKeys.IGNORE_TRAILS = false;
        return data;
    }

    @Override
    public TrailRendererSerializer<?> getSerializer() {
        return TrailRendererSerializers.AFTER_IMAGE;
    }

    @SuppressWarnings("rawtypes")
    public static class Data {

        private EntityRenderer renderer;
        private EntityRenderState renderState;

    }

    public static class Serializer extends TrailRendererSerializer<AfterImageTrailRenderer> {

        @Override
        public MapCodec<AfterImageTrailRenderer> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<TrailRenderer<?>, AfterImageTrailRenderer> builder, HolderLookup.Provider provider) {

        }
    }
}
