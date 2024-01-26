package net.threetag.palladium.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;

public class PalladiumItemTags {

    public static final TagKey<Item> VIBRATION_ABSORPTION_BOOTS = tag(Palladium.MOD_ID, "vibration_absorption_boots");

    // Connector tags
    public static final TagKey<Item> WOODEN_STICKS = connector("wooden_sticks");
    public static final TagKey<Item> IRON_INGOTS = connector("iron_ingots");
    public static final TagKey<Item> LEAD_INGOTS = connector("lead_ingots");
    public static final TagKey<Item> VIBRANIUM_INGOTS = connector("vibranium_ingots");
    public static final TagKey<Item> QUARTZ = connector("quartz");
    public static final TagKey<Item> GOLD_INGOTS = connector("gold_ingots");
    public static final TagKey<Item> COPPER_INGOTS = connector("copper_ingots");
    public static final TagKey<Item> DIAMONDS = connector("diamonds");

    private static TagKey<Item> tag(String domain, String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(domain, path));
    }

    private static TagKey<Item> connector(String path) {
        return TagKey.create(Registries.ITEM, Palladium.id("connector/" + path));
    }

    private static TagKey<Item> forgeTag(String path) {
        return tag("forge", path);
    }

    private static TagKey<Item> fabricTag(String path) {
        return tag("c", path);
    }

    public static class Forge {

        public static final TagKey<Item> ORES_LEAD = forgeTag("ores/lead");
        public static final TagKey<Item> ORES_TITANIUM = forgeTag("ores/titanium");
        public static final TagKey<Item> ORES_VIBRANIUM = forgeTag("ores/vibranium");

        public static final TagKey<Item> RAW_LEAD = forgeTag("raw_materials/lead");
        public static final TagKey<Item> RAW_TITANIUM = forgeTag("raw_materials/titanium");
        public static final TagKey<Item> RAW_VIBRANIUM = forgeTag("raw_materials/vibranium");

        public static final TagKey<Item> RAW_LEAD_BLOCKS = forgeTag("storage_blocks/raw_lead");
        public static final TagKey<Item> RAW_TITANIUM_BLOCKS = forgeTag("storage_blocks/raw_titanium");
        public static final TagKey<Item> RAW_VIBRANIUM_BLOCKS = forgeTag("storage_blocks/raw_vibranium");

        public static final TagKey<Item> STORAGE_BLOCKS_LEAD = forgeTag("storage_blocks/lead");
        public static final TagKey<Item> STORAGE_BLOCKS_VIBRANIUM = forgeTag("storage_blocks/vibranium");

        public static final TagKey<Item> INGOTS_LEAD = forgeTag("ingots/lead");
        public static final TagKey<Item> INGOTS_VIBRANIUM = forgeTag("ingots/vibranium");

    }

    public static class Fabric {

        public static final TagKey<Item> WOODEN_STICKS = fabricTag("wood_sticks");
        public static final TagKey<Item> REDSTONE = fabricTag("redstone_dusts");
        public static final TagKey<Item> REDSTONE_BLOCK = fabricTag("redstone_blocks");
        public static final TagKey<Item> QUARTZ = fabricTag("quartz");
        public static final TagKey<Item> INGOTS_IRON = fabricTag("iron_ingots");
        public static final TagKey<Item> INGOTS_GOLD = fabricTag("gold_ingots");
        public static final TagKey<Item> INGOTS_COPPER = fabricTag("copper_ingots");
        public static final TagKey<Item> DIAMONDS = fabricTag("diamonds");
        public static final TagKey<Item> INGOTS = fabricTag("ingots");

        public static final TagKey<Item> ORES = fabricTag("ores");
        public static final TagKey<Item> ORES_LEAD = fabricTag("lead_ores");
        public static final TagKey<Item> ORES_TITANIUM = fabricTag("titanium_ores");
        public static final TagKey<Item> ORES_VIBRANIUM = fabricTag("vibranium_ores");

        public static final TagKey<Item> RAW_ORES = fabricTag("raw_ores");
        public static final TagKey<Item> RAW_LEAD = fabricTag("raw_lead_ores");
        public static final TagKey<Item> RAW_TITANIUM = fabricTag("raw_titanium_ores");
        public static final TagKey<Item> RAW_VIBRANIUM = fabricTag("raw_vibranium_ores");

        public static final TagKey<Item> RAW_LEAD_BLOCKS = fabricTag("raw_lead_blocks");
        public static final TagKey<Item> RAW_TITANIUM_BLOCKS = fabricTag("raw_titanium_blocks");
        public static final TagKey<Item> RAW_VIBRANIUM_BLOCKS = fabricTag("raw_vibranium_blocks");

        public static final TagKey<Item> STORAGE_BLOCKS_LEAD = fabricTag("lead_blocks");
        public static final TagKey<Item> STORAGE_BLOCKS_VIBRANIUM = fabricTag("vibranium_blocks");

        public static final TagKey<Item> INGOTS_LEAD = fabricTag("lead_ingots");
        public static final TagKey<Item> INGOTS_VIBRANIUM = fabricTag("vibranium_ingots");

    }

}
