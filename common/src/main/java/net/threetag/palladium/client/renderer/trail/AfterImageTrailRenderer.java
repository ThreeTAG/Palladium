package net.threetag.palladium.client.renderer.trail;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.entity.HumanoidRendererModifications;
import net.threetag.palladium.client.renderer.entity.TrailSegmentEntityRenderer;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.entity.TrailSegmentEntity;
import net.threetag.palladium.util.json.GsonUtil;

import java.awt.*;

public class AfterImageTrailRenderer extends TrailRenderer<TrailRenderer.SegmentCache> {

    public static final ResourceLocation TEXTURE = Palladium.id("textures/entity/trail.png");
    private final Color color;
    public final boolean mimicPlayer;
    private final float spacing;
    private final int lifetime;
    private final float opacity;

    public AfterImageTrailRenderer(Color color, boolean mimicPlayer, float spacing, int lifetime, float opacity) {
        this.color = color;
        this.mimicPlayer = mimicPlayer;
        this.spacing = spacing;
        this.lifetime = lifetime;
        this.opacity = opacity;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, TrailSegmentEntityRenderer trailRenderer, Entity livingEntity, TrailSegmentEntity<SegmentCache> segment, float partialTick, float entityYaw) {
        HumanoidRendererModifications.ALPHA_MULTIPLIER = (1F - (segment.tickCount / (float) segment.lifetime)) * this.opacity;
        trailRenderer.renderModel(segment, entityYaw, segment.partialTick, poseStack, buffer, packedLight);
        HumanoidRendererModifications.ALPHA_MULTIPLIER = 1F;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public float getSpacing() {
        return this.spacing;
    }

    @Override
    public int getLifetime() {
        return this.lifetime;
    }

    public static class Serializer implements TrailRendererManager.TypeSerializer {

        @Override
        public TrailRenderer<?> parse(JsonObject json) {
            var color = GsonUtil.getAsColor(json, "color", Color.WHITE);
            boolean mimicPlayer = GsonHelper.getAsBoolean(json, "mimic_player", false);
            float spacing = GsonUtil.getAsFloatMin(json, "spacing", 0.1F, 1F);
            int lifetime = GsonUtil.getAsIntMin(json, "lifetime", 1, 20);
            float opacity = GsonUtil.getAsFloatRanged(json, "opacity", 0F, 1F, 0.5F);
            return new AfterImageTrailRenderer(color, mimicPlayer, spacing, lifetime, opacity);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.setTitle("After Image Trail");
            builder.setDescription("Spawns copies of you as a trail.");

            builder.addProperty("color", Color.class)
                    .description("Determines the tint/color of the after image")
                    .fallback(Color.WHITE, "#ffffff").exampleJson(new JsonPrimitive("#ffffff"));
            builder.addProperty("mimic_player", Boolean.class)
                    .description("If enabled, the after image will copy the player's skin and armor")
                    .fallback(false).exampleJson(new JsonPrimitive(false));
            builder.addProperty("spacing", Float.class)
                    .description("Determines the space between two trail segments")
                    .fallback(1F).exampleJson(new JsonPrimitive(false));
            builder.addProperty("lifetime", Integer.class)
                    .description("Determines how long one trail segment stays alive (in ticks)")
                    .fallback(20).exampleJson(new JsonPrimitive(20));
            builder.addProperty("opacity", Float.class)
                    .description("Determines the (initial) opacity of the after image.")
                    .fallback(0.5F).exampleJson(new JsonPrimitive(0.5F));
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("after_image");
        }
    }

}
