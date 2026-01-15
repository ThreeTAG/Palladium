package net.threetag.palladium.block;

import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.flag.PalladiumFeatureFlags;

public class PalladiumBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Palladium.MOD_ID);

    public static final DeferredBlock<Block> TAILORING_BENCH = BLOCKS.registerBlock("tailoring_bench", TailoringBenchBlock::new, p -> p.requiredFeatures(PalladiumFeatureFlags.TAILORING));

}
