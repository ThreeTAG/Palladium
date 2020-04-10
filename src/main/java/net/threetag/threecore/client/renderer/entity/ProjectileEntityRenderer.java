package net.threetag.threecore.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.threetag.threecore.client.renderer.entity.modellayer.IModelLayer;
import net.threetag.threecore.client.renderer.entity.modellayer.ModelLayerContext;
import net.threetag.threecore.client.renderer.entity.modellayer.ModelLayerLoader;
import net.threetag.threecore.entity.ProjectileEntity;
import net.threetag.threecore.util.RenderUtil;

public class ProjectileEntityRenderer extends SpriteRenderer<ProjectileEntity> {

    public ProjectileEntityRenderer(EntityRendererManager rendererManager, ItemRenderer itemRenderer) {
        super(rendererManager, itemRenderer, 1F, false);
    }

    @Override
    public void render(ProjectileEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        if (entity.getRenderedItem().isEmpty()) {
            if (entity.renderInfo.isEnergy()) {
                this.preRender(entity, matrixStackIn, bufferIn, entityYaw, partialTicks);
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
                IVertexBuilder vertexBuilder = bufferIn.getBuffer(RenderUtil.RenderTypes.LASER);
                RenderUtil.drawGlowingLine(matrixStackIn.getLast().getMatrix(), vertexBuilder, 1F, 0.05F, entity.renderInfo.getColor().getRed() / 255F, entity.renderInfo.getColor().getGreen() / 255F, entity.renderInfo.getColor().getBlue() / 255F, 1F, 15728640);
                matrixStackIn.pop();
            } else if (entity.renderInfo.getModelLayer() != null) {
                IModelLayer layer = ModelLayerLoader.getModelLayer(entity.renderInfo.getModelLayer());
                if (layer != null) {
                    this.preRender(entity, matrixStackIn, bufferIn, entityYaw, partialTicks);
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180.0F));
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));

                    layer.render(new ModelLayerContext(entity), matrixStackIn, bufferIn, packedLightIn, null, 0, 0, partialTicks, 0, 0, 0);
                    matrixStackIn.pop();
                }
            }
        } else {
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    private void preRender(ProjectileEntity entity, MatrixStack matrixStack, IRenderTypeBuffer buffer, float entityYaw, float partialTicks) {
        matrixStack.push();

        Vec3d vec1 = new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
        Vec3d vec2 = new Vec3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());
        vec1 = vec2.subtract(vec1);
        vec2 = vec2.subtract(vec2);
        vec1 = vec1.normalize();

        double x_ = vec2.x - vec1.x;
        double y_ = vec2.y - vec1.y;
        double z_ = vec2.z - vec1.z;
        double diff = MathHelper.sqrt(x_ * x_ + z_ * z_);
        float yaw = (float) (Math.atan2(z_, x_) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(y_, diff) * 180.0D / 3.141592653589793D);
        matrixStack.rotate(Vector3f.YP.rotation(-yaw));
        matrixStack.rotate(Vector3f.XP.rotation(pitch));
    }

}
