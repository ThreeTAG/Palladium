package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.trail.AfterImageTrailRenderer;
import net.threetag.palladium.client.renderer.trail.TrailRenderer;
import net.threetag.palladium.entity.TrailSegmentEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"rawtypes", "unchecked", "UnnecessaryLocalVariable"})
public class TrailSegmentEntityRenderer extends LivingEntityRenderer<TrailSegmentEntity<?>, EntityModel<TrailSegmentEntity<?>>> {

    public TrailSegmentEntityRenderer(EntityRendererProvider.Context context) {
        super(context, null, 0);
        this.addLayer(new HumanoidArmorLayer(this, new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidArmorModel(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public void render(TrailSegmentEntity<? extends TrailRenderer.SegmentCache> entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.renderer == null && Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity.parent) instanceof RenderLayerParent<?, ?> layerParent) {
            entity.renderer = layerParent;
            entity.model = layerParent.getModel();
            entity.texture = entity.mimicPlayer ? ((EntityRenderer) layerParent).getTextureLocation(entity.parent) : AfterImageTrailRenderer.TEXTURE;
            entity.partialTick = partialTicks;
        }

        if (entity.parent == Minecraft.getInstance().player && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
            return;
        }

        if (entity.trailRenderer != null) {
            this.model = (EntityModel) entity.model;
            TrailRenderer trailRenderer = entity.trailRenderer;
            trailRenderer.render(poseStack, buffer, packedLight, this, entity.parent, entity, partialTicks, entityYaw);
        }
    }

    public void renderModel(TrailSegmentEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        this.model.attackTime = this.getAttackAnim(entity, partialTicks);
        this.model.riding = entity.isPassenger();
        this.model.young = entity.isBaby();
        float f = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot);
        float g = Mth.rotLerp(partialTicks, entity.yHeadRotO, entity.yHeadRot);

        float h = g - f;
        float i;
        if (entity.isPassenger() && entity.getVehicle() instanceof LivingEntity livingEntity) {
            f = Mth.rotLerp(partialTicks, livingEntity.yBodyRotO, livingEntity.yBodyRot);
            h = g - f;
            i = Mth.wrapDegrees(h);
            if (i < -85.0F) {
                i = -85.0F;
            }

            if (i >= 85.0F) {
                i = 85.0F;
            }

            f = g - i;
            if (i * i > 2500.0F) {
                f += i * 0.2F;
            }

            h = g - f;
        }

        float j = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        if (isEntityUpsideDown(entity)) {
            j *= -1.0F;
            h *= -1.0F;
        }

        float k;
        if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            if (direction != null) {
                k = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float) (-direction.getStepX()) * k, 0.0F, (float) (-direction.getStepZ()) * k);
            }
        }

        i = this.getBob(entity, partialTicks);
        this.setupRotations(entity, poseStack, i, f, partialTicks);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        this.scale(entity, poseStack, partialTicks);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        k = 0.0F;
        float l = 0.0F;
        if (!entity.isPassenger() && entity.isAlive()) {
            k = entity.walkAnimation.speed(partialTicks);
            l = entity.walkAnimation.position(partialTicks);
            if (entity.isBaby()) {
                l *= 3.0F;
            }

            if (k > 1.0F) {
                k = 1.0F;
            }
        }

        if (!entity.fetchedAnimationValues) {
            entity.limbSwing = l;
            entity.limbSwingAmount = k;
            entity.ageInTicks = i;
            entity.netHeadYaw = h;
            entity.headPitch = j;
            entity.fetchedAnimationValues = true;
        }

        this.model.prepareMobModel(entity, entity.limbSwing, entity.limbSwingAmount, partialTicks);
        this.model.setupAnim(entity, entity.limbSwing, entity.limbSwingAmount, entity.ageInTicks, entity.netHeadYaw, entity.headPitch);

        Minecraft minecraft = Minecraft.getInstance();
        boolean bl = this.isBodyVisible(entity);
        boolean bl2 = !bl && !entity.isInvisibleTo(minecraft.player);
        boolean bl3 = minecraft.shouldEntityAppearGlowing(entity);
        RenderType renderType = this.getRenderType(entity, bl, bl2, bl3);
        var color = entity.trailRenderer.getColor();

        if (renderType != null) {
            VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
            int m = getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks));

            if (!entity.mimicPlayer && this.model instanceof PlayerModel playerModel) {
                playerModel.hat.visible = playerModel.jacket.visible = playerModel.rightSleeve.visible =
                        playerModel.leftSleeve.visible = playerModel.rightPants.visible = playerModel.leftPants.visible = false;
            }

            this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, m, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0F);
        }

        if (!entity.isSpectator() && entity.mimicPlayer) {
            for (RenderLayer<TrailSegmentEntity<?>, EntityModel<TrailSegmentEntity<?>>> layer : this.layers) {
                RenderLayer renderLayer = layer;
                renderLayer.render(poseStack, buffer, packedLight, entity, entity.limbSwing, entity.limbSwingAmount, partialTicks, entity.ageInTicks, entity.netHeadYaw, entity.headPitch);
            }

            for (Object s : entity.getRenderLayerSnapshots()) {
                IPackRenderLayer.Snapshot snapshot = (IPackRenderLayer.Snapshot) s;
                snapshot.applyPoses();
                poseStack.pushPose();
                snapshot.getModel().renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityTranslucent(snapshot.getTexture())), packedLight, OverlayTexture.NO_OVERLAY, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, 1.0F);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

    @Override
    public void scale(TrailSegmentEntity livingEntity, PoseStack poseStack, float partialTickTime) {
        if (livingEntity.renderer instanceof LivingEntityRenderer entityRenderer && livingEntity.parent instanceof LivingEntity living) {
            entityRenderer.scale(living, poseStack, partialTickTime);
            poseStack.scale(livingEntity.scaleWidth, livingEntity.scaleHeight, livingEntity.scaleWidth);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(TrailSegmentEntity entity) {
        return entity.texture;
    }

    @Nullable
    @Override
    public RenderType getRenderType(TrailSegmentEntity livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return super.getRenderType(livingEntity, true, true, false);
    }

    @Override
    protected boolean shouldShowName(TrailSegmentEntity entity) {
        return false;
    }
}
