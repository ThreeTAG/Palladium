package com.threetag.threecore.materials.block;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

public class BlockVibranium extends Block {

    public BlockVibranium(Block.Properties properties) {
        super(properties);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
