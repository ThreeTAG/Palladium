package net.threetag.palladium.util.fabric;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class PalladiumBlockUtilImpl {

    public static Block createFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> supplier, BlockBehaviour.Properties properties) {
        return new FlowerPotBlock(supplier.get(), properties);
    }

    public static boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        return FlammableBlockRegistry.getDefaultInstance().get(blockState.getBlock()).getBurnChance() > 0;
    }

}
