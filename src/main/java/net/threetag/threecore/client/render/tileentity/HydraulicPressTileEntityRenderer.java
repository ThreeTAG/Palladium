package net.threetag.threecore.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.block.MachineBlock;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.client.renderer.entity.model.HydraulicPressPistonModel;
import net.threetag.threecore.tileentity.HydraulicPressTileEntity;
import net.threetag.threecore.util.RenderUtil;

@OnlyIn(Dist.CLIENT)
public class HydraulicPressTileEntityRenderer extends TileEntityRenderer<HydraulicPressTileEntity> {

    public static final Material TEXTURE;
    public static HydraulicPressPistonModel MODEL = new HydraulicPressPistonModel();

    static {
        TEXTURE = ModelLoaderRegistry.blockMaterial(new ResourceLocation(ThreeCore.MODID, "block/hydraulic_press_pistons"));
    }

    public HydraulicPressTileEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    @Override
    public void render(HydraulicPressTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        matrixStack.push();
        BlockState blockstate = tileEntity.hasWorld() ? tileEntity.getBlockState() : TCBlocks.HYDRAULIC_PRESS.get().getDefaultState().with(MachineBlock.FACING, Direction.SOUTH);

        float f = blockstate.get(MachineBlock.FACING).getHorizontalAngle();
        float pixel = 0.0625F;

        if ((double) Math.abs(f) > 1.0E-5D) {
            matrixStack.translate(0.5F, 0.5F, 0.5F);
            matrixStack.rotate(Vector3f.YP.rotationDegrees(f));
            matrixStack.translate(-0.5F, -0.5F, -0.5F);
        }

        matrixStack.translate(0, pixel * 10F, 0);
        float progress = blockstate.get(MachineBlock.LIT) ? MathHelper.sin((Minecraft.getInstance().player.ticksExisted + partialTicks) / 5F) / 2F + 0.5F : 0F;

        MODEL.setProgress(progress);
        IVertexBuilder vertexBuilder = TEXTURE.getBuffer(renderTypeBuffer, RenderType::getEntitySolid);
        MODEL.render(matrixStack, vertexBuilder, combinedLightIn, combinedOverlayIn, 1F, 1F, 1F, 1F);

        IVertexBuilder boxBuilder = renderTypeBuffer.getBuffer(RenderUtil.RenderTypes.HYDRAULIC_PRESS_PISTONS);
        float hue = 0.3F;
        RenderUtil.renderFilledBox(matrixStack.getLast().getMatrix(), boxBuilder, 2 * pixel, 2.5F * pixel, 7.5F * pixel, 3 * pixel - MODEL.plate1.rotationPointX * pixel, 3.5F * pixel, 8.5F * pixel, hue, hue, hue, 1, combinedLightIn);
        RenderUtil.renderFilledBox(matrixStack.getLast().getMatrix(), boxBuilder, 13 * pixel - MODEL.plate2.rotationPointX * pixel, 2.5F * pixel, 7.5F * pixel, 14 * pixel, 3.5F * pixel, 8.5F * pixel, hue, hue, hue, 1, combinedLightIn);
        IRenderTypeBuffer.Impl buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        buffer.finish(RenderUtil.RenderTypes.HYDRAULIC_PRESS_PISTONS);

        matrixStack.pop();
    }
}
