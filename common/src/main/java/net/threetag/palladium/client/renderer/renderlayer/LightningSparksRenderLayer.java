package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.entity.BodyPart;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.util.RenderUtil;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LightningSparksRenderLayer extends AbstractPackRenderLayer {

    private final float frequency;
    private final int amount;
    private final Color coreColor;
    private final Color glowColor;
    private final float coreOpacity;
    private final float glowOpacity;
    private final float thickness;
    private final boolean normalTransparency;

    public LightningSparksRenderLayer(float frequency, int amount, Color coreColor, Color glowColor, float coreOpacity, float glowOpacity, float thickness, boolean normalTransparency) {
        this.frequency = frequency;
        this.amount = amount;
        this.coreColor = coreColor;
        this.glowColor = glowColor;
        this.coreOpacity = coreOpacity;
        this.glowOpacity = glowOpacity;
        this.thickness = thickness;
        this.normalTransparency = normalTransparency;
    }

    public static LightningSparksRenderLayer parse(JsonObject json) {
        float frequency = GsonUtil.getAsFloatRanged(json, "frequency", 0, 1, 0.5F);
        int amount = GsonUtil.getAsIntMin(json, "amount", 0, 5);
        Color coreColor = GsonUtil.getAsColor(json, "core_color", Color.WHITE);
        Color glowColor = GsonUtil.getAsColor(json, "glow_color", Color.WHITE);
        float coreOpacity = GsonUtil.getAsFloatRanged(json, "core_opacity", 0, 1, 1F);
        float glowOpacity = GsonUtil.getAsFloatRanged(json, "glow_opacity", 0, 1, 1F);
        float thickness = GsonUtil.getAsFloatMin(json, "thickness", 0.001F, 0.02F);
        boolean normalTransparency = GsonHelper.getAsBoolean(json, "normal_transparency", false);

        var layer = new LightningSparksRenderLayer(frequency, amount, coreColor, glowColor, coreOpacity, glowOpacity, thickness, normalTransparency);

        GsonUtil.ifHasKey(json, "hidden_body_parts", el -> {
            if (el.isJsonPrimitive()) {
                var string = el.getAsString();
                if (string.equalsIgnoreCase("all")) {
                    for (BodyPart bodyPart : BodyPart.values()) {
                        layer.addHiddenBodyPart(bodyPart);
                    }
                } else {
                    layer.addHiddenBodyPart(BodyPart.fromJson(string));
                }
            } else if (el.isJsonArray()) {
                JsonArray jsonArray = el.getAsJsonArray();
                for (JsonElement jsonElement : jsonArray) {
                    layer.addHiddenBodyPart(BodyPart.fromJson(jsonElement.getAsString()));
                }
            } else {
                throw new JsonParseException("hidden_body_parts setting must either be a string or an array");
            }
        });

        return layer;
    }

    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var entity = context.getLivingEntity();
        var state = getState(entity);

        if (state != null && entity != null) {
            var vertexConsumer = bufferSource.getBuffer(this.normalTransparency ? PalladiumRenderTypes.LASER_NORMAL_TRANSPARENCY : PalladiumRenderTypes.LASER);

            for (Spark spark : state.sparks) {
                float opacity = 1F - Mth.clamp((spark.ticks + partialTicks) / 4, 0, 1);

                poseStack.pushPose();
                poseStack.translate(0, 24 / 16F, 0);
                poseStack.mulPose(Axis.XP.rotationDegrees(180));
                poseStack.translate(entity.getBbWidth() / -2, 0, entity.getBbWidth() / -2);
                renderLightning(poseStack, vertexConsumer, spark.pos1, spark.pos2, this.coreColor, this.glowColor, opacity * this.coreOpacity, opacity * this.glowOpacity, this.thickness);
                renderLightning(poseStack, vertexConsumer, spark.pos1, spark.pos3, this.coreColor, this.glowColor, opacity * this.coreOpacity, opacity * this.glowOpacity, this.thickness);
                poseStack.popPose();
            }
        }
    }

    private static void renderLightning(PoseStack poseStack, VertexConsumer consumer, Vec3 start, Vec3 end, Color coreColor, Color glowColor, float coreOpacity, float glowOpacity, float thickness) {
        poseStack.pushPose();
        poseStack.translate(start.x, start.y, start.z);
        RenderUtil.faceVec(poseStack, start, end);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        AABB box = new AABB(-thickness / 2F, 0, -thickness / 2F, thickness / 2F, start.distanceTo(end), thickness / 2F);
        RenderUtil.renderFilledBox(poseStack, consumer, box, coreColor.getRed() / 255F, coreColor.getGreen() / 255F, coreColor.getBlue() / 255F, coreOpacity, 15728640);
        RenderUtil.renderFilledBox(poseStack, consumer, box.inflate(0.5F * 0.0625F), glowColor.getRed() / 255F, glowColor.getGreen() / 255F, glowColor.getBlue() / 255F, (1F / 1F / 2) * glowOpacity, 15728640);
        poseStack.popPose();
    }

    @Nullable
    public State getState(LivingEntity entity) {
        if (entity instanceof PalladiumLivingEntityExtension extension) {
            return extension.palladium$getRenderLayerStates().getOrCreate(this) instanceof State state ? state : null;
        }
        return null;
    }

    @Override
    public RenderLayerStates.State createState() {
        return new State(this);
    }

    public static class State extends RenderLayerStates.State {

        private final LightningSparksRenderLayer layer;
        private List<Spark> sparks = new ArrayList<>();

        public State(LightningSparksRenderLayer layer) {
            this.layer = layer;
        }

        @Override
        public void tick(LivingEntity entity) {
            super.tick(entity);

            this.sparks.forEach(Spark::tick);
            this.sparks = this.sparks.stream().peek(Spark::tick).filter(s -> s.ticks < 5).collect(Collectors.toList());

            if (this.sparks.isEmpty() && Math.random() < this.layer.frequency) {
                for (int i = 0; i < this.layer.amount; i++) {
                    this.sparks.add(new Spark(entity, RandomSource.create()));
                }
            }
        }
    }

    public static class Spark {

        public final Vec3 pos1;
        public final Vec3 pos2;
        public final Vec3 pos3;
        private int ticks;

        public Spark(Entity entity, RandomSource randomSource) {
            var random = new Random();
            this.pos1 = new Vec3(entity.getBbWidth() * random.nextFloat(), entity.getBbHeight() * random.nextFloat(), entity.getBbWidth() * random.nextFloat());
            this.pos2 = this.makePos(this.pos1, entity.getBbWidth(), entity.getBbHeight(), randomSource);
            this.pos3 = this.makePos(this.pos1, entity.getBbWidth(), entity.getBbHeight(), randomSource);
        }

        private Vec3 makePos(Vec3 center, float width, float height, RandomSource source) {
            float length = (width + height) / 20F;
            float x = (float) Mth.clamp(center.x + ((source.nextFloat() * 2 - 1) * length), 0, width);
            float y = (float) Mth.clamp(center.y + ((source.nextFloat() * 2 - 1) * length), 0, height);
            float z = (float) Mth.clamp(center.z + ((source.nextFloat() * 2 - 1) * length), 0, width);
            return new Vec3(x, y, z);
        }

        private void tick() {
            this.ticks++;
        }
    }

}
