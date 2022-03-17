package net.threetag.palladium.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

import java.util.Random;

public class FluxCrystalGeodeBlock extends Block {

    private static final Direction[] DIRECTIONS = Direction.values();

    public FluxCrystalGeodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (random.nextInt(5) == 0) {
            Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
            BlockPos blockPos = pos.relative(direction);
            BlockState blockState = level.getBlockState(blockPos);
            Block block = null;
            if (canClusterGrowAtState(blockState)) {
                block = PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get();
            } else if (blockState.is(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get()) && blockState.getValue(AmethystClusterBlock.FACING) == direction) {
                block = PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get();
            } else if (blockState.is(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get()) && blockState.getValue(AmethystClusterBlock.FACING) == direction) {
                block = PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get();
            } else if (blockState.is(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get()) && blockState.getValue(AmethystClusterBlock.FACING) == direction) {
                block = PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER.get();
            }

            if (block != null) {
                BlockState blockState2 = block.defaultBlockState().setValue(AmethystClusterBlock.FACING, direction).setValue(AmethystClusterBlock.WATERLOGGED, blockState.getFluidState().getType() == Fluids.WATER);
                level.setBlockAndUpdate(blockPos, blockState2);
            }

        }
    }

    public static boolean canClusterGrowAtState(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) && state.getFluidState().getAmount() == 8;
    }

}
