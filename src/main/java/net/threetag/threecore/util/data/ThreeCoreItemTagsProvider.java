package net.threetag.threecore.util.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.util.item.ThreeCoreItemTags;

public class ThreeCoreItemTagsProvider extends ItemTagsProvider {

    public ThreeCoreItemTagsProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerTags() {
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.COPPER_STORAGE_BLOCKS, ThreeCoreBase.COPPER_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.TIN_STORAGE_BLOCKS, ThreeCoreBase.TIN_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.LEAD_STORAGE_BLOCKS, ThreeCoreBase.LEAD_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.SILVER_STORAGE_BLOCKS, ThreeCoreBase.SILVER_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.PALLADIUM_STORAGE_BLOCKS, ThreeCoreBase.PALLADIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.VIBRANIUM_STORAGE_BLOCKS, ThreeCoreBase.VIBRANIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.OSMIUM_STORAGE_BLOCKS, ThreeCoreBase.OSMIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.URANIUM_STORAGE_BLOCKS, ThreeCoreBase.URANIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.TITANIUM_STORAGE_BLOCKS, ThreeCoreBase.TITANIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.IRIDIUM_STORAGE_BLOCKS, ThreeCoreBase.IRIDIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.URU_STORAGE_BLOCKS, ThreeCoreBase.URU_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.BRONZE_STORAGE_BLOCKS, ThreeCoreBase.BRONZE_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.INTERTIUM_STORAGE_BLOCKS, ThreeCoreBase.INTERTIUM_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.STEEL_STORAGE_BLOCKS, ThreeCoreBase.STEEL_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_STORAGE_BLOCKS, ThreeCoreBase.GOLD_TITANIUM_ALLOY_BLOCK);
        addToBoth(ThreeCoreItemTags.STORAGE_BLOCKS, ThreeCoreItemTags.ADAMANTIUM_STORAGE_BLOCKS, ThreeCoreBase.ADAMANTIUM_BLOCK);

        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.COPPER_ORES, ThreeCoreBase.COPPER_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.TIN_ORES, ThreeCoreBase.TIN_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.LEAD_ORES, ThreeCoreBase.LEAD_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.SILVER_ORES, ThreeCoreBase.SILVER_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.PALLADIUM_ORES, ThreeCoreBase.PALLADIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.VIBRANIUM_ORES, ThreeCoreBase.VIBRANIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.OSMIUM_ORES, ThreeCoreBase.OSMIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.URANIUM_ORES, ThreeCoreBase.URANIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.TITANIUM_ORES, ThreeCoreBase.TITANIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.IRIDIUM_ORES, ThreeCoreBase.IRIDIUM_ORE);
        addToBoth(ThreeCoreItemTags.ORES, ThreeCoreItemTags.URU_ORES, ThreeCoreBase.URU_ORE);

        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.COPPER_INGOTS, ThreeCoreBase.COPPER_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.TIN_INGOTS, ThreeCoreBase.TIN_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.LEAD_INGOTS, ThreeCoreBase.LEAD_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.SILVER_INGOTS, ThreeCoreBase.SILVER_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.PALLADIUM_INGOTS, ThreeCoreBase.PALLADIUM_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.VIBRANIUM_INGOTS, ThreeCoreBase.VIBRANIUM_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.OSMIUM_INGOTS, ThreeCoreBase.OSMIUM_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.URANIUM_INGOTS, ThreeCoreBase.URANIUM_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.TITANIUM_INGOTS, ThreeCoreBase.TITANIUM_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.IRIDIUM_INGOTS, ThreeCoreBase.IRIDIUM_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.URU_INGOTS, ThreeCoreBase.URU_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.BRONZE_INGOTS, ThreeCoreBase.BRONZE_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.INTERTIUM_INGOTS, ThreeCoreBase.INTERTIUM_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.STEEL_INGOTS, ThreeCoreBase.STEEL_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_INGOTS, ThreeCoreBase.GOLD_TITANIUM_ALLOY_INGOT);
        addToBoth(ThreeCoreItemTags.INGOTS, ThreeCoreItemTags.ADAMANTIUM_INGOTS, ThreeCoreBase.ADAMANTIUM_INGOT);

        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.COPPER_NUGGETS, ThreeCoreBase.COPPER_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.TIN_NUGGETS, ThreeCoreBase.TIN_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.LEAD_NUGGETS, ThreeCoreBase.LEAD_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.SILVER_NUGGETS, ThreeCoreBase.SILVER_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.PALLADIUM_NUGGETS, ThreeCoreBase.PALLADIUM_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.VIBRANIUM_NUGGETS, ThreeCoreBase.VIBRANIUM_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.OSMIUM_NUGGETS, ThreeCoreBase.OSMIUM_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.URANIUM_NUGGETS, ThreeCoreBase.URANIUM_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.TITANIUM_NUGGETS, ThreeCoreBase.TITANIUM_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.IRIDIUM_NUGGETS, ThreeCoreBase.IRIDIUM_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.URU_NUGGETS, ThreeCoreBase.URU_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.BRONZE_NUGGETS, ThreeCoreBase.BRONZE_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.INTERTIUM_NUGGETS, ThreeCoreBase.INTERTIUM_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.STEEL_NUGGETS, ThreeCoreBase.STEEL_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_NUGGETS, ThreeCoreBase.GOLD_TITANIUM_ALLOY_NUGGET);
        addToBoth(ThreeCoreItemTags.NUGGETS, ThreeCoreItemTags.ADAMANTIUM_NUGGETS, ThreeCoreBase.ADAMANTIUM_NUGGET);

        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.COAL_DUSTS, ThreeCoreBase.COAL_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.CHARCOAL_DUSTS, ThreeCoreBase.CHARCOAL_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.IRON_DUSTS, ThreeCoreBase.IRON_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.GOLD_DUSTS, ThreeCoreBase.GOLD_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.COPPER_DUSTS, ThreeCoreBase.COPPER_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.TIN_DUSTS, ThreeCoreBase.TIN_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.LEAD_DUSTS, ThreeCoreBase.LEAD_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.SILVER_DUSTS, ThreeCoreBase.SILVER_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.PALLADIUM_DUSTS, ThreeCoreBase.PALLADIUM_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.VIBRANIUM_DUSTS, ThreeCoreBase.VIBRANIUM_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.OSMIUM_DUSTS, ThreeCoreBase.OSMIUM_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.URANIUM_DUSTS, ThreeCoreBase.URANIUM_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.TITANIUM_DUSTS, ThreeCoreBase.TITANIUM_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.IRIDIUM_DUSTS, ThreeCoreBase.IRIDIUM_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.URU_DUSTS, ThreeCoreBase.URU_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.BRONZE_DUSTS, ThreeCoreBase.BRONZE_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.INTERTIUM_DUSTS, ThreeCoreBase.INTERTIUM_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.STEEL_DUSTS, ThreeCoreBase.STEEL_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_DUSTS, ThreeCoreBase.GOLD_TITANIUM_ALLOY_DUST);
        addToBoth(ThreeCoreItemTags.DUSTS, ThreeCoreItemTags.ADAMANTIUM_DUSTS, ThreeCoreBase.ADAMANTIUM_DUST);

        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.IRON_PLATES, ThreeCoreBase.IRON_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.GOLD_PLATES, ThreeCoreBase.GOLD_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.COPPER_PLATES, ThreeCoreBase.COPPER_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.TIN_PLATES, ThreeCoreBase.TIN_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.LEAD_PLATES, ThreeCoreBase.LEAD_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.SILVER_PLATES, ThreeCoreBase.SILVER_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.PALLADIUM_PLATES, ThreeCoreBase.PALLADIUM_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.VIBRANIUM_PLATES, ThreeCoreBase.VIBRANIUM_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.OSMIUM_PLATES, ThreeCoreBase.OSMIUM_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.URANIUM_PLATES, ThreeCoreBase.URANIUM_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.TITANIUM_PLATES, ThreeCoreBase.TITANIUM_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.IRIDIUM_PLATES, ThreeCoreBase.IRIDIUM_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.URU_PLATES, ThreeCoreBase.URU_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.BRONZE_PLATES, ThreeCoreBase.BRONZE_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.INTERTIUM_PLATES, ThreeCoreBase.INTERTIUM_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.STEEL_PLATES, ThreeCoreBase.STEEL_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.GOLD_TITANIUM_ALLOY_PLATES, ThreeCoreBase.GOLD_TITANIUM_ALLOY_PLATE);
        addToBoth(ThreeCoreItemTags.PLATES, ThreeCoreItemTags.ADAMANTIUM_PLATES, ThreeCoreBase.ADAMANTIUM_PLATE);
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