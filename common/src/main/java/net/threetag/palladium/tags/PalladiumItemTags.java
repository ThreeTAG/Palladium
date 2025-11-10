package net.threetag.palladium.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;

import java.util.EnumMap;

public class PalladiumItemTags {

    public static final EnumMap<DyeColor, TagKey<Item>> FABRIC_BY_COLOR = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, TagKey<Item>> DYE_BY_COLOR = new EnumMap<>(DyeColor.class);

    public static final TagKey<Item> VIBRATION_ABSORPTION_BOOTS = tag("vibration_absorption_boots");
    public static final TagKey<Item> FABRICS = tag("fabrics");
    public static final TagKey<Item> BLACK_FABRICS = fabric(DyeColor.BLACK);
    public static final TagKey<Item> BLUE_FABRICS = fabric(DyeColor.BLUE);
    public static final TagKey<Item> BROWN_FABRICS = fabric(DyeColor.BROWN);
    public static final TagKey<Item> CYAN_FABRICS = fabric(DyeColor.CYAN);
    public static final TagKey<Item> GRAY_FABRICS = fabric(DyeColor.GRAY);
    public static final TagKey<Item> GREEN_FABRICS = fabric(DyeColor.GREEN);
    public static final TagKey<Item> LIGHT_BLUE_FABRICS = fabric(DyeColor.LIGHT_BLUE);
    public static final TagKey<Item> LIGHT_GRAY_FABRICS = fabric(DyeColor.LIGHT_GRAY);
    public static final TagKey<Item> LIME_FABRICS = fabric(DyeColor.LIME);
    public static final TagKey<Item> MAGENTA_FABRICS = fabric(DyeColor.MAGENTA);
    public static final TagKey<Item> ORANGE_FABRICS = fabric(DyeColor.ORANGE);
    public static final TagKey<Item> PINK_FABRICS = fabric(DyeColor.PINK);
    public static final TagKey<Item> PURPLE_FABRICS = fabric(DyeColor.PURPLE);
    public static final TagKey<Item> RED_FABRICS = fabric(DyeColor.RED);
    public static final TagKey<Item> WHITE_FABRICS = fabric(DyeColor.WHITE);
    public static final TagKey<Item> YELLOW_FABRICS = fabric(DyeColor.YELLOW);

    // Connector tags
    public static final TagKey<Item> WOODEN_STICKS = connector("wooden_sticks");
    public static final TagKey<Item> STRINGS = connector("strings");
    public static final TagKey<Item> IRON_INGOTS = connector("iron_ingots");
    public static final TagKey<Item> LEAD_INGOTS = connector("lead_ingots");
    public static final TagKey<Item> VIBRANIUM_INGOTS = connector("vibranium_ingots");
    public static final TagKey<Item> QUARTZ = connector("quartz");
    public static final TagKey<Item> GOLD_INGOTS = connector("gold_ingots");
    public static final TagKey<Item> COPPER_INGOTS = connector("copper_ingots");
    public static final TagKey<Item> DIAMONDS = connector("diamonds");

    public static final TagKey<Item> BLACK_DYES = dyeConnector(DyeColor.BLACK);
    public static final TagKey<Item> BLUE_DYES = dyeConnector(DyeColor.BLUE);
    public static final TagKey<Item> BROWN_DYES = dyeConnector(DyeColor.BROWN);
    public static final TagKey<Item> CYAN_DYES = dyeConnector(DyeColor.CYAN);
    public static final TagKey<Item> GRAY_DYES = dyeConnector(DyeColor.GRAY);
    public static final TagKey<Item> GREEN_DYES = dyeConnector(DyeColor.GREEN);
    public static final TagKey<Item> LIGHT_BLUE_DYES = dyeConnector(DyeColor.LIGHT_BLUE);
    public static final TagKey<Item> LIGHT_GRAY_DYES = dyeConnector(DyeColor.LIGHT_GRAY);
    public static final TagKey<Item> LIME_DYES = dyeConnector(DyeColor.LIME);
    public static final TagKey<Item> MAGENTA_DYES = dyeConnector(DyeColor.MAGENTA);
    public static final TagKey<Item> ORANGE_DYES = dyeConnector(DyeColor.ORANGE);
    public static final TagKey<Item> PINK_DYES = dyeConnector(DyeColor.PINK);
    public static final TagKey<Item> PURPLE_DYES = dyeConnector(DyeColor.PURPLE);
    public static final TagKey<Item> RED_DYES = dyeConnector(DyeColor.RED);
    public static final TagKey<Item> WHITE_DYES = dyeConnector(DyeColor.WHITE);
    public static final TagKey<Item> YELLOW_DYES = dyeConnector(DyeColor.YELLOW);

    private static TagKey<Item> tag(String domain, String path) {
        return TagKey.create(Registries.ITEM, new ResourceLocation(domain, path));
    }

    private static TagKey<Item> tag(String path) {
        return TagKey.create(Registries.ITEM, Palladium.id(path));
    }

    private static TagKey<Item> fabric(DyeColor color) {
        var tag = TagKey.create(Registries.ITEM, Palladium.id(color.getName() + "_fabrics"));
        FABRIC_BY_COLOR.put(color, tag);
        return tag;
    }

    private static TagKey<Item> dyeConnector(DyeColor color) {
        var tag = TagKey.create(Registries.ITEM, Palladium.id("connector/" + color.getName() + "_dyes"));
        DYE_BY_COLOR.put(color, tag);
        return tag;
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

        public static final TagKey<Item> BLACK_DYES = fabricTag("black_dyes");
        public static final TagKey<Item> BLUE_DYES = fabricTag("blue_dyes");
        public static final TagKey<Item> BROWN_DYES = fabricTag("brown_dyes");
        public static final TagKey<Item> CYAN_DYES = fabricTag("cyan_dyes");
        public static final TagKey<Item> GRAY_DYES = fabricTag("gray_dyes");
        public static final TagKey<Item> GREEN_DYES = fabricTag("green_dyes");
        public static final TagKey<Item> LIGHT_BLUE_DYES = fabricTag("light_blue_dyes");
        public static final TagKey<Item> LIGHT_GRAY_DYES = fabricTag("light_gray_dyes");
        public static final TagKey<Item> LIME_DYES = fabricTag("lime_dyes");
        public static final TagKey<Item> MAGENTA_DYES = fabricTag("magenta_dyes");
        public static final TagKey<Item> ORANGE_DYES = fabricTag("orange_dyes");
        public static final TagKey<Item> PINK_DYES = fabricTag("pink_dyes");
        public static final TagKey<Item> PURPLE_DYES = fabricTag("purple_dyes");
        public static final TagKey<Item> RED_DYES = fabricTag("red_dyes");
        public static final TagKey<Item> WHITE_DYES = fabricTag("white_dyes");
        public static final TagKey<Item> YELLOW_DYES = fabricTag("yellow_dyes");

    }

}
