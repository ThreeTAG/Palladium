package net.threetag.palladium.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class PalladiumBlockUtil {

    @ExpectPlatform
    public static Block createFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> supplier, BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        throw new AssertionError();
    }

}
