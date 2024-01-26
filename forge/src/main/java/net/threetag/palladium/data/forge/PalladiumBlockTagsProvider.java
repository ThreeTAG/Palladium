package net.threetag.palladium.data.forge;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.tags.PalladiumBlockTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.threetag.palladium.block.PalladiumBlocks.*;

@SuppressWarnings("unchecked")
public class PalladiumBlockTagsProvider extends IntrinsicHolderTagsProvider<Block> {

    public PalladiumBlockTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, Registries.BLOCK, completableFuture, block -> block.builtInRegistryHolder().key(), Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(PalladiumBlockTags.PREVENTS_INTANGIBILITY).add(Blocks.BEDROCK);

        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(LEAD_BLOCK.get(), VIBRANIUM_BLOCK.get());

        // Ore Blocks
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.ORES_LEAD, PalladiumBlockTags.Fabric.ORES_LEAD, LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get());
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.ORES_TITANIUM, PalladiumBlockTags.Fabric.ORES_TITANIUM, TITANIUM_ORE.get());
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.ORES_VIBRANIUM, PalladiumBlockTags.Fabric.ORES_VIBRANIUM, VIBRANIUM_ORE.get());
        this.multiLoaderTagMetalTags(Tags.Blocks.ORES, PalladiumBlockTags.Forge.ORES_LEAD, PalladiumBlockTags.Fabric.ORES, PalladiumBlockTags.Fabric.ORES_LEAD);
        this.multiLoaderTagMetalTags(Tags.Blocks.ORES, PalladiumBlockTags.Forge.ORES_TITANIUM, PalladiumBlockTags.Fabric.ORES, PalladiumBlockTags.Fabric.ORES_TITANIUM);
        this.multiLoaderTagMetalTags(Tags.Blocks.ORES, PalladiumBlockTags.Forge.ORES_VIBRANIUM, PalladiumBlockTags.Fabric.ORES, PalladiumBlockTags.Fabric.ORES_VIBRANIUM);

        // Raw Ore Blocks
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.RAW_LEAD_BLOCKS, PalladiumBlockTags.Fabric.RAW_LEAD_BLOCKS, PalladiumBlocks.RAW_LEAD_BLOCK.get());
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.RAW_TITANIUM_BLOCKS, PalladiumBlockTags.Fabric.RAW_TITANIUM_BLOCKS, PalladiumBlocks.RAW_TITANIUM_BLOCK.get());
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.RAW_VIBRANIUM_BLOCKS, PalladiumBlockTags.Fabric.RAW_VIBRANIUM_BLOCKS, PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(PalladiumBlockTags.Forge.RAW_LEAD_BLOCKS, PalladiumBlockTags.Forge.RAW_TITANIUM_BLOCKS, PalladiumBlockTags.Forge.RAW_VIBRANIUM_BLOCKS);

        // Storage Blocks
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.STORAGE_BLOCKS_LEAD, PalladiumBlockTags.Fabric.STORAGE_BLOCKS_LEAD, PalladiumBlocks.LEAD_BLOCK.get());
        this.multiLoaderTagMetalItems(PalladiumBlockTags.Forge.STORAGE_BLOCKS_VIBRANIUM, PalladiumBlockTags.Fabric.STORAGE_BLOCKS_VIBRANIUM, PalladiumBlocks.VIBRANIUM_BLOCK.get());
        this.tag(Tags.Blocks.STORAGE_BLOCKS).addTags(PalladiumBlockTags.Forge.STORAGE_BLOCKS_LEAD, PalladiumBlockTags.Forge.STORAGE_BLOCKS_VIBRANIUM);

        // Harvest Tools
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get(), TITANIUM_ORE.get(), VIBRANIUM_ORE.get(), LEAD_BLOCK.get(), VIBRANIUM_BLOCK.get(), RAW_LEAD_BLOCK.get(), RAW_TITANIUM_BLOCK.get(), RAW_VIBRANIUM_BLOCK.get(), REDSTONE_FLUX_CRYSTAL_GEODE.get(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(LEAD_ORE.get(), LEAD_BLOCK.get(), RAW_LEAD_BLOCK.get(), REDSTONE_FLUX_CRYSTAL_GEODE.get(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get());
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(TITANIUM_ORE.get(), RAW_TITANIUM_BLOCK.get());
        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(VIBRANIUM_ORE.get(), VIBRANIUM_BLOCK.get(), RAW_VIBRANIUM_BLOCK.get());
    }

    public void multiLoaderTagMetalItems(TagKey<Block> forgeTag, TagKey<Block> fabricTag, Block... blocks) {
        for (Block block : blocks) {
            this.tag(forgeTag).add(block);
            this.tag(fabricTag).add(block);
        }
    }

    public void multiLoaderTagMetalTags(TagKey<Block> rootForge, TagKey<Block> ownForge, TagKey<Block> rootFabric, TagKey<Block> ownFabric) {
        this.tag(rootForge).addTag(ownForge);
        this.tag(rootFabric).addTag(ownFabric);
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
