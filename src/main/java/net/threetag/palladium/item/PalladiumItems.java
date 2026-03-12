package net.threetag.palladium.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.flag.PalladiumFeatureFlags;

import java.util.EnumMap;

public class PalladiumItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Palladium.MOD_ID);
    public static final EnumMap<DyeColor, DeferredItem<Item>> FABRIC_BY_COLOR = new EnumMap<>(DyeColor.class);

    // Utility Blocks
    public static final DeferredItem<BlockItem> TAILORING_BENCH = ITEMS.registerSimpleBlockItem(PalladiumBlocks.TAILORING_BENCH, properties -> properties.requiredFeatures(PalladiumFeatureFlags.TAILORING));

    // Resource Blocks
    public static final DeferredItem<BlockItem> METEORITE_STONE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_STONE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_STONE_STAIRS = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_STONE_STAIRS, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_STONE_WALL = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_STONE_WALL, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_STONE_SLAB = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_STONE_SLAB, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_BRICKS = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_BRICKS, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_BRICK_STAIRS = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_BRICK_STAIRS, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_BRICK_WALL = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_BRICK_WALL, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_BRICK_SLAB = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_BRICK_SLAB, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_COAL_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_COAL_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_IRON_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_IRON_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_COPPER_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_COPPER_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_GOLD_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_GOLD_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_REDSTONE_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_REDSTONE_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_EMERALD_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_EMERALD_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_LAPIS_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_LAPIS_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_DIAMOND_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_DIAMOND_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_VIBRANIUM_ORE = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_VIBRANIUM_ORE, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> METEORITE_VIBRANIUM_VEIN = ITEMS.registerSimpleBlockItem(PalladiumBlocks.METEORITE_VIBRANIUM_VEIN, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> VIBRANIUM_BLOCK = ITEMS.registerSimpleBlockItem(PalladiumBlocks.VIBRANIUM_BLOCK, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<BlockItem> RAW_VIBRANIUM_BLOCK = ITEMS.registerSimpleBlockItem(PalladiumBlocks.RAW_VIBRANIUM_BLOCK, properties -> properties.requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));

    // Utilities
    public static final DeferredItem<Item> SUIT_STAND = ITEMS.registerItem("suit_stand", SuitStandItem::new);

    // Materials
    public static final DeferredItem<Item> RAW_VIBRANIUM = ITEMS.registerSimpleItem("raw_vibranium", properties -> properties.rarity(Rarity.EPIC).requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<Item> VIBRANIUM_INGOT = ITEMS.registerSimpleItem("vibranium_ingot", properties -> properties.rarity(Rarity.EPIC).requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));
    public static final DeferredItem<Item> VIBRANIUM_NUGGET = ITEMS.registerSimpleItem("vibranium_nugget", properties -> properties.rarity(Rarity.EPIC).requiredFeatures(PalladiumFeatureFlags.MATERIALS_VIBRANIUM));

    public static final DeferredItem<Item> WHITE_FABRIC = registerFabric(DyeColor.WHITE);
    public static final DeferredItem<Item> LIGHT_GRAY_FABRIC = registerFabric(DyeColor.LIGHT_GRAY);
    public static final DeferredItem<Item> GRAY_FABRIC = registerFabric(DyeColor.GRAY);
    public static final DeferredItem<Item> BLACK_FABRIC = registerFabric(DyeColor.BLACK);
    public static final DeferredItem<Item> BROWN_FABRIC = registerFabric(DyeColor.BROWN);
    public static final DeferredItem<Item> RED_FABRIC = registerFabric(DyeColor.RED);
    public static final DeferredItem<Item> ORANGE_FABRIC = registerFabric(DyeColor.ORANGE);
    public static final DeferredItem<Item> YELLOW_FABRIC = registerFabric(DyeColor.YELLOW);
    public static final DeferredItem<Item> LIME_FABRIC = registerFabric(DyeColor.LIME);
    public static final DeferredItem<Item> GREEN_FABRIC = registerFabric(DyeColor.GREEN);
    public static final DeferredItem<Item> CYAN_FABRIC = registerFabric(DyeColor.CYAN);
    public static final DeferredItem<Item> LIGHT_BLUE_FABRIC = registerFabric(DyeColor.LIGHT_BLUE);
    public static final DeferredItem<Item> BLUE_FABRIC = registerFabric(DyeColor.BLUE);
    public static final DeferredItem<Item> PURPLE_FABRIC = registerFabric(DyeColor.PURPLE);
    public static final DeferredItem<Item> MAGENTA_FABRIC = registerFabric(DyeColor.MAGENTA);
    public static final DeferredItem<Item> PINK_FABRIC = registerFabric(DyeColor.PINK);

    private static DeferredItem<Item> registerFabric(DyeColor color) {
        var supplier = ITEMS.registerSimpleItem(color.getName() + "_fabric", properties -> properties.requiredFeatures(PalladiumFeatureFlags.TAILORING));
        FABRIC_BY_COLOR.put(color, supplier);
        return supplier;
    }

}
