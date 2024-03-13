package net.threetag.palladium.client.renderer.trail;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.entity.TrailSegmentEntityRenderer;
import net.threetag.palladium.documentation.JsonDocumentationBuilder;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.entity.TrailSegmentEntity;
import net.threetag.palladium.util.RenderUtil;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class LightningTrailRenderer extends TrailRenderer<LightningTrailRenderer.Cache> {

    private final Color color;
    private final Color coreColor;
    private final float spacing;
    private final int lifetime;
    private final int amount;
    private final float spreadX, spreadY;
    private final float thickness;

    public LightningTrailRenderer(Color color, Color coreColor, float spacing, int lifetime, int amount, float spreadX, float spreadY, float thickness) {
        this.color = color;
        this.coreColor = coreColor;
        this.spacing = spacing;
        this.lifetime = lifetime;
        this.amount = amount;
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.thickness = thickness;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, TrailSegmentEntityRenderer trailRenderer, LivingEntity livingEntity, TrailSegmentEntity<Cache> segment, float partialTick, float entityYaw) {
        var vertexConsumer = buffer.getBuffer(PalladiumRenderTypes.LASER);

        if (livingEntity instanceof PalladiumLivingEntityExtension ext) {
            var trails = ext.palladium$getTrailHandler().getTrails().get(this);
            var index = trails.indexOf(segment);

            if (index == trails.size() - 1) {
                for (int i = 0; i < 4; i++) {
                    renderSegmentWithChild(poseStack, vertexConsumer, segment, trails, partialTick, index, i);
                }
            }
        }
    }

    private void renderSegmentWithChild(PoseStack poseStack, VertexConsumer vertexConsumer, TrailSegmentEntity<Cache> segment, List<TrailSegmentEntity<?>> segments, float partialTick, int index, int stage) {
        if (index > 0) {
            var previousSegment = segments.get(index - 1);
            var cache = segment.cache;
            var previousC = previousSegment.cache;

            if (previousC instanceof Cache previousCache && cache.offsets.length == previousCache.offsets.length) {
                for (int i = 0; i < cache.offsets.length; i++) {
                    var start = getOffsetPos(segment, null, cache.offsets[i]);
                    var end = getOffsetPos(previousSegment, null, previousCache.offsets[i]).add(previousSegment.position().subtract(segment.position()));
                    float opacity = 1F - ((segment.tickCount + partialTick) / (float) segment.lifetime);

                    poseStack.pushPose();
                    poseStack.translate(start.x, start.y, start.z);
                    faceVec(poseStack, start, end);
                    poseStack.mulPose(Axis.XP.rotationDegrees(90));
                    renderBox(poseStack, vertexConsumer, (float) start.distanceTo(end), this.thickness, stage == 0 ? this.coreColor : this.color, opacity, stage);
                    poseStack.popPose();
                }

                poseStack.pushPose();
                var offsetPos = previousSegment.position().subtract(segment.position());
                poseStack.translate(offsetPos.x, offsetPos.y, offsetPos.z);
                this.renderSegmentWithChild(poseStack, vertexConsumer, (TrailSegmentEntity<Cache>) previousSegment, segments, partialTick, index - 1, stage);
                poseStack.popPose();
            }
        }
    }

    private static void renderBox(PoseStack poseStack, VertexConsumer consumer, float length, float width, Color color, float alpha, int stage) {
        AABB box = new AABB(-width / 2F, 0, -width / 2F, width / 2F, length, width / 2F);

        if (stage == 0) {
            RenderUtil.renderFilledBox(poseStack, consumer, box, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha, 15728640);
        } else {
            int i = stage - 1;
            RenderUtil.renderFilledBox(poseStack, consumer, box.inflate(i * 0.5F * 0.0625F), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, (1F / i / 2) * alpha, 15728640);
        }
    }

    public static Vec3 getOffsetPos(TrailSegmentEntity<?> segment, @Nullable TrailSegmentEntity<?> previous, Vec3 offset) {
        if (previous == null) {
            return new Vec3(offset.x * segment.getBbWidth(), (segment.getBbHeight() / 2D) + (offset.y * segment.getBbHeight()), offset.z * segment.getBbWidth());
        } else {
            var offsetBetween = previous.position().subtract(segment.position());
            return new Vec3(offsetBetween.x - (offset.x * previous.getBbWidth()), offsetBetween.y + (previous.getBbHeight() / 2D) + (offset.y * previous.getBbHeight()), offsetBetween.z - (offset.z * previous.getBbWidth()));
        }
    }

    public static void faceVec(PoseStack poseStack, Vec3 src, Vec3 dst) {
        double x = dst.x - src.x;
        double y = dst.y - src.y;
        double z = dst.z - src.z;
        double diff = Mth.sqrt((float) (x * x + z * z));
        float yaw = (float) (Math.atan2(z, x) * 180 / Math.PI) - 90;
        float pitch = (float) -(Math.atan2(y, diff) * 180 / Math.PI);

        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(pitch));
    }

    @Override
    public SegmentCache createCache() {
        var random = new Random();
        var offsets = new Vec3[this.amount];

        for (int i = 0; i < this.amount; i++) {
            var spacingY = (1D / this.amount) * this.spreadY;
            offsets[i] = new Vec3((random.nextDouble() - 0.5D) * this.spreadX, ((spacingY * this.amount) / -2D) + (spacingY * i) + (spacingY / 2D) + ((random.nextDouble() - 0.5F) * spacingY / 1.5D), (random.nextDouble() - 0.5D) * this.spreadX);
        }

        return new Cache(offsets);
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

    public static class Cache extends SegmentCache {

        private final Vec3[] offsets;

        public Cache(Vec3[] offsets) {
            this.offsets = offsets;
        }
    }

    public static class Serializer implements TrailRendererManager.TypeSerializer {

        @Override
        public TrailRenderer<Cache> parse(JsonObject json) {
            var color = GsonUtil.getAsColor(json, "color", Color.WHITE);
            var coreColor = GsonUtil.getAsColor(json, "core_color", Color.WHITE);
            float spacing = GsonUtil.getAsFloatMin(json, "spacing", 0.1F, 1F);
            int lifetime = GsonUtil.getAsIntMin(json, "lifetime", 1, 20);
            int amount = GsonUtil.getAsIntMin(json, "amount", 1, 10);
            float spreadX = GsonUtil.getAsFloatMin(json, "spread_x", 0F, 1F);
            float spreadY = GsonUtil.getAsFloatMin(json, "spread_y", 0F, 1F);
            float thickness = GsonUtil.getAsFloatMin(json, "thickness", 0.001F, 0.1F);
            return new LightningTrailRenderer(color, coreColor, spacing, lifetime, amount, spreadX, spreadY, thickness);
        }

        @Override
        public void generateDocumentation(JsonDocumentationBuilder builder) {
            builder.addProperty("color", Color.class)
                    .description("Determines the tint/color of glow")
                    .fallback(Color.WHITE).exampleJson(new JsonPrimitive("#ffffff"));
            builder.addProperty("core_color", Color.class)
                    .description("Determines the tint/color of inner core")
                    .fallback(Color.WHITE).exampleJson(new JsonPrimitive("#ffffff"));
            builder.addProperty("spacing", Float.class)
                    .description("Determines the space between two trail segments")
                    .fallback(1F);
            builder.addProperty("lifetime", Integer.class)
                    .description("Determines how long one trail segment stays alive (in ticks)")
                    .fallback(20);
            builder.addProperty("amount", Integer.class)
                    .description("Determines how many lightnings the entity will generate behind it")
                    .fallback(7);
            builder.addProperty("spread_x", Float.class)
                    .description("Determines the spread of a lightning position relative to the player on the X/horizontal axis. 1 means across the normal player hitbox, 0 means always in the middle.")
                    .fallback(1F);
            builder.addProperty("spread_y", Float.class)
                    .description("Determines the spread of a lightning position relative to the player on the Y/vertical axis. 1 means across the normal player hitbox, 0 means always in the middle.")
                    .fallback(1F);
            builder.addProperty("thickness", Float.class)
                    .description("Determines the thickness of one lightning bolt.")
                    .fallback(0.05F);
        }

        @Override
        public ResourceLocation getId() {
            return Palladium.id("lightning");
        }
    }

}
