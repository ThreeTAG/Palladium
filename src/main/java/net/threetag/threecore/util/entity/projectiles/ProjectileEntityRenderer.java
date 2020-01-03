package net.threetag.threecore.util.entity.projectiles;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.threetag.threecore.util.client.RenderUtil;
import net.threetag.threecore.util.modellayer.IModelLayer;
import net.threetag.threecore.util.modellayer.ModelLayerContext;
import net.threetag.threecore.util.modellayer.ModelLayerLoader;

import java.awt.*;

public class ProjectileEntityRenderer extends SpriteRenderer<ProjectileEntity> {

    public ProjectileEntityRenderer(EntityRendererManager rendererManager, ItemRenderer itemRenderer) {
        super(rendererManager, itemRenderer, 1F);
    }

    @Override
    public void doRender(ProjectileEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.getRenderedItem().isEmpty()) {
            if (entity.renderInfo.isEnergy()) {
                this.preRender(entity, x, y, z, entityYaw, partialTicks);
                GlStateManager.rotatef(90F, 1.0F, 0.0F, 0.0F);
                RenderUtil.drawGlowingLine(0.05F, 1F, Color.RED, true, true, true);
                GlStateManager.popMatrix();
            } else {
                IModelLayer layer = ModelLayerLoader.getModelLayer(entity.renderInfo.getModelLayer());
                if (layer != null) {
                    this.preRender(entity, x, y, z, entityYaw, partialTicks);
                    GlStateManager.rotatef(180.0F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);

                    layer.render(new ModelLayerContext(entity), null, 0, 0, partialTicks, 0, 0, 0, 0.0625F);
                    GlStateManager.popMatrix();
                }
            }
        } else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    private void preRender(ProjectileEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);

        Vec3d vec1 = new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ);
        Vec3d vec2 = new Vec3d(entity.posX, entity.posY, entity.posZ);
        vec1 = vec2.subtract(vec1);
        vec2 = vec2.subtract(vec2);
        vec1 = vec1.normalize();

        double x_ = vec2.x - vec1.x;
        double y_ = vec2.y - vec1.y;
        double z_ = vec2.z - vec1.z;
        double diff = MathHelper.sqrt(x_ * x_ + z_ * z_);
        float yaw = (float) (Math.atan2(z_, x_) * 180.0D / 3.141592653589793D) - 90.0F;
        float pitch = (float) -(Math.atan2(y_, diff) * 180.0D / 3.141592653589793D);
        GlStateManager.rotatef(-yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(pitch, 1.0F, 0.0F, 0.0F);
    }

}
