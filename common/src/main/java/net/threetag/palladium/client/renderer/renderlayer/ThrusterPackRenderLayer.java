package net.threetag.palladium.client.renderer.renderlayer;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.model.ThrusterHumanoidModel;
import net.threetag.palladium.client.renderer.DynamicColor;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;

import java.awt.*;

public class ThrusterPackRenderLayer extends AbstractPackRenderLayer {

    public static final ModelLayerLocation MODEL_LAYER_LOCATION = new ModelLayerLocation(Palladium.id("humanoid"), "thrusters");
    public static final ModelLayerLocation MODEL_LAYER_LOCATION_SLIM = new ModelLayerLocation(Palladium.id("humanoid"), "thrusters_slim");
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[8];
    private static final DynamicColor DEFAULT_COLOR = DynamicColor.staticColor(new Color(234, 182, 43));
    private final SkinTypedValue<ThrusterHumanoidModel<?>> model;
    private final boolean rightArm, leftArm, rightLeg, leftLeg;
    private final DynamicColor color;

    static {
        for (int i = 0; i < TEXTURES.length; i++) {
            TEXTURES[i] = Palladium.id("textures/models/thruster/" + i + ".png");
        }
    }

    public ThrusterPackRenderLayer(boolean rightArm, boolean leftArm, boolean rightLeg, boolean leftLeg, DynamicColor color) {
        this.model = new SkinTypedValue<>(
                new ThrusterHumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MODEL_LAYER_LOCATION)),
                new ThrusterHumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(MODEL_LAYER_LOCATION_SLIM))
        );
        this.rightArm = rightArm;
        this.leftArm = leftArm;
        this.rightLeg = rightLeg;
        this.leftLeg = leftLeg;
        this.color = color;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var entity = context.getLivingEntity();
        if (entity != null && IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions)) {
            ThrusterHumanoidModel model = this.model.get(entity);

            if (parentModel instanceof HumanoidModel<?> parentHumanoid) {
                IPackRenderLayer.copyModelProperties(entity, parentHumanoid, model);
            }

            model.extraAnimations(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, partialTicks);

            model.rightArmThruster.visible = this.rightArm;
            model.leftArmThruster.visible = this.leftArm;
            model.rightLegThruster.visible = this.rightLeg;
            model.leftLegThruster.visible = this.leftLeg;

            VertexConsumer vertexConsumer = bufferSource.getBuffer(PalladiumRenderTypes.getGlowing(TEXTURES[(entity.tickCount / 2) % TEXTURES.length]));
            var color = this.color.getColor(context);
            model.renderToBuffer(poseStack, vertexConsumer, 15728640, OverlayTexture.NO_OVERLAY, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
        }
    }

    @Override
    public void renderArm(DataContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.renderArm(context, arm, playerRenderer, poseStack, bufferSource, packedLight);
    }

    public static ThrusterPackRenderLayer parse(JsonObject json) {
        return new ThrusterPackRenderLayer(
                GsonHelper.getAsBoolean(json, "right_arm", true),
                GsonHelper.getAsBoolean(json, "left_arm", true),
                GsonHelper.getAsBoolean(json, "right_leg", true),
                GsonHelper.getAsBoolean(json, "left_leg", true),
                DynamicColor.getFromJson(json, "color", DEFAULT_COLOR)
        );
    }
}
