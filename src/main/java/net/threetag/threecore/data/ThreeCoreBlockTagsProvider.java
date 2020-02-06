package net.threetag.threecore.data;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.tags.TCBlockTags;

public class ThreeCoreBlockTagsProvider extends BlockTagsProvider {

    public ThreeCoreBlockTagsProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerTags() {
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.COPPER_STORAGE_BLOCKS, TCBlocks.COPPER_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.TIN_STORAGE_BLOCKS, TCBlocks.TIN_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.LEAD_STORAGE_BLOCKS, TCBlocks.LEAD_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.SILVER_STORAGE_BLOCKS, TCBlocks.SILVER_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.PALLADIUM_STORAGE_BLOCKS, TCBlocks.PALLADIUM_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.VIBRANIUM_STORAGE_BLOCKS, TCBlocks.VIBRANIUM_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.OSMIUM_STORAGE_BLOCKS, TCBlocks.OSMIUM_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.URANIUM_STORAGE_BLOCKS, TCBlocks.URANIUM_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.TITANIUM_STORAGE_BLOCKS, TCBlocks.TITANIUM_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.IRIDIUM_STORAGE_BLOCKS, TCBlocks.IRIDIUM_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.URU_STORAGE_BLOCKS, TCBlocks.URU_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.BRONZE_STORAGE_BLOCKS, TCBlocks.BRONZE_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.INTERTIUM_STORAGE_BLOCKS, TCBlocks.INTERTIUM_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.STEEL_STORAGE_BLOCKS, TCBlocks.STEEL_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS, TCBlocks.GOLD_TITANIUM_ALLOY_BLOCK.get());
        addToBoth(TCBlockTags.STORAGE_BLOCKS, TCBlockTags.ADAMANTIUM_STORAGE_BLOCKS, TCBlocks.ADAMANTIUM_BLOCK.get());

        addToBoth(TCBlockTags.ORES, TCBlockTags.COPPER_ORES, TCBlocks.COPPER_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.TIN_ORES, TCBlocks.TIN_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.LEAD_ORES, TCBlocks.LEAD_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.SILVER_ORES, TCBlocks.SILVER_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.PALLADIUM_ORES, TCBlocks.PALLADIUM_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.VIBRANIUM_ORES, TCBlocks.VIBRANIUM_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.OSMIUM_ORES, TCBlocks.OSMIUM_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.URANIUM_ORES, TCBlocks.URANIUM_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.TITANIUM_ORES, TCBlocks.TITANIUM_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.IRIDIUM_ORES, TCBlocks.IRIDIUM_ORE.get());
        addToBoth(TCBlockTags.ORES, TCBlockTags.URU_ORES, TCBlocks.URU_ORE.get());
    }

    public void addToBoth(Tag<Block> root, Tag<Block> branch, Block block) {
        this.getBuilder(branch).add(block);
        this.getBuilder(root).add(branch);
    }

    @Override
    public String getName() {
        return "ThreeCore Block Tags";
    }

}
