package net.threetag.palladium.data.forge;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.tags.PalladiumBlockTags;
import org.jetbrains.annotations.Nullable;

import static net.threetag.palladium.block.PalladiumBlocks.*;

public class PalladiumBlockTagsProvider extends BlockTagsProvider {

    public PalladiumBlockTagsProvider(DataGenerator arg, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, Palladium.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(PalladiumBlockTags.PREVENTS_INTANGIBILITY).add(Blocks.BEDROCK);

        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(LEAD_BLOCK.get(), VIBRANIUM_BLOCK.get());

        this.multiLoaderTagMetal(PalladiumBlockTags.ORES, PalladiumBlockTags.Fabric.ORES, PalladiumBlockTags.ORES_LEAD, PalladiumBlockTags.Fabric.ORES_LEAD, LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get());
        this.multiLoaderTagMetal(PalladiumBlockTags.ORES, PalladiumBlockTags.Fabric.ORES, PalladiumBlockTags.ORES_TITANIUM, PalladiumBlockTags.Fabric.ORES_TITANIUM, TITANIUM_ORE.get());
        this.multiLoaderTagMetal(PalladiumBlockTags.ORES, PalladiumBlockTags.Fabric.ORES, PalladiumBlockTags.ORES_VIBRANIUM, PalladiumBlockTags.Fabric.ORES_VIBRANIUM, VIBRANIUM_ORE.get());

        this.multiLoaderTagMetal(PalladiumBlockTags.STORAGE_BLOCKS, PalladiumBlockTags.Fabric.STORAGE_BLOCKS, PalladiumBlockTags.STORAGE_BLOCKS_LEAD, PalladiumBlockTags.Fabric.STORAGE_BLOCKS_LEAD, LEAD_BLOCK.get());
        this.multiLoaderTagMetal(PalladiumBlockTags.STORAGE_BLOCKS, PalladiumBlockTags.Fabric.STORAGE_BLOCKS, PalladiumBlockTags.STORAGE_BLOCKS_VIBRANIUM, PalladiumBlockTags.Fabric.STORAGE_BLOCKS_VIBRANIUM, VIBRANIUM_BLOCK.get());

        // Harvest Tools
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get(), TITANIUM_ORE.get(), VIBRANIUM_ORE.get(), LEAD_BLOCK.get(), VIBRANIUM_BLOCK.get(), RAW_LEAD_BLOCK.get(), RAW_TITANIUM_BLOCK.get(), RAW_VIBRANIUM_BLOCK.get(), REDSTONE_FLUX_CRYSTAL_GEODE.get(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(LEAD_ORE.get(), LEAD_BLOCK.get(), RAW_LEAD_BLOCK.get(), REDSTONE_FLUX_CRYSTAL_GEODE.get(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get());
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(TITANIUM_ORE.get(), RAW_TITANIUM_BLOCK.get());
        this.tag(Tags.Blocks.NEEDS_NETHERITE_TOOL).add(VIBRANIUM_ORE.get(), VIBRANIUM_BLOCK.get(), RAW_VIBRANIUM_BLOCK.get());
    }

    public void multiLoaderTagMetal(TagKey<Block> rootForge, TagKey<Block> rootFabric, TagKey<Block> forgeBranch, TagKey<Block> fabricBranch, Block... blocks) {
        for (Block block : blocks) {
            this.tag(fabricBranch).add(block);
        }
        this.tag(forgeBranch).addTag(fabricBranch);
        this.tag(rootFabric).addTag(fabricBranch);
        this.tag(rootForge).addTag(forgeBranch).addTag(rootFabric);
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
