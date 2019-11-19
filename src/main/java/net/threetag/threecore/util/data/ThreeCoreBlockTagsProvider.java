package net.threetag.threecore.util.data;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;
import net.threetag.threecore.base.block.TCBaseBlocks;
import net.threetag.threecore.util.block.ThreeCoreBlockTags;

public class ThreeCoreBlockTagsProvider extends BlockTagsProvider {

    public ThreeCoreBlockTagsProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerTags() {
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.COPPER_STORAGE_BLOCKS, TCBaseBlocks.COPPER_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.TIN_STORAGE_BLOCKS, TCBaseBlocks.TIN_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.LEAD_STORAGE_BLOCKS, TCBaseBlocks.LEAD_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.SILVER_STORAGE_BLOCKS, TCBaseBlocks.SILVER_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.PALLADIUM_STORAGE_BLOCKS, TCBaseBlocks.PALLADIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.VIBRANIUM_STORAGE_BLOCKS, TCBaseBlocks.VIBRANIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.OSMIUM_STORAGE_BLOCKS, TCBaseBlocks.OSMIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.URANIUM_STORAGE_BLOCKS, TCBaseBlocks.URANIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.TITANIUM_STORAGE_BLOCKS, TCBaseBlocks.TITANIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.IRIDIUM_STORAGE_BLOCKS, TCBaseBlocks.IRIDIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.URU_STORAGE_BLOCKS, TCBaseBlocks.URU_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.BRONZE_STORAGE_BLOCKS, TCBaseBlocks.BRONZE_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.INTERTIUM_STORAGE_BLOCKS, TCBaseBlocks.INTERTIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.STEEL_STORAGE_BLOCKS, TCBaseBlocks.STEEL_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS, TCBaseBlocks.GOLD_TITANIUM_ALLOY_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.ADAMANTIUM_STORAGE_BLOCKS, TCBaseBlocks.ADAMANTIUM_BLOCK);

        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.COPPER_ORES, TCBaseBlocks.COPPER_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.TIN_ORES, TCBaseBlocks.TIN_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.LEAD_ORES, TCBaseBlocks.LEAD_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.SILVER_ORES, TCBaseBlocks.SILVER_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.PALLADIUM_ORES, TCBaseBlocks.PALLADIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.VIBRANIUM_ORES, TCBaseBlocks.VIBRANIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.OSMIUM_ORES, TCBaseBlocks.OSMIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.URANIUM_ORES, TCBaseBlocks.URANIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.TITANIUM_ORES, TCBaseBlocks.TITANIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.IRIDIUM_ORES, TCBaseBlocks.IRIDIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.URU_ORES, TCBaseBlocks.URU_ORE);
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
