package net.threetag.palladium.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.threetag.palladium.block.PalladiumBlocks;

public class UndergroundMeteoriteFeature extends Feature<OreConfiguration> {

    public UndergroundMeteoriteFeature(Codec<OreConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> pContext) {
        for (OreConfiguration.TargetBlockState state : pContext.config().targetStates) {
            if (!state.target.test(pContext.level().getBlockState(pContext.origin()), pContext.random())) {
                return false;
            }
        }

        int r = pContext.config().size + 2;
        BlockPos pos = pContext.origin();
        WorldGenLevel level = pContext.level();
        RandomSource rand = pContext.random();

        for (int x = pos.getX() - r; x <= pos.getX() + r; x++) {
            for (int y = pos.getY() - r; y <= pos.getY() + r; y++) {
                for (int z = pos.getZ() - r; z <= pos.getZ() + r; z++) {
                    BlockPos p = new BlockPos(x, y, z);

                    if (p.closerThan(pos, pContext.config().size) && level.getBlockState(p).getBlock() != Blocks.BEDROCK) {
                        int i = rand.nextInt(100);
                        if (i <= 5) {
                            level.setBlock(p, PalladiumBlocks.VIBRANIUM_ORE.get().defaultBlockState(), 2);
                        } else if (i <= 10) {
                            level.setBlock(p, PalladiumBlocks.TITANIUM_ORE.get().defaultBlockState(), 2);
                        } else if (i <= 15) {
                            level.setBlock(p, Blocks.MAGMA_BLOCK.defaultBlockState(), 2);
                        } else {
                            level.setBlock(p, Blocks.BLACKSTONE.defaultBlockState(), 2);
                        }
                    }

                    if (level.isEmptyBlock(p) && PalladiumBlocks.HEART_SHAPED_HERB.get().defaultBlockState().canSurvive(level, p)) {
                        level.setBlock(p, PalladiumBlocks.HEART_SHAPED_HERB.get().defaultBlockState(), 2);
                    }
                }
            }
        }

        if (rand.nextInt(25) == 0) {
            BlockPos searchForGrass = pos.above();
            while (searchForGrass.getY() < level.getMaxBuildHeight()) {
                if (PalladiumBlocks.HEART_SHAPED_HERB.get().defaultBlockState().canSurvive(level, searchForGrass) && level.isEmptyBlock(searchForGrass)) {
                    level.setBlock(searchForGrass, PalladiumBlocks.HEART_SHAPED_HERB.get().defaultBlockState(), 2);
                    return true;
                }
                searchForGrass = searchForGrass.above();
            }
        }

        return true;
    }
}
