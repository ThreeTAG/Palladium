package net.threetag.threecore.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fluids.IFluidTank;
import net.threetag.threecore.util.RenderUtil;

import java.util.BitSet;
import java.util.List;

public abstract class FastFluidTESR<T extends TileEntity> extends TileEntityRenderer<T> {

    public FastFluidTESR(TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher);
    }

    @Override
    public void render(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        for (TankRenderInfo tankRenderInfo : getTanksToRender(te)) {
            doRender(te, partialTicks, matrixStack, renderTypeBuffer, combinedLightIn, combinedOverlayIn, tankRenderInfo);
        }
    }

    private void doRender(T te, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int combinedLightIn, int combinedOverlayIn, TankRenderInfo tankRenderInfo) {
        IFluidTank tank = tankRenderInfo.tank;
        if (tank.getFluidAmount() == 0) return;

        Fluid f = tank.getFluid().getFluid();
        TextureAtlasSprite still = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(f.getAttributes().getStillTexture());
        float u1 = still.getMinU(), v1 = still.getMinV(), u2 = still.getMaxU(), v2 = still.getMaxV();

        int color = f.getAttributes().getColor(tank.getFluid());
        float red = RenderUtil.red(color) / 255F;
        float green = RenderUtil.green(color) / 255F;
        float blue = RenderUtil.blue(color) / 255F;
        float alpha = RenderUtil.alpha(color) / 255F;

        AxisAlignedBB bounds = getRenderBounds(tank, tankRenderInfo.bounds);
        IVertexBuilder builder = renderTypeBuffer.getBuffer(RenderType.getTranslucent());

        matrixStack.push();

        if (tankRenderInfo.shouldRender(Direction.DOWN)) {
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.minY, (float) bounds.maxZ, u1, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, u1, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.minY, (float) bounds.minZ, u2, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.minY, (float) bounds.maxZ, u2, v2, red, green, blue, alpha, combinedLightIn);
        }

        if (tankRenderInfo.shouldRender(Direction.UP)) {
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.maxY, (float) bounds.maxZ, u1, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ, u2, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.minZ, u2, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.maxY, (float) bounds.minZ, u1, v1, red, green, blue, alpha, combinedLightIn);
        }

        if (tankRenderInfo.shouldRender(Direction.NORTH)) {
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, u1, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.maxY, (float) bounds.minZ, u1, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.minZ, u2, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.minY, (float) bounds.minZ, u2, v1, red, green, blue, alpha, combinedLightIn);
        }

        if (tankRenderInfo.shouldRender(Direction.SOUTH)) {
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.minY, (float) bounds.maxZ, u2, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ, u2, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.maxY, (float) bounds.maxZ, u1, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.minY, (float) bounds.maxZ, u1, v1, red, green, blue, alpha, combinedLightIn);
        }

        if (tankRenderInfo.shouldRender(Direction.WEST)) {
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.minY, (float) bounds.maxZ, u1, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.maxY, (float) bounds.maxZ, u2, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.maxY, (float) bounds.minZ, u2, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.minX, (float) bounds.minY, (float) bounds.minZ, u1, v1, red, green, blue, alpha, combinedLightIn);
        }

        if (tankRenderInfo.shouldRender(Direction.EAST)) {
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.minY, (float) bounds.minZ, u1, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.minZ, u2, v1, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.maxY, (float) bounds.maxZ, u2, v2, red, green, blue, alpha, combinedLightIn);
            this.add(builder, matrixStack, (float) bounds.maxX, (float) bounds.minY, (float) bounds.maxZ, u1, v2, red, green, blue, alpha, combinedLightIn);
        }

        matrixStack.pop();
    }

    private void add(IVertexBuilder builder, MatrixStack matrixStack, float x, float y, float z, float u, float v, float red, float green, float blue, float alpha, int light) {
        builder.pos(matrixStack.getLast().getMatrix(), x, y, z)
                .color(red, green, blue, alpha)
                .tex(u, v)
                .lightmap(light)
                .normal(1, 0, 0)
                .endVertex();
    }

    private AxisAlignedBB getRenderBounds(IFluidTank tank, AxisAlignedBB tankBounds) {
        float percent = (float) tank.getFluidAmount() / (float) tank.getCapacity();

        double tankHeight = tankBounds.maxY - tankBounds.minY;
        double y1 = tankBounds.minY, y2 = (tankBounds.minY + (tankHeight * percent));
        if (tank.getFluid().getFluid().getAttributes().getDensity() < 0) {
            double yOff = tankBounds.maxY - y2;  // lighter than air fluids move to the top of the tank
            y1 += yOff;
            y2 += yOff;
        }
        return new AxisAlignedBB(tankBounds.minX, y1, tankBounds.minZ, tankBounds.maxX, y2, tankBounds.maxZ);
    }

    static AxisAlignedBB rotateY(AxisAlignedBB in, int rot) {
        switch (rot) {
            case 90:
                return new AxisAlignedBB(1 - in.minZ, in.minY, in.minX, 1 - in.maxZ, in.maxY, in.maxX);
            case 180:
                return new AxisAlignedBB(1 - in.minX, in.minY, 1 - in.minZ, 1 - in.maxX, in.maxY, 1 - in.maxZ);
            case 270:
                return new AxisAlignedBB(in.minZ, in.minY, 1 - in.minX, in.maxZ, in.maxY, 1 - in.maxX);
            default:
                throw new IllegalArgumentException("rot must be 90, 180 or 270");
        }
    }

    public abstract List<TankRenderInfo> getTanksToRender(T te);

    public static class TankRenderInfo {
        final IFluidTank tank;
        final AxisAlignedBB bounds;
        final BitSet faces = new BitSet(6);

        public TankRenderInfo(IFluidTank tank, AxisAlignedBB bounds, Direction... renderFaces) {
            this.tank = tank;
            this.bounds = bounds;
            if (renderFaces.length == 0) {
                faces.set(0, 6, true);
            } else {
                for (Direction face : renderFaces) {
                    faces.set(face.getIndex(), true);
                }
            }
        }

        TankRenderInfo without(Direction face) {
            faces.clear(face.getIndex());
            return this;
        }

        boolean shouldRender(Direction face) {
            return faces.get(face.getIndex());
        }
    }
}
