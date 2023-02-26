package net.threetag.palladium.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.threetag.palladium.PalladiumConfig;
import net.threetag.palladium.block.PalladiumBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(OreFeature.class)
public class OreFeatureMixin {

    @ModifyArg(method = "doPlace", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;setBlockState(IIILnet/minecraft/world/level/block/state/BlockState;Z)Lnet/minecraft/world/level/block/state/BlockState;"), index = 3)
    private BlockState injected(BlockState state) {
        if (!PalladiumConfig.Server.REDSTONE_FLUX_CRYSTAL_GEODE_GENERATION.get()) {
            return state;
        } else if (state.is(Blocks.REDSTONE_ORE)) {
            RandomSource random = RandomSource.create();
            return random.nextInt(20) == 0 ? PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE.get().defaultBlockState() : state;
        } else if (state.is(Blocks.DEEPSLATE_REDSTONE_ORE)) {
            RandomSource random = RandomSource.create();
            return random.nextInt(15) == 0 ? PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get().defaultBlockState() : state;
        } else {
            return state;
        }
    }

}
