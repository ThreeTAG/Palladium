package net.threetag.palladium.compat.geckolib.renderlayer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.client.renderer.renderlayer.*;
import net.threetag.palladium.compat.geckolib.playeranimator.ParsedAnimationController;
import net.threetag.palladium.entity.PalladiumLivingEntityExtension;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.molang.MolangParser;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;
import software.bernie.geckolib3.util.MolangUtils;

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

    public AnimatedGeoModel<GeckoLayerState> getGeoModel() {
        return new AnimatedGeoModel<>() {

            @Override
            public ResourceLocation getAnimationResource(GeckoLayerState animatable) {
                return animatable.layer.animationLocation;
            }

            @Override
            public ResourceLocation getModelResource(GeckoLayerState object) {
                return object.layer.cachedModel;
            }

            @Override
            public ResourceLocation getTextureResource(GeckoLayerState object) {
                return object.layer.cachedTexture;
            }

            @Override
            public void setMolangQueries(IAnimatable animatable, double seekTime) {
                super.setMolangQueries(animatable, seekTime);
                MolangParser parser = GeckoLibCache.getInstance().parser;
                Minecraft mc = Minecraft.getInstance();
                var entity = model.entityLiving;

                if (entity != null) {
                    parser.setValue("query.distance_from_camera", () -> mc.gameRenderer.getMainCamera().getPosition().distanceTo(entity.position()));
                    parser.setValue("query.is_on_ground", () -> MolangUtils.booleanToFloat(entity.isOnGround()));
                    parser.setValue("query.is_in_water", () -> MolangUtils.booleanToFloat(entity.isInWater()));
                    parser.setValue("query.is_in_water_or_rain", () -> MolangUtils.booleanToFloat(entity.isInWaterRainOrBubble()));

                    parser.setValue("query.health", entity::getHealth);
                    parser.setValue("query.max_health", entity::getMaxHealth);
                    parser.setValue("query.is_on_fire", () -> MolangUtils.booleanToFloat(entity.isOnFire()));
                    parser.setValue("query.ground_speed", () -> {
                        Vec3 velocity = entity.getDeltaMovement();

                        return Mth.sqrt((float) ((velocity.x * velocity.x) + (velocity.z * velocity.z)));
                    });
                    parser.setValue("query.yaw_speed", () -> entity.getViewYRot((float) seekTime - entity.getViewYRot((float) seekTime - 0.1f)));
                }
            }
        };
    }

    @Override
    public void render(IRenderLayerContext context, PoseStack poseStack, MultiBufferSource bufferSource, EntityModel<Entity> parentModel, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var entity = context.getEntity();

        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.thirdPersonConditions) && entity instanceof LivingEntity living) {
            this.model.setRenderedEntity(living);
            HumanoidModel entityModel = this.model;
            entityModel.setAllVisible(true);

            this.cachedTexture = this.texture.get(living).getTexture(living);
            this.cachedModel = this.modelLocation.get(living);

            parentModel.copyPropertiesTo(entityModel);

            if (parentModel instanceof HumanoidModel parentHumanoid) {
                IPackRenderLayer.copyModelProperties(entity, parentHumanoid, entityModel);
            } else if (entity instanceof LivingEntity livingEntity) {
                entityModel.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTicks);
                entityModel.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            }

            if (entityModel instanceof GeckoRenderLayerModel gecko) {
                gecko.renderModel(context, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);
            }
        }
    }

    @Override
    public void renderArm(IRenderLayerContext context, HumanoidArm arm, PlayerRenderer playerRenderer, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        var entity = context.getEntity();
        if (IPackRenderLayer.conditionsFulfilled(entity, this.conditions, this.firstPersonConditions) && entity instanceof AbstractClientPlayer living) {
            this.model.setRenderedEntity(living);
            GeckoRenderLayerModel humanoidModel = this.model;
            this.cachedTexture = this.texture.get(living).getTexture(living);
            this.cachedModel = this.modelLocation.get(living);

            playerRenderer.getModel().copyPropertiesTo(humanoidModel);
            humanoidModel.attackTime = 0.0F;
            humanoidModel.crouching = false;
            humanoidModel.swimAmount = 0.0F;
            humanoidModel.rightArm.xRot = 0.0F;
            humanoidModel.leftArm.xRot = 0.0F;
            humanoidModel.renderArm(context, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, Minecraft.getInstance().getFrameTime(), arm == HumanoidArm.RIGHT);
        }
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
                ParsedAnimationController.getAsList(json, "animation_controllers"),
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
