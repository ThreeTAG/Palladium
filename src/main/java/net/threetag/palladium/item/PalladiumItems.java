package net.threetag.palladium.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
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

    // Utilities
    public static final DeferredItem<Item> SUIT_STAND = ITEMS.registerItem("suit_stand", SuitStandItem::new);

    // Materials
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
