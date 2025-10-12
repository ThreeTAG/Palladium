package net.threetag.palladium.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.platform.PlatformHelper;

public class PalladiumBlockUtil {

    public static boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        return PlatformHelper.PLATFORM.getBlocks().canBurn(blockState, level, pos, facing);
    }

}
