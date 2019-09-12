package net.threetag.threecore.util.data;

import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.util.block.ThreeCoreBlockTags;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.Tag;

public class ThreeCoreBlockTagsProvider extends BlockTagsProvider {

    public ThreeCoreBlockTagsProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerTags() {
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.COPPER_STORAGE_BLOCKS, ThreeCoreBase.COPPER_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.TIN_STORAGE_BLOCKS, ThreeCoreBase.TIN_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.LEAD_STORAGE_BLOCKS, ThreeCoreBase.LEAD_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.SILVER_STORAGE_BLOCKS, ThreeCoreBase.SILVER_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.PALLADIUM_STORAGE_BLOCKS, ThreeCoreBase.PALLADIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.VIBRANIUM_STORAGE_BLOCKS, ThreeCoreBase.VIBRANIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.OSMIUM_STORAGE_BLOCKS, ThreeCoreBase.OSMIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.URANIUM_STORAGE_BLOCKS, ThreeCoreBase.URANIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.TITANIUM_STORAGE_BLOCKS, ThreeCoreBase.TITANIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.IRIDIUM_STORAGE_BLOCKS, ThreeCoreBase.IRIDIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.URU_STORAGE_BLOCKS, ThreeCoreBase.URU_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.BRONZE_STORAGE_BLOCKS, ThreeCoreBase.BRONZE_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.INTERTIUM_STORAGE_BLOCKS, ThreeCoreBase.INTERTIUM_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.STEEL_STORAGE_BLOCKS, ThreeCoreBase.STEEL_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS, ThreeCoreBase.GOLD_TITANIUM_ALLOY_BLOCK);
        addToBoth(ThreeCoreBlockTags.STORAGE_BLOCKS, ThreeCoreBlockTags.ADAMANTIUM_STORAGE_BLOCKS, ThreeCoreBase.ADAMANTIUM_BLOCK);

        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.COPPER_ORES, ThreeCoreBase.COPPER_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.TIN_ORES, ThreeCoreBase.TIN_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.LEAD_ORES, ThreeCoreBase.LEAD_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.SILVER_ORES, ThreeCoreBase.SILVER_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.PALLADIUM_ORES, ThreeCoreBase.PALLADIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.VIBRANIUM_ORES, ThreeCoreBase.VIBRANIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.OSMIUM_ORES, ThreeCoreBase.OSMIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.URANIUM_ORES, ThreeCoreBase.URANIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.TITANIUM_ORES, ThreeCoreBase.TITANIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.IRIDIUM_ORES, ThreeCoreBase.IRIDIUM_ORE);
        addToBoth(ThreeCoreBlockTags.ORES, ThreeCoreBlockTags.URU_ORES, ThreeCoreBase.URU_ORE);
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
