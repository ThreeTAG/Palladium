package net.threetag.palladium.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.entity.CustomProjectile;
import net.threetag.palladium.util.RenderUtil;
import net.threetag.palladium.util.context.DataContext;

public class CustomProjectileRenderer extends EntityRenderer<CustomProjectile> {

    private final ItemRenderer itemRenderer;

    public CustomProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(CustomProjectile entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        for (CustomProjectile.Appearance appearance : entity.appearances) {
            if (appearance instanceof CustomProjectile.ItemAppearance item) {
                if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25)) {
                    poseStack.pushPose();
                    poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
                    poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                    this.itemRenderer.renderStatic(item.item, ItemTransforms.TransformType.GROUND, packedLight, OverlayTexture.NO_OVERLAY, poseStack, buffer, entity.getId());
                    poseStack.popPose();
                }
            } else if (appearance instanceof CustomProjectile.LaserAppearance laser) {
                poseStack.pushPose();
                poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
                poseStack.translate(-0.5F, entity.dimensions.height / 2F, 0);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
                var vertexConsumer = buffer.getBuffer(PalladiumRenderTypes.LASER);
                RenderUtil.drawGlowingBox(poseStack, vertexConsumer, 0.5F, laser.thickness, laser.color.getRed() / 255F, laser.color.getGreen() / 255F, laser.color.getBlue() / 255F, 1F, 15728640);
                poseStack.popPose();
            } else if (appearance instanceof CustomProjectile.RenderLayerAppearance renderLayers) {
                poseStack.pushPose();
                poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
                poseStack.translate(-0.5F, entity.dimensions.height / 2F, 0);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
                poseStack.mulPose(Vector3f.XP.rotationDegrees(270.0F));
                for (ResourceLocation id : renderLayers.renderLayers) {
                    IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(id);

                    if (layer != null) {
                        layer.render(DataContext.forEntity(entity), poseStack, buffer, null, packedLight, 0, 0, partialTicks, entity.tickCount, 0, 0);
                    }
                }
                poseStack.popPose();
            }
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CustomProjectile entity) {
        return null;
    }
}
