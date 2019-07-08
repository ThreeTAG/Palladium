package com.threetag.threecore.base.client.renderer.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.base.ThreeCoreBase;
import com.threetag.threecore.base.block.HydraulicPressBlock;
import com.threetag.threecore.base.client.renderer.model.HydraulicPressPistonModel;
import com.threetag.threecore.base.tileentity.HydraulicPressTileEntity;
import com.threetag.threecore.util.client.RenderUtil;
import com.threetag.threecore.util.math.TCMathHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class HydraulicPressTileEntityRenderer extends TileEntityRenderer<HydraulicPressTileEntity> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/block/hydraulic_press_pistons.png");
    public static HydraulicPressPistonModel MODEL = new HydraulicPressPistonModel();

    @Override
    public void render(HydraulicPressTileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translatef((float) x, (float) y + 1.0F, (float) z + 1.0F);
        GlStateManager.scalef(1.0F, -1.0F, -1.0F);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        BlockState blockstate = tileEntityIn.hasWorld() ? tileEntityIn.getBlockState() : ThreeCoreBase.HYDRAULIC_PRESS.getDefaultState().with(HydraulicPressBlock.FACING, Direction.SOUTH);
        float f = blockstate.get(ChestBlock.FACING).getHorizontalAngle();
        if ((double) Math.abs(f) > 1.0E-5D) {
            GlStateManager.translatef(0.5F, 0.5F, 0.5F);
            GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
            GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        }

        float progress = blockstate.get(HydraulicPressBlock.LIT) ? MathHelper.sin((Minecraft.getInstance().player.ticksExisted + partialTicks) / 5F) / 2F + 0.5F : 0F;
        //progress = MathHelper.clamp(TCMathHelper.interpolate(tileEntityIn.prevProgress, tileEntityIn.progress, partialTicks) / (float) tileEntityIn.progressMax, 0F, 1F);
        float pixel = 0.0625F;
        this.bindTexture(TEXTURE);
        MODEL.render(progress, pixel);

        GlStateManager.disableTexture();
        RenderUtil.renderFilledBox(2 * pixel, 2.5F * pixel, 7.5F * pixel, 3 * pixel - MODEL.plate1.rotationPointX * pixel, 3.5 * pixel, 8.5F * pixel, 0.5F, 0.5F, 0.5F, 1);
        RenderUtil.renderFilledBox(13 * pixel - MODEL.plate2.rotationPointX * pixel, 2.5F * pixel, 7.5F * pixel, 14 * pixel, 3.5 * pixel, 8.5F * pixel, 0.5F, 0.5F, 0.5F, 1);
        GlStateManager.enableTexture();

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }
}
