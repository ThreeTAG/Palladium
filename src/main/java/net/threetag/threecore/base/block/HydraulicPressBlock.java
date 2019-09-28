package net.threetag.threecore.base.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.threetag.threecore.base.tileentity.HydraulicPressTileEntity;

import javax.annotation.Nullable;

public class HydraulicPressBlock extends MachineBlock {

    public HydraulicPressBlock(Block.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new HydraulicPressTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext selectionContext) {
        return MachineBlock.SHAPE;
    }

    @Override
    public VoxelShape getRaytraceShape(BlockState state, IBlockReader blockReader, BlockPos pos) {
        return MachineBlock.INSIDE;
    }

}
