package net.threetag.threecore.addonpacks;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.threetag.threecore.sizechanging.SizeManager;

public class Test {

    private boolean renderShadow;
    private World world;
    private boolean debugBoundingBox;

    public <T extends Entity> EntityRenderer<? super T> getRenderer(T entityIn) {
        return null;
    }

    public <E extends Entity> void renderEntityStatic(E entityIn, double xIn, double yIn, double zIn, float rotationYawIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        EntityRenderer<? super E> entityrenderer = this.getRenderer(entityIn);

        try {
            Vec3d vec3d = entityrenderer.getRenderOffset(entityIn, partialTicks);
            double d2 = xIn + vec3d.getX();
            double d3 = yIn + vec3d.getY();
            double d0 = zIn + vec3d.getZ();
            matrixStackIn.push();
            matrixStackIn.translate(d2, d3, d0);
            SizeManager.preRenderCallback(entityIn, matrixStackIn, partialTicks);
            entityrenderer.render(entityIn, rotationYawIn, partialTicks, matrixStackIn, bufferIn, packedLightIn);
            if (entityIn.canRenderOnFire()) {
                this.renderFire(matrixStackIn, bufferIn, entityIn);
            }

            matrixStackIn.translate(-vec3d.getX(), -vec3d.getY(), -vec3d.getZ());
            if (this.renderShadow && !entityIn.isInvisible()) {
                double d1 = this.getDistanceToCamera(entityIn.getPosX(), entityIn.getPosY(), entityIn.getPosZ());
                float f = (float) ((1.0D - d1 / 256.0D) * 1);
                if (f > 0.0F) {
                    renderShadow(matrixStackIn, bufferIn, entityIn, f, partialTicks, this.world, 0F);
                }
            }

            SizeManager.postRenderCallback(entityIn, matrixStackIn, partialTicks);

            if (this.debugBoundingBox && !entityIn.isInvisible() && !Minecraft.getInstance().isReducedDebug()) {
                this.renderDebugBoundingBox(matrixStackIn, bufferIn.getBuffer(RenderType.getLines()), entityIn, partialTicks);
            }

            matrixStackIn.pop();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            entityIn.fillCrashReport(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addDetail("Assigned renderer", entityrenderer);
            crashreportcategory1.addDetail("Location", CrashReportCategory.getCoordinateInfo(xIn, yIn, zIn));
            crashreportcategory1.addDetail("Rotation", rotationYawIn);
            crashreportcategory1.addDetail("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }

    private <E extends Entity> void renderFire(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, E entityIn) {

    }

    private <E extends Entity> void renderDebugBoundingBox(MatrixStack matrixStackIn, IVertexBuilder buffer, E entityIn, float partialTicks) {

    }

    private <E extends Entity> void renderShadow(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, E entityIn, float f, float partialTicks, World world, float v) {
    }

    private double getDistanceToCamera(double posX, double posY, double posZ) {
        return 0D;
    }

}
