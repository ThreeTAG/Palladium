package net.threetag.threecore.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.item.TCItems;
import net.threetag.threecore.tags.TCItemTags;

public class ThreeCoreItemTagsProvider extends ItemTagsProvider {

    public ThreeCoreItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider provider) {
        super(dataGenerator, provider);
    }

    @Override
    protected void registerTags() {
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.COPPER_STORAGE_BLOCKS, TCBlocks.COPPER_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.TIN_STORAGE_BLOCKS, TCBlocks.TIN_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.LEAD_STORAGE_BLOCKS, TCBlocks.LEAD_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.SILVER_STORAGE_BLOCKS, TCBlocks.SILVER_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.PALLADIUM_STORAGE_BLOCKS, TCBlocks.PALLADIUM_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.VIBRANIUM_STORAGE_BLOCKS, TCBlocks.VIBRANIUM_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.OSMIUM_STORAGE_BLOCKS, TCBlocks.OSMIUM_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.URANIUM_STORAGE_BLOCKS, TCBlocks.URANIUM_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.TITANIUM_STORAGE_BLOCKS, TCBlocks.TITANIUM_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.IRIDIUM_STORAGE_BLOCKS, TCBlocks.IRIDIUM_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.URU_STORAGE_BLOCKS, TCBlocks.URU_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.BRONZE_STORAGE_BLOCKS, TCBlocks.BRONZE_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.INTERTIUM_STORAGE_BLOCKS, TCBlocks.INTERTIUM_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.STEEL_STORAGE_BLOCKS, TCBlocks.STEEL_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS, TCBlocks.GOLD_TITANIUM_ALLOY_BLOCK.get());
        addToBoth(TCItemTags.STORAGE_BLOCKS, TCItemTags.ADAMANTIUM_STORAGE_BLOCKS, TCBlocks.ADAMANTIUM_BLOCK.get());

        addToBoth(TCItemTags.ORES, TCItemTags.COPPER_ORES, TCBlocks.COPPER_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.TIN_ORES, TCBlocks.TIN_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.LEAD_ORES, TCBlocks.LEAD_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.SILVER_ORES, TCBlocks.SILVER_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.PALLADIUM_ORES, TCBlocks.PALLADIUM_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.VIBRANIUM_ORES, TCBlocks.VIBRANIUM_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.OSMIUM_ORES, TCBlocks.OSMIUM_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.URANIUM_ORES, TCBlocks.URANIUM_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.TITANIUM_ORES, TCBlocks.TITANIUM_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.IRIDIUM_ORES, TCBlocks.IRIDIUM_ORE.get());
        addToBoth(TCItemTags.ORES, TCItemTags.URU_ORES, TCBlocks.URU_ORE.get());

        addToBoth(TCItemTags.INGOTS, TCItemTags.COPPER_INGOTS, TCItems.COPPER_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.TIN_INGOTS, TCItems.TIN_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.LEAD_INGOTS, TCItems.LEAD_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.SILVER_INGOTS, TCItems.SILVER_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.PALLADIUM_INGOTS, TCItems.PALLADIUM_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.VIBRANIUM_INGOTS, TCItems.VIBRANIUM_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.OSMIUM_INGOTS, TCItems.OSMIUM_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.URANIUM_INGOTS, TCItems.URANIUM_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.TITANIUM_INGOTS, TCItems.TITANIUM_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.IRIDIUM_INGOTS, TCItems.IRIDIUM_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.URU_INGOTS, TCItems.URU_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.BRONZE_INGOTS, TCItems.BRONZE_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.INTERTIUM_INGOTS, TCItems.INTERTIUM_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.STEEL_INGOTS, TCItems.STEEL_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.GOLD_TITANIUM_ALLOY_INGOTS, TCItems.GOLD_TITANIUM_ALLOY_INGOT.get());
        addToBoth(TCItemTags.INGOTS, TCItemTags.ADAMANTIUM_INGOTS, TCItems.ADAMANTIUM_INGOT.get());

        addToBoth(TCItemTags.NUGGETS, TCItemTags.COPPER_NUGGETS, TCItems.COPPER_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.TIN_NUGGETS, TCItems.TIN_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.LEAD_NUGGETS, TCItems.LEAD_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.SILVER_NUGGETS, TCItems.SILVER_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.PALLADIUM_NUGGETS, TCItems.PALLADIUM_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.VIBRANIUM_NUGGETS, TCItems.VIBRANIUM_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.OSMIUM_NUGGETS, TCItems.OSMIUM_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.URANIUM_NUGGETS, TCItems.URANIUM_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.TITANIUM_NUGGETS, TCItems.TITANIUM_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.IRIDIUM_NUGGETS, TCItems.IRIDIUM_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.URU_NUGGETS, TCItems.URU_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.BRONZE_NUGGETS, TCItems.BRONZE_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.INTERTIUM_NUGGETS, TCItems.INTERTIUM_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.STEEL_NUGGETS, TCItems.STEEL_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.GOLD_TITANIUM_ALLOY_NUGGETS, TCItems.GOLD_TITANIUM_ALLOY_NUGGET.get());
        addToBoth(TCItemTags.NUGGETS, TCItemTags.ADAMANTIUM_NUGGETS, TCItems.ADAMANTIUM_NUGGET.get());

        addToBoth(TCItemTags.DUSTS, TCItemTags.COAL_DUSTS, TCItems.COAL_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.CHARCOAL_DUSTS, TCItems.CHARCOAL_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.IRON_DUSTS, TCItems.IRON_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.GOLD_DUSTS, TCItems.GOLD_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.COPPER_DUSTS, TCItems.COPPER_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.TIN_DUSTS, TCItems.TIN_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.LEAD_DUSTS, TCItems.LEAD_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.SILVER_DUSTS, TCItems.SILVER_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.PALLADIUM_DUSTS, TCItems.PALLADIUM_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.VIBRANIUM_DUSTS, TCItems.VIBRANIUM_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.OSMIUM_DUSTS, TCItems.OSMIUM_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.URANIUM_DUSTS, TCItems.URANIUM_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.TITANIUM_DUSTS, TCItems.TITANIUM_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.IRIDIUM_DUSTS, TCItems.IRIDIUM_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.URU_DUSTS, TCItems.URU_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.BRONZE_DUSTS, TCItems.BRONZE_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.INTERTIUM_DUSTS, TCItems.INTERTIUM_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.STEEL_DUSTS, TCItems.STEEL_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.GOLD_TITANIUM_ALLOY_DUSTS, TCItems.GOLD_TITANIUM_ALLOY_DUST.get());
        addToBoth(TCItemTags.DUSTS, TCItemTags.ADAMANTIUM_DUSTS, TCItems.ADAMANTIUM_DUST.get());

        addToBoth(TCItemTags.PLATES, TCItemTags.IRON_PLATES, TCItems.IRON_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.GOLD_PLATES, TCItems.GOLD_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.COPPER_PLATES, TCItems.COPPER_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.TIN_PLATES, TCItems.TIN_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.LEAD_PLATES, TCItems.LEAD_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.SILVER_PLATES, TCItems.SILVER_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.PALLADIUM_PLATES, TCItems.PALLADIUM_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.VIBRANIUM_PLATES, TCItems.VIBRANIUM_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.OSMIUM_PLATES, TCItems.OSMIUM_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.URANIUM_PLATES, TCItems.URANIUM_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.TITANIUM_PLATES, TCItems.TITANIUM_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.IRIDIUM_PLATES, TCItems.IRIDIUM_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.URU_PLATES, TCItems.URU_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.BRONZE_PLATES, TCItems.BRONZE_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.INTERTIUM_PLATES, TCItems.INTERTIUM_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.STEEL_PLATES, TCItems.STEEL_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.GOLD_TITANIUM_ALLOY_PLATES, TCItems.GOLD_TITANIUM_ALLOY_PLATE.get());
        addToBoth(TCItemTags.PLATES, TCItemTags.ADAMANTIUM_PLATES, TCItems.ADAMANTIUM_PLATE.get());

        addToBoth(TCItemTags.FABRIC, TCItemTags.WHITE_FABRIC, TCItems.WHITE_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.ORANGE_FABRIC, TCItems.ORANGE_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.MAGENTA_FABRIC, TCItems.MAGENTA_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.LIGHT_BLUE_FABRIC, TCItems.LIGHT_BLUE_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.YELLOW_FABRIC, TCItems.YELLOW_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.LIME_FABRIC, TCItems.LIME_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.PINK_FABRIC, TCItems.PINK_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.GRAY_FABRIC, TCItems.GRAY_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.LIGHT_GRAY_FABRIC, TCItems.LIGHT_GRAY_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.CYAN_FABRIC, TCItems.CYAN_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.PURPLE_FABRIC, TCItems.PURPLE_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.BLUE_FABRIC, TCItems.BLUE_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.BROWN_FABRIC, TCItems.BROWN_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.GREEN_FABRIC, TCItems.GREEN_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.RED_FABRIC, TCItems.RED_FABRIC.get());
        addToBoth(TCItemTags.FABRIC, TCItemTags.BLACK_FABRIC, TCItems.BLACK_FABRIC.get());
    }

    public void addToBoth(ITag.INamedTag<Item> root, ITag.INamedTag<Item> branch, IItemProvider item) {
        this.getOrCreateBuilder(branch).add(item.asItem());
        this.getOrCreateBuilder(root).addTag(branch);
    }

    @Override
    public String getName() {
        return "ThreeCore Item Tags";
    }

}
