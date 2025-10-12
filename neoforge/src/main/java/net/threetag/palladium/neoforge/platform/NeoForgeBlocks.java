package net.threetag.palladium.neoforge.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.platform.BlockService;

public class NeoForgeBlocks implements BlockService {

    @Override
    public boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        return blockState.isFlammable(level, pos, facing);
    }
}
