package net.threetag.threecore.client.renderer.tileentity;

import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.threetag.threecore.tileentity.FluidComposerTileEntity;

import java.util.Arrays;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class FluidComposerTileEntityRenderer extends FastFluidTESR<FluidComposerTileEntity> {

    @Override
    public List<FastFluidTESR.TankRenderInfo> getTanksToRender(FluidComposerTileEntity te) {
        return Arrays.asList(new TankRenderInfo(te.outputFluidTank,
                new AxisAlignedBB(2F / 16F, 8F / 16F, 2F / 16F, 14F / 16F, 15F / 16F, 14F / 16F),
                Direction.UP));
    }
}
