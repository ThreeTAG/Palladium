package net.threetag.palladium.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;

public class PalladiumItemTags {

    public static final TagKey<Item> VIBRATION_ABSORPTION_BOOTS = tag(Palladium.MOD_ID, "vibration_absorption_boots");

    // Connector tags
    public static final TagKey<Item> WOODEN_STICKS = commonTag("rods/wooden");
    public static final TagKey<Item> IRON_INGOTS = commonTag("ingots/iron");
    public static final TagKey<Item> LEAD_INGOTS = commonTag("ingots/lead");
    public static final TagKey<Item> VIBRANIUM_INGOTS = commonTag("ingots/vibranium");
    public static final TagKey<Item> QUARTZ = commonTag("gems/quartz");
    public static final TagKey<Item> GOLD_INGOTS = commonTag("ingots/gold");
    public static final TagKey<Item> COPPER_INGOTS = commonTag("ingots/copper");
    public static final TagKey<Item> DIAMONDS = commonTag("gems/diamond");


    public static final TagKey<Item> ORES_LEAD = commonTag("ores/lead");
    public static final TagKey<Item> ORES_TITANIUM = commonTag("ores/titanium");
    public static final TagKey<Item> ORES_VIBRANIUM = commonTag("ores/vibranium");

    public static final TagKey<Item> RAW_LEAD = commonTag("raw_materials/lead");
    public static final TagKey<Item> RAW_TITANIUM = commonTag("raw_materials/titanium");
    public static final TagKey<Item> RAW_VIBRANIUM = commonTag("raw_materials/vibranium");

    public static final TagKey<Item> RAW_LEAD_BLOCKS = commonTag("storage_blocks/raw_lead");
    public static final TagKey<Item> RAW_TITANIUM_BLOCKS = commonTag("storage_blocks/raw_titanium");
    public static final TagKey<Item> RAW_VIBRANIUM_BLOCKS = commonTag("storage_blocks/raw_vibranium");

    public static final TagKey<Item> STORAGE_BLOCKS_LEAD = commonTag("storage_blocks/lead");
    public static final TagKey<Item> STORAGE_BLOCKS_VIBRANIUM = commonTag("storage_blocks/vibranium");

    public static final TagKey<Item> INGOTS_LEAD = commonTag("ingots/lead");
    public static final TagKey<Item> INGOTS_VIBRANIUM = commonTag("ingots/vibranium");


    private static TagKey<Item> tag(String domain, String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(domain, path));
    }

    private static TagKey<Item> commonTag(String path) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", path));
    }

}
