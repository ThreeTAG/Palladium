package net.threetag.palladium.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.block.PalladiumBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(OreFeature.class)
public class OreFeatureMixin {

    @ModifyArgs(method = "doPlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;"))
    private void injected(Args args) {
        int blockStateIndex = -1;
        for (int i = 0; i < args.size(); i++) {
            if (args.get(i) instanceof BlockState) {
                blockStateIndex = i;
                break;
            }
        }

        if (blockStateIndex != -1) {
            BlockState state = args.get(blockStateIndex);
            if (PalladiumConfig.Server.REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION.get()) {
                if (state.is(Blocks.REDSTONE_ORE)) {
                    RandomSource random = RandomSource.create();
                    if (random.nextInt(20) == 0) {
                        args.set(blockStateIndex, PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE.get().defaultBlockState());
                    }
                } else if (state.is(Blocks.DEEPSLATE_REDSTONE_ORE)) {
                    RandomSource random = RandomSource.create();
                    if (random.nextInt(15) == 0) {
                        args.set(blockStateIndex, PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get().defaultBlockState());
                    }
                }
            }
        }
    }

}
