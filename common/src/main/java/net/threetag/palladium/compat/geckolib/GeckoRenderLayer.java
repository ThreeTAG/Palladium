package net.threetag.palladium.compat.geckolib;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.client.renderer.renderlayer.*;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.function.BiFunction;

@SuppressWarnings({"unchecked", "rawtypes", "ConstantValue"})
public class GeckoRenderLayer extends AbstractPackRenderLayer implements IAnimatable, AnimationController.ModelFetcher<GeckoRenderLayer> {

    private AnimationFactory factory = GeckoLibUtil.createFactory(this, true);
    private final SkinTypedValue<DynamicTexture> texture;
    private final SkinTypedValue<ResourceLocation> modelLocation;
    private final ResourceLocation animationLocation;
    private final String animationName;
    private ResourceLocation cachedTexture;
    private ResourceLocation cachedModel;
    public final BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> renderType;
    private final GeckoRenderLayerModel model;

    public GeckoRenderLayer(SkinTypedValue<DynamicTexture> texture, SkinTypedValue<ResourceLocation> modelLocation, ResourceLocation animationLocation, String animationName, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> renderType) {
        this.texture = texture;
        this.renderType = renderType;
        this.modelLocation = modelLocation;
        this.animationLocation = animationLocation;
        this.animationName = animationName;
        this.model = new GeckoRenderLayerModel(this);
    }

    public AnimatedGeoModel<GeckoRenderLayer> getGeoModel() {
        return new AnimatedGeoModel<>() {

            @Override
            public ResourceLocation getAnimationResource(GeckoRenderLayer animatable) {
                return animatable.animationLocation;
            }

            @Override
            public ResourceLocation getModelResource(GeckoRenderLayer object) {
                return object.cachedModel;
            }

            @Override
            public ResourceLocation getTextureResource(GeckoRenderLayer object) {
                return object.cachedTexture;
            }
        };
    }

    public GeckoRenderLayerModel getModel() {
        return model;
    }

    @Override
    public void render(IRenderLayerContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<LivingEntity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var entity = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions) && entity instanceof AbstractClientPlayer player) {
            HumanoidModel entityModel = this.model;
            entityModel.setAllVisible(true);

            this.cachedTexture = this.texture.get(player).getTexture(player);
            this.cachedModel = this.modelLocation.get(player);

            if (parentModel instanceof HumanoidModel parentHumanoid) {
                parentHumanoid.copyPropertiesTo(entityModel);
            }

            parentModel.copyPropertiesTo(entityModel);
            entityModel.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
            entityModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

            // TODO apply enchant glint when item is enchanted
            if (entityModel instanceof GeckoRenderLayerModel gecko) {

                gecko.renderModel(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    @Override
    public void renderArm(IRenderLayerContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        var entity = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.firstPersonConditions) && entity instanceof AbstractClientPlayer player) {
            GeckoRenderLayerModel humanoidModel = this.model;
            this.cachedTexture = this.texture.get(player).getTexture(player);
            this.cachedModel = this.modelLocation.get(player);

            playerRenderer.getModel().copyPropertiesTo(humanoidModel);
            humanoidModel.attackTime = 0.0F;
            humanoidModel.crouching = false;
            humanoidModel.swimAmount = 0.0F;
            humanoidModel.rightArm.xRot = 0.0F;
            humanoidModel.leftArm.xRot = 0.0F;
            humanoidModel.renderArm(poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getFrameTime(), arm == HumanoidArm.RIGHT);
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        if (this.animationLocation != null) {
            data.addAnimationController(new AnimationController<>(this, "controller", 20, this::predicate));
        }
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation(this.animationName, ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void onLoad() {
        AnimationController.addModelFetcher(this);
    }

    @Override
    public void onUnload() {
        AnimationController.removeModelFetcher(this);
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
                GsonUtil.getAsResourceLocation(json, "animation", null),
                GsonHelper.getAsString(json, "animation_name", "animation.render_layer.loop"),
                renderType
        );

        var bonesJson = GsonHelper.getAsJsonObject(json, "bones", new JsonObject());
        layer.model.headBone = GsonHelper.getAsString(bonesJson, "head", layer.model.headBone);
        layer.model.bodyBone = GsonHelper.getAsString(bonesJson, "body", layer.model.bodyBone);
        layer.model.rightArmBone = GsonHelper.getAsString(bonesJson, "right_arm", layer.model.rightArmBone);
        layer.model.leftArmBone = GsonHelper.getAsString(bonesJson, "left_arm", layer.model.leftArmBone);
        layer.model.rightLegBone = GsonHelper.getAsString(bonesJson, "right_leg", layer.model.rightLegBone);
        layer.model.leftLegBone = GsonHelper.getAsString(bonesJson, "left_leg", layer.model.leftLegBone);

        return layer;
    }

    @Override
    public IAnimatableModel<GeckoRenderLayer> apply(IAnimatable animatable) {
        if (animatable instanceof GeckoRenderLayer layer) {
            return layer.getGeoModel();
        }
        return null;
    }
}
