package net.threetag.palladium.fabric.platform;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.threetag.palladium.platform.BlockService;

public class FabricBlocks implements BlockService {

    @Override
    public boolean canBurn(BlockState blockState, BlockGetter level, BlockPos pos, Direction facing) {
        return FlammableBlockRegistry.getDefaultInstance().get(blockState.getBlock()).getBurnChance() > 0;
    }
}
