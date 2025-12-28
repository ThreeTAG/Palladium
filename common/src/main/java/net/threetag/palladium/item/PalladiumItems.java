package net.threetag.palladium.item;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.multiverse.MultiverseManager;
import net.threetag.palladium.multiverse.Universe;
import net.threetag.palladium.tags.PalladiumItemTags;
import net.threetag.palladiumcore.item.SimpleArmorMaterial;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;
import net.threetag.palladiumcore.util.Platform;

import java.util.EnumMap;

public class PalladiumItems {

    public static final EnumMap<DyeColor, RegistrySupplier<Item>> FABRIC_BY_COLOR = new EnumMap<>(DyeColor.class);

    public static final SimpleArmorMaterial VIBRANIUM_WEAVE = new SimpleArmorMaterial("vibranium_weave", 8,
            Util.make(new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
                enumMap.put(ArmorItem.Type.BOOTS, 2);
                enumMap.put(ArmorItem.Type.LEGGINGS, 2);
                enumMap.put(ArmorItem.Type.CHESTPLATE, 3);
                enumMap.put(ArmorItem.Type.HELMET, 1);
            }), 12, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            () -> Ingredient.of(PalladiumItemTags.VIBRANIUM_INGOTS));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Palladium.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> LEAD_ORE = ITEMS.register("lead_ore", () -> new BlockItem(PalladiumBlocks.LEAD_ORE.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> DEEPSLATE_LEAD_ORE = ITEMS.register("deepslate_lead_ore", () -> new BlockItem(PalladiumBlocks.DEEPSLATE_LEAD_ORE.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> TITANIUM_ORE = ITEMS.register("titanium_ore", () -> new BlockItem(PalladiumBlocks.TITANIUM_ORE.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> VIBRANIUM_ORE = ITEMS.register("vibranium_ore", () -> new BlockItem(PalladiumBlocks.VIBRANIUM_ORE.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> REDSTONE_FLUX_CRYSTAL_GEODE = ITEMS.register("redstone_flux_crystal_geode", () -> new BlockItem(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE = ITEMS.register("deepslate_redstone_flux_crystal_geode", () -> new BlockItem(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> SMALL_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("small_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("medium_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> LARGE_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("large_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> REDSTONE_FLUX_CRYSTAL_CLUSTER = ITEMS.register("redstone_flux_crystal_cluster", () -> new BlockItem(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> LEAD_BLOCK = ITEMS.register("lead_block", () -> new BlockItem(PalladiumBlocks.LEAD_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> TITANIUM_BLOCK = ITEMS.register("titanium_block", () -> new BlockItem(PalladiumBlocks.TITANIUM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> VIBRANIUM_BLOCK = ITEMS.register("vibranium_block", () -> new BlockItem(PalladiumBlocks.VIBRANIUM_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> RAW_LEAD_BLOCK = ITEMS.register("raw_lead_block", () -> new BlockItem(PalladiumBlocks.RAW_LEAD_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_TITANIUM_BLOCK = ITEMS.register("raw_titanium_block", () -> new BlockItem(PalladiumBlocks.RAW_TITANIUM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_VIBRANIUM_BLOCK = ITEMS.register("raw_vibranium_block", () -> new BlockItem(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> HEART_SHAPED_HERB = ITEMS.register("heart_shaped_herb", () -> new BlockItem(PalladiumBlocks.HEART_SHAPED_HERB.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> TAILORING_BENCH = ITEMS.register("tailoring_bench", () -> new BlockItem(PalladiumBlocks.TAILORING_BENCH.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> MULTIVERSAL_ITERATOR = ITEMS.register("multiversal_iterator", () -> new BlockItem(PalladiumBlocks.MULTIVERSAL_ITERATOR.get(), new Item.Properties()));

    // -----------------------------------------------------------------------------------------------------------------

    public static final RegistrySupplier<Item> RAW_LEAD = ITEMS.register("raw_lead", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_TITANIUM = ITEMS.register("raw_titanium", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_VIBRANIUM = ITEMS.register("raw_vibranium", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> VIBRANIUM_INGOT = ITEMS.register("vibranium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> REDSTONE_FLUX_CRYSTAL = ITEMS.register("redstone_flux_crystal", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> SUIT_STAND = ITEMS.register("suit_stand", () -> new SuitStandItem(new Item.Properties().stacksTo(16)));
    public static final RegistrySupplier<Item> MULTIVERSAL_EXTRAPOLATOR = ITEMS.register("multiversal_extrapolator", () -> new MultiversalExtrapolatorItem(new Item.Properties().durability(4)));
    public static final RegistrySupplier<Item> LEAD_CIRCUIT = ITEMS.register("lead_circuit", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> QUARTZ_CIRCUIT = ITEMS.register("quartz_circuit", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> VIBRANIUM_CIRCUIT = ITEMS.register("vibranium_circuit", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistrySupplier<FluxCapacitorItem> LEAD_FLUX_CAPACITOR = ITEMS.register("lead_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().stacksTo(1), 500000, 1000, 1000));
    public static final RegistrySupplier<FluxCapacitorItem> QUARTZ_FLUX_CAPACITOR = ITEMS.register("quartz_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().stacksTo(1), 1000000, 5000, 5000));
    public static final RegistrySupplier<FluxCapacitorItem> VIBRANIUM_FLUX_CAPACITOR = ITEMS.register("vibranium_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 2000000, 10000, 10000));

    public static final RegistrySupplier<Item> VIBRANIUM_WEAVE_BOOTS = ITEMS.register("vibranium_weave_boots", () -> new VibraniumWeaveArmorItem(VIBRANIUM_WEAVE, ArmorItem.Type.BOOTS, (new Item.Properties())));

    public static final RegistrySupplier<Item> WHITE_FABRIC = registerFabric(DyeColor.WHITE);
    public static final RegistrySupplier<Item> ORANGE_FABRIC = registerFabric(DyeColor.ORANGE);
    public static final RegistrySupplier<Item> MAGENTA_FABRIC = registerFabric(DyeColor.MAGENTA);
    public static final RegistrySupplier<Item> LIGHT_BLUE_FABRIC = registerFabric(DyeColor.LIGHT_BLUE);
    public static final RegistrySupplier<Item> YELLOW_FABRIC = registerFabric(DyeColor.YELLOW);
    public static final RegistrySupplier<Item> LIME_FABRIC = registerFabric(DyeColor.LIME);
    public static final RegistrySupplier<Item> PINK_FABRIC = registerFabric(DyeColor.PINK);
    public static final RegistrySupplier<Item> GRAY_FABRIC = registerFabric(DyeColor.GRAY);
    public static final RegistrySupplier<Item> LIGHT_GRAY_FABRIC = registerFabric(DyeColor.LIGHT_GRAY);
    public static final RegistrySupplier<Item> CYAN_FABRIC = registerFabric(DyeColor.CYAN);
    public static final RegistrySupplier<Item> PURPLE_FABRIC = registerFabric(DyeColor.PURPLE);
    public static final RegistrySupplier<Item> BLUE_FABRIC = registerFabric(DyeColor.BLUE);
    public static final RegistrySupplier<Item> BROWN_FABRIC = registerFabric(DyeColor.BROWN);
    public static final RegistrySupplier<Item> GREEN_FABRIC = registerFabric(DyeColor.GREEN);
    public static final RegistrySupplier<Item> RED_FABRIC = registerFabric(DyeColor.RED);
    public static final RegistrySupplier<Item> BLACK_FABRIC = registerFabric(DyeColor.BLACK);

    public static void init() {
        CreativeModeTabRegistry.addToTab(PalladiumCreativeModeTabs.PALLADIUM_MODS, entries -> {
            entries.add(
                    TAILORING_BENCH.get(),
                    MULTIVERSAL_ITERATOR.get(),
                    LEAD_ORE.get(),
                    DEEPSLATE_LEAD_ORE.get(),
                    TITANIUM_ORE.get(),
                    VIBRANIUM_ORE.get(),
                    REDSTONE_FLUX_CRYSTAL_GEODE.get(),
                    DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get(),
                    SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                    MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                    LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get(),
                    REDSTONE_FLUX_CRYSTAL_CLUSTER.get(),
                    LEAD_BLOCK.get(),
                    TITANIUM_BLOCK.get(),
                    VIBRANIUM_BLOCK.get(),
                    RAW_LEAD_BLOCK.get(),
                    RAW_TITANIUM_BLOCK.get(),
                    RAW_VIBRANIUM_BLOCK.get(),
                    HEART_SHAPED_HERB.get(),
                    RAW_LEAD.get(),
                    LEAD_INGOT.get(),
                    RAW_TITANIUM.get(),
                    TITANIUM_INGOT.get(),
                    RAW_VIBRANIUM.get(),
                    VIBRANIUM_INGOT.get(),
                    REDSTONE_FLUX_CRYSTAL.get(),
                    SUIT_STAND.get(),
                    MULTIVERSAL_EXTRAPOLATOR.get(),
                    LEAD_CIRCUIT.get(),
                    QUARTZ_CIRCUIT.get(),
                    VIBRANIUM_CIRCUIT.get(),
                    Items.KNOWLEDGE_BOOK,
                    WHITE_FABRIC.get(),
                    LIGHT_GRAY_FABRIC.get(),
                    GRAY_FABRIC.get(),
                    BLACK_FABRIC.get(),
                    BROWN_FABRIC.get(),
                    RED_FABRIC.get(),
                    ORANGE_FABRIC.get(),
                    YELLOW_FABRIC.get(),
                    LIME_FABRIC.get(),
                    GREEN_FABRIC.get(),
                    CYAN_FABRIC.get(),
                    LIGHT_BLUE_FABRIC.get(),
                    BLUE_FABRIC.get(),
                    PURPLE_FABRIC.get(),
                    MAGENTA_FABRIC.get(),
                    PINK_FABRIC.get()
            );

            entries.add(
                    LEAD_FLUX_CAPACITOR.get().getEmptyInstance(),
                    QUARTZ_FLUX_CAPACITOR.get().getEmptyInstance(),
                    VIBRANIUM_FLUX_CAPACITOR.get().getEmptyInstance(),
                    VIBRANIUM_WEAVE_BOOTS.get().getDefaultInstance()
            );
        });

        CreativeModeTabRegistry.addToTab(PalladiumCreativeModeTabs.TECHNOLOGY, entries -> {
            entries.add(MULTIVERSAL_ITERATOR.get());
            entries.add(LEAD_CIRCUIT.get());
            entries.add(QUARTZ_CIRCUIT.get());
            entries.add(VIBRANIUM_CIRCUIT.get());
            entries.add(LEAD_FLUX_CAPACITOR.get().getEmptyInstance());
            entries.add(LEAD_FLUX_CAPACITOR.get().getFullyChargedInstance());
            entries.add(QUARTZ_FLUX_CAPACITOR.get().getEmptyInstance());
            entries.add(QUARTZ_FLUX_CAPACITOR.get().getFullyChargedInstance());
            entries.add(VIBRANIUM_FLUX_CAPACITOR.get().getEmptyInstance());
            entries.add(VIBRANIUM_FLUX_CAPACITOR.get().getFullyChargedInstance());
            entries.add(MULTIVERSAL_EXTRAPOLATOR.get());
            for (Universe universe : MultiverseManager.getInstance(Platform.isClient()).getUniverses().values()) {
                var stack = MULTIVERSAL_EXTRAPOLATOR.get().getDefaultInstance();
                MultiversalExtrapolatorItem.setUniverse(stack, universe);
                entries.add(stack);
            }
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.FUNCTIONAL_BLOCKS), entries -> {
            entries.addAfter(Items.ARMOR_STAND, SUIT_STAND.get());
            entries.addAfter(Items.LOOM, TAILORING_BENCH.get());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.INGREDIENTS), entries -> {
            entries.addAfter(Items.COPPER_INGOT, RAW_LEAD.get(), LEAD_INGOT.get());
            entries.addAfter(Items.GOLD_INGOT, RAW_TITANIUM.get(), TITANIUM_INGOT.get());
            entries.addAfter(Items.NETHERITE_INGOT, RAW_VIBRANIUM.get(), VIBRANIUM_INGOT.get());
            entries.addAfter(Items.REDSTONE, REDSTONE_FLUX_CRYSTAL.get());
            entries.addAfter(Items.PINK_DYE,
                    WHITE_FABRIC.get(),
                    LIGHT_GRAY_FABRIC.get(),
                    GRAY_FABRIC.get(),
                    BLACK_FABRIC.get(),
                    BROWN_FABRIC.get(),
                    RED_FABRIC.get(),
                    ORANGE_FABRIC.get(),
                    YELLOW_FABRIC.get(),
                    LIME_FABRIC.get(),
                    GREEN_FABRIC.get(),
                    CYAN_FABRIC.get(),
                    LIGHT_BLUE_FABRIC.get(),
                    BLUE_FABRIC.get(),
                    PURPLE_FABRIC.get(),
                    MAGENTA_FABRIC.get(),
                    PINK_FABRIC.get()
            );
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.BUILDING_BLOCKS), entries -> {
            entries.add(Items.DIAMOND_BLOCK, LEAD_BLOCK.get(), TITANIUM_BLOCK.get());
            entries.addAfter(Items.NETHERITE_BLOCK, VIBRANIUM_BLOCK.get());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.NATURAL_BLOCKS), entries -> {
            entries.addAfter(Items.DEEPSLATE_COPPER_ORE, LEAD_ORE.get(), DEEPSLATE_LEAD_ORE.get(), TITANIUM_ORE.get(), VIBRANIUM_ORE.get());
            entries.addAfter(Items.DEEPSLATE_REDSTONE_ORE, REDSTONE_FLUX_CRYSTAL_GEODE.get(), DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get());
            entries.addAfter(Items.AMETHYST_CLUSTER, SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get(), MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get(), LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get(), REDSTONE_FLUX_CRYSTAL_CLUSTER.get());
            entries.addAfter(Items.RAW_GOLD_BLOCK, RAW_LEAD_BLOCK.get(), RAW_TITANIUM_BLOCK.get(), RAW_VIBRANIUM_BLOCK.get());
            entries.addAfter(Items.WITHER_ROSE, HEART_SHAPED_HERB.get());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.COMBAT), entries -> {
            entries.addAfter(Items.LEATHER_BOOTS, VIBRANIUM_WEAVE_BOOTS.get());
        });

    }

    private static RegistrySupplier<Item> registerFabric(DyeColor color) {
        var supplier = ITEMS.register(color.getName() + "_fabric", () -> new Item(new Item.Properties()));
        FABRIC_BY_COLOR.put(color, supplier);
        return supplier;
    }

}
