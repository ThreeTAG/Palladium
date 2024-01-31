package net.threetag.palladium.util.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class PalladiumBlockUtilImpl {

    public static Block createFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> supplier, BlockBehaviour.Properties properties) {
        return new FlowerPotBlock(emptyPot, supplier, properties);
    }

    public static boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        return blockState.isFlammable(level, pos, facing);
    }

}
