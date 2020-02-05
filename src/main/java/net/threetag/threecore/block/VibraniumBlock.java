package net.threetag.threecore.block;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

public class VibraniumBlock extends Block {

    public VibraniumBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
