package net.threetag.palladium.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockService {

    boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing);

}
