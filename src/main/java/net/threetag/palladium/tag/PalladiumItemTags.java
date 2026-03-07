package net.threetag.palladium.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.threetag.palladium.Palladium;

import java.util.EnumMap;

public class PalladiumItemTags {

    public static final EnumMap<DyeColor, TagKey<Item>> FABRIC_BY_COLOR = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, TagKey<Item>> DYE_BY_COLOR = new EnumMap<>(DyeColor.class);
    public static final EnumMap<DyeColor, TagKey<Item>> DYED_BY_COLOR = new EnumMap<>(DyeColor.class);

    public static final TagKey<Item> VIBRANIUM_ORES = c("ores/vibranium");
    public static final TagKey<Item> VIBRANIUM_STORAGE_BLOCKS = c("storage_blocks/vibranium");
    public static final TagKey<Item> RAW_VIBRANIUM_STORAGE_BLOCKS = c("storage_blocks/raw_vibranium");
    public static final TagKey<Item> RAW_VIBRANIUM = c("raw_materials/vibranium");
    public static final TagKey<Item> VIBRANIUM_INGOTS = c("ingots/vibranium");
    public static final TagKey<Item> VIBRANIUM_NUGGETS = c("nuggets/vibranium");

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

    private static TagKey<Item> tag(String path) {
        return TagKey.create(Registries.ITEM, Palladium.id(path));
    }

    private static TagKey<Item> c(String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", path));
    }

    private static TagKey<Item> fabric(DyeColor color) {
        var tag = tag(color.getName() + "_fabrics");
        FABRIC_BY_COLOR.put(color, tag);
        return tag;
    }

    static {
        DYE_BY_COLOR.put(DyeColor.BLACK, Tags.Items.DYES_BLACK);
        DYE_BY_COLOR.put(DyeColor.BLUE, Tags.Items.DYES_BLUE);
        DYE_BY_COLOR.put(DyeColor.BROWN, Tags.Items.DYES_BROWN);
        DYE_BY_COLOR.put(DyeColor.CYAN, Tags.Items.DYES_CYAN);
        DYE_BY_COLOR.put(DyeColor.GRAY, Tags.Items.DYES_GRAY);
        DYE_BY_COLOR.put(DyeColor.GREEN, Tags.Items.DYES_GREEN);
        DYE_BY_COLOR.put(DyeColor.LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE);
        DYE_BY_COLOR.put(DyeColor.LIGHT_GRAY, Tags.Items.DYES_LIGHT_GRAY);
        DYE_BY_COLOR.put(DyeColor.LIME, Tags.Items.DYES_LIME);
        DYE_BY_COLOR.put(DyeColor.MAGENTA, Tags.Items.DYES_MAGENTA);
        DYE_BY_COLOR.put(DyeColor.ORANGE, Tags.Items.DYES_ORANGE);
        DYE_BY_COLOR.put(DyeColor.PINK, Tags.Items.DYES_PINK);
        DYE_BY_COLOR.put(DyeColor.PURPLE, Tags.Items.DYES_PURPLE);
        DYE_BY_COLOR.put(DyeColor.RED, Tags.Items.DYES_RED);
        DYE_BY_COLOR.put(DyeColor.WHITE, Tags.Items.DYES_WHITE);
        DYE_BY_COLOR.put(DyeColor.YELLOW, Tags.Items.DYES_YELLOW);

        DYED_BY_COLOR.put(DyeColor.BLACK, Tags.Items.DYED_BLACK);
        DYED_BY_COLOR.put(DyeColor.BLUE, Tags.Items.DYED_BLUE);
        DYED_BY_COLOR.put(DyeColor.BROWN, Tags.Items.DYED_BROWN);
        DYED_BY_COLOR.put(DyeColor.CYAN, Tags.Items.DYED_CYAN);
        DYED_BY_COLOR.put(DyeColor.GRAY, Tags.Items.DYED_GRAY);
        DYED_BY_COLOR.put(DyeColor.GREEN, Tags.Items.DYED_GREEN);
        DYED_BY_COLOR.put(DyeColor.LIGHT_BLUE, Tags.Items.DYED_LIGHT_BLUE);
        DYED_BY_COLOR.put(DyeColor.LIGHT_GRAY, Tags.Items.DYED_LIGHT_GRAY);
        DYED_BY_COLOR.put(DyeColor.LIME, Tags.Items.DYED_LIME);
        DYED_BY_COLOR.put(DyeColor.MAGENTA, Tags.Items.DYED_MAGENTA);
        DYED_BY_COLOR.put(DyeColor.ORANGE, Tags.Items.DYED_ORANGE);
        DYED_BY_COLOR.put(DyeColor.PINK, Tags.Items.DYED_PINK);
        DYED_BY_COLOR.put(DyeColor.PURPLE, Tags.Items.DYED_PURPLE);
        DYED_BY_COLOR.put(DyeColor.RED, Tags.Items.DYED_RED);
        DYED_BY_COLOR.put(DyeColor.WHITE, Tags.Items.DYED_WHITE);
        DYED_BY_COLOR.put(DyeColor.YELLOW, Tags.Items.DYED_YELLOW);
    }
    
}
