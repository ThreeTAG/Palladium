package net.threetag.threecore.scripts.bindings;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.scripts.accessors.BlockStateAccessor;
import net.threetag.threecore.scripts.accessors.ScriptAccessor;

public class BlockStateBuilder {

    public BlockStateAccessor create(String blockId) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockId));
        return (BlockStateAccessor) ScriptAccessor.makeAccessor(block == null ? Blocks.AIR.getDefaultState() : block.getDefaultState());
    }

}
