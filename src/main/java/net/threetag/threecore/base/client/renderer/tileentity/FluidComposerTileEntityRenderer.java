package net.threetag.threecore.base.client.renderer.tileentity;

import net.minecraft.block.CauldronBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.base.tileentity.FluidComposerTileEntity;
import net.threetag.threecore.util.client.RenderUtil;
import net.threetag.threecore.util.client.tileentity.FastFluidTESR;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FluidComposerTileEntityRenderer extends FastFluidTESR<FluidComposerTileEntity> {

    @Override
    public List<FastFluidTESR.TankRenderInfo> getTanksToRender(FluidComposerTileEntity te) {
        float tankPercentage = (float) te.outputFluidTank.getFluidAmount() / (float) te.outputFluidTank.getCapacity();
        return Arrays.asList(new TankRenderInfo(te.outputFluidTank,
                new AxisAlignedBB(2F / 16F, 8F / 16F, 2F / 16F, 14F / 16F, (8F + (7F * tankPercentage)) / 16F, 14F / 16F),
                Direction.UP));
    }
}
