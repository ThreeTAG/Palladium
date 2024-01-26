package net.threetag.palladium.item;

import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.tags.PalladiumItemTags;
import net.threetag.palladiumcore.item.SimpleArmorMaterial;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

import java.util.EnumMap;

public class PalladiumItems {

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
    public static final RegistrySupplier<Item> VIBRANIUM_BLOCK = ITEMS.register("vibranium_block", () -> new BlockItem(PalladiumBlocks.VIBRANIUM_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> RAW_LEAD_BLOCK = ITEMS.register("raw_lead_block", () -> new BlockItem(PalladiumBlocks.RAW_LEAD_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_TITANIUM_BLOCK = ITEMS.register("raw_titanium_block", () -> new BlockItem(PalladiumBlocks.RAW_TITANIUM_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_VIBRANIUM_BLOCK = ITEMS.register("raw_vibranium_block", () -> new BlockItem(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get(), new Item.Properties()));

    public static final RegistrySupplier<Item> HEART_SHAPED_HERB = ITEMS.register("heart_shaped_herb", () -> new BlockItem(PalladiumBlocks.HEART_SHAPED_HERB.get(), new Item.Properties()));

    // -----------------------------------------------------------------------------------------------------------------

    public static final RegistrySupplier<Item> RAW_LEAD = ITEMS.register("raw_lead", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_TITANIUM = ITEMS.register("raw_titanium", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> RAW_VIBRANIUM = ITEMS.register("raw_vibranium", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> VIBRANIUM_INGOT = ITEMS.register("vibranium_ingot", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> REDSTONE_FLUX_CRYSTAL = ITEMS.register("redstone_flux_crystal", () -> new Item(new Item.Properties()));

    public static final RegistrySupplier<Item> SUIT_STAND = ITEMS.register("suit_stand", () -> new SuitStandItem(new Item.Properties().stacksTo(16)));
    public static final RegistrySupplier<Item> LEAD_CIRCUIT = ITEMS.register("lead_circuit", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> QUARTZ_CIRCUIT = ITEMS.register("quartz_circuit", () -> new Item(new Item.Properties()));
    public static final RegistrySupplier<Item> VIBRANIUM_CIRCUIT = ITEMS.register("vibranium_circuit", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistrySupplier<FluxCapacitorItem> LEAD_FLUX_CAPACITOR = ITEMS.register("lead_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().stacksTo(1), 500000, 1000, 1000));
    public static final RegistrySupplier<FluxCapacitorItem> QUARTZ_FLUX_CAPACITOR = ITEMS.register("quartz_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().stacksTo(1), 1000000, 5000, 5000));
    public static final RegistrySupplier<FluxCapacitorItem> VIBRANIUM_FLUX_CAPACITOR = ITEMS.register("vibranium_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 2000000, 10000, 10000));

    public static final RegistrySupplier<Item> VIBRANIUM_WEAVE_BOOTS = ITEMS.register("vibranium_weave_boots", () -> new VibraniumWeaveArmorItem(VIBRANIUM_WEAVE, ArmorItem.Type.BOOTS, (new Item.Properties())));

    public static void init() {
        CreativeModeTabRegistry.addToTab(PalladiumCreativeModeTabs.TECHNOLOGY, entries -> {
            entries.add(LEAD_CIRCUIT.get());
            entries.add(QUARTZ_CIRCUIT.get());
            entries.add(VIBRANIUM_CIRCUIT.get());
            entries.add(LEAD_FLUX_CAPACITOR.get());
            entries.add(LEAD_FLUX_CAPACITOR.get().getFullyChargedInstance());
            entries.add(QUARTZ_FLUX_CAPACITOR.get());
            entries.add(QUARTZ_FLUX_CAPACITOR.get().getFullyChargedInstance());
            entries.add(VIBRANIUM_FLUX_CAPACITOR.get());
            entries.add(VIBRANIUM_FLUX_CAPACITOR.get().getFullyChargedInstance());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.FUNCTIONAL_BLOCKS), entries -> {
            entries.addAfter(Items.ARMOR_STAND, SUIT_STAND.get());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.INGREDIENTS), entries -> {
            entries.addAfter(Items.COPPER_INGOT, RAW_LEAD.get(), LEAD_INGOT.get());
            entries.addAfter(Items.GOLD_INGOT, RAW_TITANIUM.get());
            entries.addAfter(Items.NETHERITE_INGOT, RAW_VIBRANIUM.get(), VIBRANIUM_INGOT.get());
            entries.addAfter(Items.REDSTONE, REDSTONE_FLUX_CRYSTAL.get());
        });

        CreativeModeTabRegistry.addToTab(() -> BuiltInRegistries.CREATIVE_MODE_TAB.get(CreativeModeTabs.BUILDING_BLOCKS), entries -> {
            entries.addAfter(Items.COPPER_BLOCK, LEAD_BLOCK.get());
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
}
