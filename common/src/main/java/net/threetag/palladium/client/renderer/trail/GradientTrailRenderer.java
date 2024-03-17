package net.threetag.palladium.client.renderer.trail;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.entity.TrailSegmentEntityRenderer;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.entity.PalladiumEntityExtension;
import net.threetag.palladium.entity.TrailSegmentEntity;
import net.threetag.palladium.util.json.GsonUtil;
import org.joml.Matrix4f;

import java.awt.*;

public class GradientTrailRenderer extends TrailRenderer<TrailRenderer.SegmentCache> {

    private final float spacing;
    private final int lifetime;
    private final Color color;
    private final float opacity;
    private final boolean normalTransparency;

    public GradientTrailRenderer(float spacing, int lifetime, Color color, float opacity, boolean normalTransparency) {
        this.spacing = spacing;
        this.lifetime = lifetime;
        this.color = color;
        this.opacity = opacity;
        this.normalTransparency = normalTransparency;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, TrailSegmentEntityRenderer trailRenderer, Entity livingEntity, TrailSegmentEntity<SegmentCache> segment, float partialTick, float entityYaw) {
        if (livingEntity instanceof PalladiumEntityExtension ext) {
            var trails = ext.palladium$getTrailHandler().getTrails().get(this);
            var index = trails.indexOf(segment);

            if (index > 0) {
                var vertexConsumer = buffer.getBuffer(this.normalTransparency ? PalladiumRenderTypes.LASER_NORMAL_TRANSPARENCY : PalladiumRenderTypes.LASER);
                float r = this.color.getRed() / 255F;
                float g = this.color.getGreen() / 255F;
                float b = this.color.getBlue() / 255F;
                float segmentOpacity = 1F - Mth.clamp((segment.tickCount + partialTick) / segment.lifetime, 0, 1);

                if (index == trails.size() - 1) {
                    poseStack.pushPose();
                    Vec3 endPos = livingEntity.getPosition(partialTick).subtract(segment.position());
                    Matrix4f matrix = poseStack.last().pose();

                    vertexConsumer.vertex(matrix, 0, 0, 0).color(r, g, b, this.opacity * segmentOpacity).uv2(packedLight).endVertex();
                    vertexConsumer.vertex(matrix, 0, segment.getBbHeight(), 0).color(r, g, b, this.opacity * segmentOpacity).uv2(packedLight).endVertex();
                    vertexConsumer.vertex(matrix, (float) endPos.x, (float) (endPos.y + livingEntity.getBbHeight()), (float) endPos.z).color(r, g, b, this.opacity * segmentOpacity).uv2(packedLight).endVertex();
                    vertexConsumer.vertex(matrix, (float) endPos.x, (float) endPos.y, (float) endPos.z).color(r, g, b, this.opacity * segmentOpacity).uv2(packedLight).endVertex();

                    poseStack.popPose();
                }

                var prev = trails.get(index - 1);

                if (!prev.isAlive()) {
                    return;
                }

                Vec3 endPos = prev.position().subtract(segment.position());
                float prevOpacity = 1F - Mth.clamp((prev.tickCount + partialTick) / prev.lifetime, 0, 1);

                poseStack.pushPose();
                Matrix4f matrix = poseStack.last().pose();

                vertexConsumer.vertex(matrix, 0, 0, 0).color(r, g, b, this.opacity * segmentOpacity).uv2(packedLight).endVertex();
                vertexConsumer.vertex(matrix, 0, segment.getBbHeight(), 0).color(r, g, b, this.opacity * segmentOpacity).uv2(packedLight).endVertex();
                vertexConsumer.vertex(matrix, (float) endPos.x, (float) (endPos.y + prev.getBbHeight()), (float) endPos.z).color(r, g, b, this.opacity * prevOpacity).uv2(packedLight).endVertex();
                vertexConsumer.vertex(matrix, (float) endPos.x, (float) endPos.y, (float) endPos.z).color(r, g, b, this.opacity * prevOpacity).uv2(packedLight).endVertex();

                poseStack.popPose();
            }
        }
    }

    @Override
    public float getSpacing() {
        return this.spacing;
    }

    @Override
    public int getLifetime() {
        return this.lifetime;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    public static class Serializer implements TrailRendererManager.TypeSerializer {

        @Override
        public TrailRenderer<?> parse(JsonObject json) {
            var color = GsonUtil.getAsColor(json, "color", Color.WHITE);
            float spacing = GsonUtil.getAsFloatMin(json, "spacing", 0.1F, 1F);
            int lifetime = GsonUtil.getAsIntMin(json, "lifetime", 1, 20);
            float opacity = GsonUtil.getAsFloatRanged(json, "opacity", 0F, 1F, 0.5F);
            boolean normalTransparency = GsonHelper.getAsBoolean(json, "normal_transparency", false);
            return new GradientTrailRenderer(spacing, lifetime, color, opacity, normalTransparency);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.addProperty("color", Color.class)
                    .description("Determines the tint/color of the gradient")
                    .fallback(Color.WHITE, "#ffffff").exampleJson(new JsonPrimitive("#ffffff"));
            builder.addProperty("spacing", Float.class)
                    .description("Determines the space between two trail segments")
                    .fallback(1F).exampleJson(new JsonPrimitive(false));
            builder.addProperty("lifetime", Integer.class)
                    .description("Determines how long one trail segment stays alive (in ticks)")
                    .fallback(20).exampleJson(new JsonPrimitive(20));
            builder.addProperty("opacity", Float.class)
                    .description("Determines the (initial) opacity of the after image.")
                    .fallback(0.5F).exampleJson(new JsonPrimitive(0.5F));
            builder.addProperty("normal_transparency", Boolean.class)
                    .description("Can be turned on if you want to make a gradient black.")
                    .fallback(false).exampleJson(new JsonPrimitive(false));
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("gradient");
        }
    }
}
