package net.threetag.threecore.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.threetag.threecore.tileentity.FluidComposerTileEntity;

import javax.annotation.Nullable;

public class FluidComposerBlock extends MachineBlock {

    public FluidComposerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new FluidComposerTileEntity();
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        float y = (float) pos.getY() + 0.5F;
        if (!world.isRemote && entity.getBoundingBox().minY <= (double) y) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof FluidComposerTileEntity) {
                FluidStack fluidStack = ((FluidComposerTileEntity) tileEntity).outputFluidTank.getFluid();

                if (!fluidStack.isEmpty() && fluidStack.getFluid().getAttributes().getTemperature(fluidStack) >= 1000) {
                    entity.setFire(10);
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext selectionContext) {
        return MachineBlock.SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader blockReader, BlockPos pos) {
        return MachineBlock.INSIDE;
    }
}
