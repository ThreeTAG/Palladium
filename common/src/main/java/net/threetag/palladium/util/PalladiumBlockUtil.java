package net.threetag.palladium.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Supplier;

public class PalladiumBlockUtil {

    @ExpectPlatform
    public static Block createFlowerPotBlock(Supplier<FlowerPotBlock> emptyPot, Supplier<? extends Block> supplier, BlockBehaviour.Properties properties) {
        throw new AssertionError();
    }

}
