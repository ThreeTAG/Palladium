package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.tag.PalladiumBlockTags;

import java.util.concurrent.CompletableFuture;

public class PalladiumBlockTagProvider extends BlockTagsProvider {

    public PalladiumBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(PalladiumBlocks.TAILORING_BENCH.get());
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                PalladiumBlocks.METEORITE_STONE.get(),
                PalladiumBlocks.METEORITE_BRICKS.get(),
                PalladiumBlocks.METEORITE_VIBRANIUM_VEIN.get(),
                PalladiumBlocks.METEORITE_COAL_ORE.get(),
                PalladiumBlocks.METEORITE_IRON_ORE.get(),
                PalladiumBlocks.METEORITE_COPPER_ORE.get(),
                PalladiumBlocks.METEORITE_GOLD_ORE.get(),
                PalladiumBlocks.METEORITE_REDSTONE_ORE.get(),
                PalladiumBlocks.METEORITE_EMERALD_ORE.get(),
                PalladiumBlocks.METEORITE_LAPIS_ORE.get(),
                PalladiumBlocks.METEORITE_DIAMOND_ORE.get(),
                PalladiumBlocks.METEORITE_VIBRANIUM_ORE.get(),
                PalladiumBlocks.VIBRANIUM_BLOCK.get(),
                PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get()
        );
        this.tag(BlockTags.NEEDS_STONE_TOOL).add(
                PalladiumBlocks.METEORITE_IRON_ORE.get(),
                PalladiumBlocks.METEORITE_COPPER_ORE.get(),
                PalladiumBlocks.METEORITE_LAPIS_ORE.get()
        );
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(
                PalladiumBlocks.METEORITE_STONE.get(),
                PalladiumBlocks.METEORITE_BRICKS.get(),
                PalladiumBlocks.METEORITE_GOLD_ORE.get(),
                PalladiumBlocks.METEORITE_REDSTONE_ORE.get(),
                PalladiumBlocks.METEORITE_EMERALD_ORE.get(),
                PalladiumBlocks.METEORITE_DIAMOND_ORE.get()
        );
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(PalladiumBlocks.METEORITE_VIBRANIUM_ORE.get(),PalladiumBlocks.METEORITE_VIBRANIUM_VEIN.get(), PalladiumBlocks.VIBRANIUM_BLOCK.get(), PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
        this.tag(BlockTags.MOSS_REPLACEABLE).add(PalladiumBlocks.METEORITE_STONE.get());
        this.tag(PalladiumBlockTags.PREVENTS_INTANGIBILITY).add(Blocks.BEDROCK);

        this.tag(PalladiumBlockTags.VIBRANIUM_STORAGE_BLOCKS).add(PalladiumBlocks.VIBRANIUM_BLOCK.get());
        this.tag(PalladiumBlockTags.RAW_VIBRANIUM_STORAGE_BLOCKS).add(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
        this.tag(BlockTags.COAL_ORES).add(PalladiumBlocks.METEORITE_COAL_ORE.get());
        this.tag(BlockTags.IRON_ORES).add(PalladiumBlocks.METEORITE_IRON_ORE.get());
        this.tag(BlockTags.COPPER_ORES).add(PalladiumBlocks.METEORITE_COPPER_ORE.get());
        this.tag(BlockTags.GOLD_ORES).add(PalladiumBlocks.METEORITE_GOLD_ORE.get());
        this.tag(BlockTags.REDSTONE_ORES).add(PalladiumBlocks.METEORITE_REDSTONE_ORE.get());
        this.tag(BlockTags.EMERALD_ORES).add(PalladiumBlocks.METEORITE_EMERALD_ORE.get());
        this.tag(BlockTags.LAPIS_ORES).add(PalladiumBlocks.METEORITE_LAPIS_ORE.get());
        this.tag(BlockTags.DIAMOND_ORES).add(PalladiumBlocks.METEORITE_DIAMOND_ORE.get());
        this.tag(PalladiumBlockTags.VIBRANIUM_ORES).add(PalladiumBlocks.METEORITE_VIBRANIUM_ORE.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(PalladiumBlockTags.VIBRANIUM_STORAGE_BLOCKS, PalladiumBlockTags.RAW_VIBRANIUM_STORAGE_BLOCKS);
        this.tag(Tags.Blocks.ORES).addTags(PalladiumBlockTags.VIBRANIUM_ORES);
    }
}
