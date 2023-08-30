package net.threetag.palladium.compat.geckolib.renderlayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.client.renderer.renderlayer.AbstractPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.client.renderer.renderlayer.RenderTypeFunction;
import net.threetag.palladium.compat.geckolib.playeranimator.ParsedAnimationController;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"unchecked", "rawtypes", "ConstantValue"})
public class GeckoRenderLayer extends AbstractPackRenderLayer {

    private final SkinTypedValue<DynamicTexture> texture;
    private final SkinTypedValue<ResourceLocation> modelLocation;
    public final ResourceLocation animationLocation;
    public final List<ParsedAnimationController<GeckoLayerState>> animationControllers;
    public ResourceLocation cachedTexture;
    public ResourceLocation cachedModel;
    public final RenderTypeFunction renderType;
    private final GeckoRenderLayerModel model;

    public GeckoRenderLayer(SkinTypedValue<DynamicTexture> texture, SkinTypedValue<ResourceLocation> modelLocation, ResourceLocation animationLocation, List<ParsedAnimationController<GeckoLayerState>> animationControllers, RenderTypeFunction renderType) {
        this.texture = texture;
        this.renderType = renderType;
        this.modelLocation = modelLocation;
        this.animationLocation = animationLocation;
        this.animationControllers = animationControllers;
        this.model = new GeckoRenderLayerModel(this);
    }

    public GeckoRenderLayerModel getModel() {
        return model;
    }

    @Nullable
    public GeckoLayerState getState(LivingEntity entity) {
        if (entity instanceof PalladiumLivingEntityExtension extension) {
            return extension.palladium$getRenderLayerStates().getOrCreate(this) instanceof GeckoLayerState state ? state : null;
        }
        return null;
    }

    @Override
    public void render(DataContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var living = context.getLivingEntity();

        if (living != null && IPackRenderLayer.conditionsFulfilled(living, this.conditions, this.thirdPersonConditions) && parentModel instanceof HumanoidModel parentHumanoid) {
            this.model.setCurrentRenderingFields(getState(living), living, parentHumanoid);
            HumanoidModel entityModel = this.model;
            entityModel.setAllVisible(true);

            this.cachedTexture = this.texture.get(living).getTexture(context);
            this.cachedModel = this.modelLocation.get(living);
            IPackRenderLayer.copyModelProperties(living, parentHumanoid, entityModel);

            if (entityModel instanceof GeckoRenderLayerModel gecko) {
                gecko.renderToBuffer(poseStack, this.renderType.createVertexConsumer(bufferSource, this.cachedTexture, false), packedLight, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            }
        }
    }

    @Override
    public void renderArm(DataContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
//        var living = context.getLivingEntity();
//        if (living != null && IPackRenderLayer.conditionsFulfilled(living, this.conditions, this.firstPersonConditions)) {
//            this.model.setRenderedEntity(living);
//            GeckoRenderLayerModel humanoidModel = this.model;
//            this.cachedTexture = this.texture.get(living).getTexture(context);
//            this.cachedModel = this.modelLocation.get(living);
//
//            playerRenderer.getModel().copyPropertiesTo(humanoidModel);
//            humanoidModel.attackTime = 0.0F;
//            humanoidModel.crouching = false;
//            humanoidModel.swimAmount = 0.0F;
//            humanoidModel.rightArm.xRot = 0.0F;
//            humanoidModel.leftArm.xRot = 0.0F;
//            humanoidModel.renderArm(context, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getFrameTime(), arm == HumanoidArm.RIGHT);
//        }
    }

    public static GeckoRenderLayer parse(JsonObject json) {
        SkinTypedValue<ResourceLocation> modelLocation = SkinTypedValue.fromJSON(json.get("model"), js -> GsonUtil.convertToResourceLocation(js, "model"));
        var texture = SkinTypedValue.fromJSON(json.get("texture"), DynamicTexture::parse);
        var renderType = PackRenderLayerManager.getRenderType(new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")));

        if (renderType == null) {
            throw new JsonParseException("Unknown render type '" + new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")) + "'");
        }

        var layer = new GeckoRenderLayer(
                texture,
                modelLocation,
                GsonUtil.getAsResourceLocation(json, "animation_file", null),
                GsonUtil.fromListOrPrimitive(json.get("animation_controller"), el -> ParsedAnimationController.controllerFromJson(el.getAsJsonObject())),
                renderType
        );

        var bonesJson = GsonHelper.getAsJsonObject(json, "bones", new JsonObject());
        layer.model.headBone = GsonHelper.getAsString(bonesJson, "head", layer.model.headBone);
        layer.model.bodyBone = GsonHelper.getAsString(bonesJson, "body", layer.model.bodyBone);
        layer.model.rightArmBone = GsonHelper.getAsString(bonesJson, "right_arm", layer.model.rightArmBone);
        layer.model.leftArmBone = GsonHelper.getAsString(bonesJson, "left_arm", layer.model.leftArmBone);
        layer.model.rightLegBone = GsonHelper.getAsString(bonesJson, "right_leg", layer.model.rightLegBone);
        layer.model.leftLegBone = GsonHelper.getAsString(bonesJson, "left_leg", layer.model.leftLegBone);

        return IPackRenderLayer.parseConditions(layer, json);
    }

}
