package net.threetag.threecore.client.render.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.animation.TileEntityRendererFast;
import net.minecraftforge.fluids.IFluidTank;
import net.threetag.threecore.util.RenderUtil;

import java.util.BitSet;
import java.util.List;

public abstract class FastFluidTESR<T extends TileEntity> extends TileEntityRendererFast<T> {

    @Override
    public void renderTileEntityFast(T te, double x, double y, double z, float partialTicks, int destroyStage, BufferBuilder buffer) {
        for (TankRenderInfo tankRenderInfo : getTanksToRender(te)) {
            doRender(te, x, y, z, buffer, tankRenderInfo);
        }
    }

    private void doRender(T te, double x, double y, double z, BufferBuilder buffer, TankRenderInfo tankRenderInfo) {
        IFluidTank tank = tankRenderInfo.tank;
        if (tank.getFluidAmount() == 0) return;

        Fluid f = tank.getFluid().getFluid();
        TextureAtlasSprite still = Minecraft.getInstance().getTextureMap().getAtlasSprite(f.getAttributes().getStillTexture().toString());
        float u1 = still.getMinU(), v1 = still.getMinV(), u2 = still.getMaxU(), v2 = still.getMaxV();

        int color = f.getAttributes().getColor(tank.getFluid());
        float red = RenderUtil.red(color) / 255F;
        float green = RenderUtil.green(color) / 255F;
        float blue = RenderUtil.blue(color) / 255F;
        float alpha = RenderUtil.alpha(color) / 255F;

        buffer.setTranslation(x, y, z);

        AxisAlignedBB bounds = getRenderBounds(tank, tankRenderInfo.bounds);

        if (tankRenderInfo.shouldRender(Direction.DOWN)) {
            int downCombined = getWorld().getCombinedLight(te.getPos().down(), 0);
            int downLMa = downCombined >> 16 & 65535;
            int downLMb = downCombined & 65535;
            buffer.pos(bounds.minX, bounds.minY, bounds.maxZ).color(red, green, blue, alpha).tex(u1, v2).lightmap(downLMa, downLMb).endVertex();
            buffer.pos(bounds.minX, bounds.minY, bounds.minZ).color(red, green, blue, alpha).tex(u1, v1).lightmap(downLMa, downLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.minY, bounds.minZ).color(red, green, blue, alpha).tex(u2, v1).lightmap(downLMa, downLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.minY, bounds.maxZ).color(red, green, blue, alpha).tex(u2, v2).lightmap(downLMa, downLMb).endVertex();
        }

        if (tankRenderInfo.shouldRender(Direction.UP)) {
            int upCombined = getWorld().getCombinedLight(te.getPos().up(), 0);
            int upLMa = upCombined >> 16 & 65535;
            int upLMb = upCombined & 65535;
            buffer.pos(bounds.minX, bounds.maxY, bounds.maxZ).color(red, green, blue, alpha).tex(u1, v2).lightmap(upLMa, upLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.maxY, bounds.maxZ).color(red, green, blue, alpha).tex(u2, v2).lightmap(upLMa, upLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.maxY, bounds.minZ).color(red, green, blue, alpha).tex(u2, v1).lightmap(upLMa, upLMb).endVertex();
            buffer.pos(bounds.minX, bounds.maxY, bounds.minZ).color(red, green, blue, alpha).tex(u1, v1).lightmap(upLMa, upLMb).endVertex();
        }

        if (tankRenderInfo.shouldRender(Direction.NORTH)) {
            int northCombined = getWorld().getCombinedLight(te.getPos().north(), 0);
            int northLMa = northCombined >> 16 & 65535;
            int northLMb = northCombined & 65535;
            buffer.pos(bounds.minX, bounds.minY, bounds.minZ).color(red, green, blue, alpha).tex(u1, v1).lightmap(northLMa, northLMb).endVertex();
            buffer.pos(bounds.minX, bounds.maxY, bounds.minZ).color(red, green, blue, alpha).tex(u1, v2).lightmap(northLMa, northLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.maxY, bounds.minZ).color(red, green, blue, alpha).tex(u2, v2).lightmap(northLMa, northLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.minY, bounds.minZ).color(red, green, blue, alpha).tex(u2, v1).lightmap(northLMa, northLMb).endVertex();
        }

        if (tankRenderInfo.shouldRender(Direction.SOUTH)) {
            int southCombined = getWorld().getCombinedLight(te.getPos().south(), 0);
            int southLMa = southCombined >> 16 & 65535;
            int southLMb = southCombined & 65535;
            buffer.pos(bounds.maxX, bounds.minY, bounds.maxZ).color(red, green, blue, alpha).tex(u2, v1).lightmap(southLMa, southLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.maxY, bounds.maxZ).color(red, green, blue, alpha).tex(u2, v2).lightmap(southLMa, southLMb).endVertex();
            buffer.pos(bounds.minX, bounds.maxY, bounds.maxZ).color(red, green, blue, alpha).tex(u1, v2).lightmap(southLMa, southLMb).endVertex();
            buffer.pos(bounds.minX, bounds.minY, bounds.maxZ).color(red, green, blue, alpha).tex(u1, v1).lightmap(southLMa, southLMb).endVertex();
        }

        if (tankRenderInfo.shouldRender(Direction.WEST)) {
            int westCombined = getWorld().getCombinedLight(te.getPos().west(), 0);
            int westLMa = westCombined >> 16 & 65535;
            int westLMb = westCombined & 65535;
            buffer.pos(bounds.minX, bounds.minY, bounds.maxZ).color(red, green, blue, alpha).tex(u1, v2).lightmap(westLMa, westLMb).endVertex();
            buffer.pos(bounds.minX, bounds.maxY, bounds.maxZ).color(red, green, blue, alpha).tex(u2, v2).lightmap(westLMa, westLMb).endVertex();
            buffer.pos(bounds.minX, bounds.maxY, bounds.minZ).color(red, green, blue, alpha).tex(u2, v1).lightmap(westLMa, westLMb).endVertex();
            buffer.pos(bounds.minX, bounds.minY, bounds.minZ).color(red, green, blue, alpha).tex(u1, v1).lightmap(westLMa, westLMb).endVertex();
        }

        if (tankRenderInfo.shouldRender(Direction.EAST)) {
            int eastCombined = getWorld().getCombinedLight(te.getPos().east(), 0);
            int eastLMa = eastCombined >> 16 & 65535;
            int eastLMb = eastCombined & 65535;
            buffer.pos(bounds.maxX, bounds.minY, bounds.minZ).color(red, green, blue, alpha).tex(u1, v1).lightmap(eastLMa, eastLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.maxY, bounds.minZ).color(red, green, blue, alpha).tex(u2, v1).lightmap(eastLMa, eastLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.maxY, bounds.maxZ).color(red, green, blue, alpha).tex(u2, v2).lightmap(eastLMa, eastLMb).endVertex();
            buffer.pos(bounds.maxX, bounds.minY, bounds.maxZ).color(red, green, blue, alpha).tex(u1, v2).lightmap(eastLMa, eastLMb).endVertex();
        }
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
