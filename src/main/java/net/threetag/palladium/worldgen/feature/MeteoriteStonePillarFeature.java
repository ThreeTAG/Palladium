package net.threetag.palladium.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.threetag.palladium.block.PalladiumBlocks;

public class MeteoriteStonePillarFeature extends Feature<NoneFeatureConfiguration> {

    public MeteoriteStonePillarFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> p_159446_) {
        BlockPos blockpos = p_159446_.origin();
        WorldGenLevel worldgenlevel = p_159446_.level();
        RandomSource randomsource = p_159446_.random();
        if (worldgenlevel.isEmptyBlock(blockpos) && !worldgenlevel.isEmptyBlock(blockpos.above())) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockpos.mutable();
            BlockPos.MutableBlockPos blockpos$mutableblockpos1 = blockpos.mutable();
            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            boolean flag3 = true;

            while (worldgenlevel.isEmptyBlock(blockpos$mutableblockpos)) {
                if (worldgenlevel.isOutsideBuildHeight(blockpos$mutableblockpos)) {
                    return true;
                }

                worldgenlevel.setBlock(blockpos$mutableblockpos, PalladiumBlocks.METEORITE_STONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                flag = flag
                        && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.NORTH));
                flag1 = flag1
                        && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.SOUTH));
                flag2 = flag2
                        && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.WEST));
                flag3 = flag3
                        && this.placeHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.EAST));
                blockpos$mutableblockpos.move(Direction.DOWN);
            }

            blockpos$mutableblockpos.move(Direction.UP);
            this.placeBaseHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.NORTH));
            this.placeBaseHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.SOUTH));
            this.placeBaseHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.WEST));
            this.placeBaseHangOff(worldgenlevel, randomsource, blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos, Direction.EAST));
            blockpos$mutableblockpos.move(Direction.DOWN);
            BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();

            for (int i = -3; i < 4; i++) {
                for (int j = -3; j < 4; j++) {
                    int k = Mth.abs(i) * Mth.abs(j);
                    if (randomsource.nextInt(10) < 10 - k) {
                        blockpos$mutableblockpos2.set(blockpos$mutableblockpos.offset(i, 0, j));
                        int l = 3;

                        while (worldgenlevel.isEmptyBlock(blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos2, Direction.DOWN))) {
                            blockpos$mutableblockpos2.move(Direction.DOWN);
                            if (--l <= 0) {
                                break;
                            }
                        }

                        if (!worldgenlevel.isEmptyBlock(blockpos$mutableblockpos1.setWithOffset(blockpos$mutableblockpos2, Direction.DOWN))) {
                            worldgenlevel.setBlock(blockpos$mutableblockpos2, PalladiumBlocks.METEORITE_STONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }

            return true;
        } else {
            return false;
        }
    }

    private void placeBaseHangOff(LevelAccessor level, RandomSource random, BlockPos pos) {
        if (random.nextBoolean()) {
            level.setBlock(pos, PalladiumBlocks.METEORITE_STONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private boolean placeHangOff(LevelAccessor level, RandomSource random, BlockPos pos) {
        if (random.nextInt(10) != 0) {
            level.setBlock(pos, PalladiumBlocks.METEORITE_STONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
            return true;
        } else {
            return false;
        }
    }
}
