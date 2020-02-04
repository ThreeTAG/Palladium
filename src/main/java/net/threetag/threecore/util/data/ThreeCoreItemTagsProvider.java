package net.threetag.threecore.util.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.threetag.threecore.base.block.TCBaseBlocks;
import net.threetag.threecore.base.item.TCBaseItems;
import net.threetag.threecore.util.item.ThreeCoreItemTags;

public class ThreeCoreItemTagsProvider extends ItemTagsProvider {

    public ThreeCoreItemTagsProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerTags() {
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.COPPER_STORAGE_BLOCKS, TCBaseBlocks.COPPER_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.TIN_STORAGE_BLOCKS, TCBaseBlocks.TIN_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.LEAD_STORAGE_BLOCKS, TCBaseBlocks.LEAD_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.SILVER_STORAGE_BLOCKS, TCBaseBlocks.SILVER_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.PALLADIUM_STORAGE_BLOCKS, TCBaseBlocks.PALLADIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.VIBRANIUM_STORAGE_BLOCKS, TCBaseBlocks.VIBRANIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.OSMIUM_STORAGE_BLOCKS, TCBaseBlocks.OSMIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.URANIUM_STORAGE_BLOCKS, TCBaseBlocks.URANIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.TITANIUM_STORAGE_BLOCKS, TCBaseBlocks.TITANIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.IRIDIUM_STORAGE_BLOCKS, TCBaseBlocks.IRIDIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.URU_STORAGE_BLOCKS, TCBaseBlocks.URU_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.BRONZE_STORAGE_BLOCKS, TCBaseBlocks.BRONZE_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.INTERTIUM_STORAGE_BLOCKS, TCBaseBlocks.INTERTIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.STEEL_STORAGE_BLOCKS, TCBaseBlocks.STEEL_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS, TCBaseBlocks.GOLD_TITANIUM_ALLOY_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.ADAMANTIUM_STORAGE_BLOCKS, TCBaseBlocks.ADAMANTIUM_BLOCK);

        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.COPPER_ORES, TCBaseBlocks.COPPER_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.TIN_ORES, TCBaseBlocks.TIN_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.LEAD_ORES, TCBaseBlocks.LEAD_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.SILVER_ORES, TCBaseBlocks.SILVER_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.PALLADIUM_ORES, TCBaseBlocks.PALLADIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.VIBRANIUM_ORES, TCBaseBlocks.VIBRANIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.OSMIUM_ORES, TCBaseBlocks.OSMIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.URANIUM_ORES, TCBaseBlocks.URANIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.TITANIUM_ORES, TCBaseBlocks.TITANIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.IRIDIUM_ORES, TCBaseBlocks.IRIDIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.URU_ORES, TCBaseBlocks.URU_ORE);

        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.COPPER_INGOTS, TCBaseItems.COPPER_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.TIN_INGOTS, TCBaseItems.TIN_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.LEAD_INGOTS, TCBaseItems.LEAD_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.SILVER_INGOTS, TCBaseItems.SILVER_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.PALLADIUM_INGOTS, TCBaseItems.PALLADIUM_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.VIBRANIUM_INGOTS, TCBaseItems.VIBRANIUM_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.OSMIUM_INGOTS, TCBaseItems.OSMIUM_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.URANIUM_INGOTS, TCBaseItems.URANIUM_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.TITANIUM_INGOTS, TCBaseItems.TITANIUM_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.IRIDIUM_INGOTS, TCBaseItems.IRIDIUM_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.URU_INGOTS, TCBaseItems.URU_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.BRONZE_INGOTS, TCBaseItems.BRONZE_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.INTERTIUM_INGOTS, TCBaseItems.INTERTIUM_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.STEEL_INGOTS, TCBaseItems.STEEL_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_INGOTS, TCBaseItems.GOLD_TITANIUM_ALLOY_INGOT.get());
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.ADAMANTIUM_INGOTS, TCBaseItems.ADAMANTIUM_INGOT.get());

        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.COPPER_NUGGETS, TCBaseItems.COPPER_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.TIN_NUGGETS, TCBaseItems.TIN_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.LEAD_NUGGETS, TCBaseItems.LEAD_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.SILVER_NUGGETS, TCBaseItems.SILVER_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.PALLADIUM_NUGGETS, TCBaseItems.PALLADIUM_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.VIBRANIUM_NUGGETS, TCBaseItems.VIBRANIUM_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.OSMIUM_NUGGETS, TCBaseItems.OSMIUM_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.URANIUM_NUGGETS, TCBaseItems.URANIUM_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.TITANIUM_NUGGETS, TCBaseItems.TITANIUM_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.IRIDIUM_NUGGETS, TCBaseItems.IRIDIUM_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.URU_NUGGETS, TCBaseItems.URU_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.BRONZE_NUGGETS, TCBaseItems.BRONZE_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.INTERTIUM_NUGGETS, TCBaseItems.INTERTIUM_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.STEEL_NUGGETS, TCBaseItems.STEEL_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_NUGGETS, TCBaseItems.GOLD_TITANIUM_ALLOY_NUGGET.get());
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.ADAMANTIUM_NUGGETS, TCBaseItems.ADAMANTIUM_NUGGET.get());

        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.COAL_DUSTS, TCBaseItems.COAL_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.CHARCOAL_DUSTS, TCBaseItems.CHARCOAL_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.IRON_DUSTS, TCBaseItems.IRON_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.GOLD_DUSTS, TCBaseItems.GOLD_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.COPPER_DUSTS, TCBaseItems.COPPER_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.TIN_DUSTS, TCBaseItems.TIN_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.LEAD_DUSTS, TCBaseItems.LEAD_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.SILVER_DUSTS, TCBaseItems.SILVER_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.PALLADIUM_DUSTS, TCBaseItems.PALLADIUM_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.VIBRANIUM_DUSTS, TCBaseItems.VIBRANIUM_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.OSMIUM_DUSTS, TCBaseItems.OSMIUM_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.URANIUM_DUSTS, TCBaseItems.URANIUM_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.TITANIUM_DUSTS, TCBaseItems.TITANIUM_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.IRIDIUM_DUSTS, TCBaseItems.IRIDIUM_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.URU_DUSTS, TCBaseItems.URU_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.BRONZE_DUSTS, TCBaseItems.BRONZE_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.INTERTIUM_DUSTS, TCBaseItems.INTERTIUM_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.STEEL_DUSTS, TCBaseItems.STEEL_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_DUSTS, TCBaseItems.GOLD_TITANIUM_ALLOY_DUST.get());
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.ADAMANTIUM_DUSTS, TCBaseItems.ADAMANTIUM_DUST.get());

        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.IRON_PLATES, TCBaseItems.IRON_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.GOLD_PLATES, TCBaseItems.GOLD_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.COPPER_PLATES, TCBaseItems.COPPER_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.TIN_PLATES, TCBaseItems.TIN_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.LEAD_PLATES, TCBaseItems.LEAD_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.SILVER_PLATES, TCBaseItems.SILVER_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.PALLADIUM_PLATES, TCBaseItems.PALLADIUM_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.VIBRANIUM_PLATES, TCBaseItems.VIBRANIUM_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.OSMIUM_PLATES, TCBaseItems.OSMIUM_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.URANIUM_PLATES, TCBaseItems.URANIUM_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.TITANIUM_PLATES, TCBaseItems.TITANIUM_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.IRIDIUM_PLATES, TCBaseItems.IRIDIUM_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.URU_PLATES, TCBaseItems.URU_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.BRONZE_PLATES, TCBaseItems.BRONZE_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.INTERTIUM_PLATES, TCBaseItems.INTERTIUM_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.STEEL_PLATES, TCBaseItems.STEEL_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_PLATES, TCBaseItems.GOLD_TITANIUM_ALLOY_PLATE.get());
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.ADAMANTIUM_PLATES, TCBaseItems.ADAMANTIUM_PLATE.get());

        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.WHITE_FABRIC, TCBaseItems.WHITE_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.ORANGE_FABRIC, TCBaseItems.ORANGE_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.MAGENTA_FABRIC, TCBaseItems.MAGENTA_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.LIGHT_BLUE_FABRIC, TCBaseItems.LIGHT_BLUE_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.YELLOW_FABRIC, TCBaseItems.YELLOW_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.LIME_FABRIC, TCBaseItems.LIME_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.PINK_FABRIC, TCBaseItems.PINK_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.GRAY_FABRIC, TCBaseItems.GRAY_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.LIGHT_GRAY_FABRIC, TCBaseItems.LIGHT_GRAY_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.CYAN_FABRIC, TCBaseItems.CYAN_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.PURPLE_FABRIC, TCBaseItems.PURPLE_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.BLUE_FABRIC, TCBaseItems.BLUE_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.BROWN_FABRIC, TCBaseItems.BROWN_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.GREEN_FABRIC, TCBaseItems.GREEN_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.RED_FABRIC, TCBaseItems.RED_FABRIC.get());
        addToBoth(ThreeCoreItemTags.FABRIC, ThreeCoreItemTags.BLACK_FABRIC, TCBaseItems.BLACK_FABRIC.get());
    }

    public void addToBoth(Tag<Item> root, Tag<Item> branch, IItemProvider item) {
        this.getBuilder(branch).add(item.asItem());
        this.getBuilder(root).add(branch);
    }

    @Override
    public String getName() {
        return "ThreeCore Item Tags";
    }

}