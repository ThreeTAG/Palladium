package net.threetag.palladium.item;

import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.tags.PalladiumItemTags;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumItems {

    public static final SimpleArmorMaterial VIBRANIUM_WEAVE = new SimpleArmorMaterial("vibranium_weave", 8,
            new int[]{1, 2, 3, 2}, 12, () -> SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F, 0.0F,
            () -> Ingredient.of(PalladiumItemTags.INGOTS_VIBRANIUM));

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Palladium.MOD_ID, Registry.ITEM_REGISTRY);
    public static final CreativeModeTabFiller FILLER_AFTER_COPPER_ORE = new CreativeModeTabFiller(() -> Items.DEEPSLATE_COPPER_ORE);
    public static final CreativeModeTabFiller FILLER_AFTER_DEEPSLATE_REDSTONE_ORE = new CreativeModeTabFiller(() -> Items.DEEPSLATE_REDSTONE_ORE);
    public static final CreativeModeTabFiller FILLER_AFTER_GILDED_BLACKSTONE = new CreativeModeTabFiller(() -> Items.GILDED_BLACKSTONE);
    public static final CreativeModeTabFiller FILLER_AFTER_COPPER_BLOCK = new CreativeModeTabFiller(() -> Items.COPPER_BLOCK);
    public static final CreativeModeTabFiller FILLER_AFTER_NETHERITE_BLOCK = new CreativeModeTabFiller(() -> Items.NETHERITE_BLOCK);
    public static final CreativeModeTabFiller FILLER_AFTER_RAW_COPPER_BLOCK = new CreativeModeTabFiller(() -> Items.RAW_COPPER_BLOCK);
    public static final CreativeModeTabFiller FILLER_AFTER_RAW_GOLD_BLOCK = new CreativeModeTabFiller(() -> Items.RAW_GOLD_BLOCK);
    public static final CreativeModeTabFiller FILLER_AFTER_COPPER = new CreativeModeTabFiller(() -> Items.COPPER_INGOT);
    public static final CreativeModeTabFiller FILLER_AFTER_GOLD = new CreativeModeTabFiller(() -> Items.GOLD_INGOT);
    public static final CreativeModeTabFiller FILLER_AFTER_NETHERITE = new CreativeModeTabFiller(() -> Items.NETHERITE_SCRAP);
    public static final CreativeModeTabFiller FILLER_AFTER_REDSTONE = new CreativeModeTabFiller(() -> Items.REDSTONE);
    public static final CreativeModeTabFiller FILLER_AFTER_WITHER_ROSE = new CreativeModeTabFiller(() -> Blocks.WITHER_ROSE);

    public static final RegistrySupplier<Item> LEAD_ORE = ITEMS.register("lead_ore", () -> new SortedBlockItem(PalladiumBlocks.LEAD_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_COPPER_ORE));
    public static final RegistrySupplier<Item> DEEPSLATE_LEAD_ORE = ITEMS.register("deepslate_lead_ore", () -> new SortedBlockItem(PalladiumBlocks.DEEPSLATE_LEAD_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_COPPER_ORE));
    public static final RegistrySupplier<Item> TITANIUM_ORE = ITEMS.register("titanium_ore", () -> new SortedBlockItem(PalladiumBlocks.TITANIUM_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_GILDED_BLACKSTONE));
    public static final RegistrySupplier<Item> VIBRANIUM_ORE = ITEMS.register("vibranium_ore", () -> new SortedBlockItem(PalladiumBlocks.VIBRANIUM_ORE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_GILDED_BLACKSTONE));

    public static final RegistrySupplier<Item> REDSTONE_FLUX_CRYSTAL_GEODE = ITEMS.register("redstone_flux_crystal_geode", () -> new SortedBlockItem(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_GEODE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_DEEPSLATE_REDSTONE_ORE));
    public static final RegistrySupplier<Item> DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE = ITEMS.register("deepslate_redstone_flux_crystal_geode", () -> new SortedBlockItem(PalladiumBlocks.DEEPSLATE_REDSTONE_FLUX_CRYSTAL_GEODE.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_DEEPSLATE_REDSTONE_ORE));
    public static final RegistrySupplier<Item> SMALL_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("small_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.SMALL_REDSTONE_FLUX_CRYSTAL_BUD.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistrySupplier<Item> MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("medium_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.MEDIUM_REDSTONE_FLUX_CRYSTAL_BUD.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistrySupplier<Item> LARGE_REDSTONE_FLUX_CRYSTAL_BUD = ITEMS.register("large_redstone_flux_crystal_bud", () -> new BlockItem(PalladiumBlocks.LARGE_REDSTONE_FLUX_CRYSTAL_BUD.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));
    public static final RegistrySupplier<Item> REDSTONE_FLUX_CRYSTAL_CLUSTER = ITEMS.register("redstone_flux_crystal_cluster", () -> new BlockItem(PalladiumBlocks.REDSTONE_FLUX_CRYSTAL_CLUSTER.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS)));

    public static final RegistrySupplier<Item> LEAD_BLOCK = ITEMS.register("lead_block", () -> new SortedBlockItem(PalladiumBlocks.LEAD_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_COPPER_BLOCK));
    public static final RegistrySupplier<Item> VIBRANIUM_BLOCK = ITEMS.register("vibranium_block", () -> new SortedBlockItem(PalladiumBlocks.VIBRANIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_NETHERITE_BLOCK));

    public static final RegistrySupplier<Item> RAW_LEAD_BLOCK = ITEMS.register("raw_lead_block", () -> new SortedBlockItem(PalladiumBlocks.RAW_LEAD_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_RAW_COPPER_BLOCK));
    public static final RegistrySupplier<Item> RAW_TITANIUM_BLOCK = ITEMS.register("raw_titanium_block", () -> new SortedBlockItem(PalladiumBlocks.RAW_TITANIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_RAW_GOLD_BLOCK));
    public static final RegistrySupplier<Item> RAW_VIBRANIUM_BLOCK = ITEMS.register("raw_vibranium_block", () -> new SortedBlockItem(PalladiumBlocks.RAW_VIBRANIUM_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS), FILLER_AFTER_RAW_GOLD_BLOCK));

    public static final RegistrySupplier<Item> SOLAR_PANEL = ITEMS.register("solar_panel", () -> new BlockItem(PalladiumBlocks.SOLAR_PANEL.get(), new Item.Properties().tab(PalladiumCreativeModeTabs.TECHNOLOGY)));
    public static final RegistrySupplier<Item> HEART_SHAPED_HERB = ITEMS.register("heart_shaped_herb", () -> new SortedBlockItem(PalladiumBlocks.HEART_SHAPED_HERB.get(), new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS), FILLER_AFTER_WITHER_ROSE));

    // -----------------------------------------------------------------------------------------------------------------

    public static final RegistrySupplier<Item> RAW_LEAD = ITEMS.register("raw_lead", () -> new SortedItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS), FILLER_AFTER_COPPER));
    public static final RegistrySupplier<Item> LEAD_INGOT = ITEMS.register("lead_ingot", () -> new SortedItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS), FILLER_AFTER_COPPER));
    public static final RegistrySupplier<Item> RAW_TITANIUM = ITEMS.register("raw_titanium", () -> new SortedItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS), FILLER_AFTER_GOLD));
    public static final RegistrySupplier<Item> RAW_VIBRANIUM = ITEMS.register("raw_vibranium", () -> new SortedItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS), FILLER_AFTER_NETHERITE));
    public static final RegistrySupplier<Item> VIBRANIUM_INGOT = ITEMS.register("vibranium_ingot", () -> new SortedItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS), FILLER_AFTER_NETHERITE));
    public static final RegistrySupplier<Item> REDSTONE_FLUX_CRYSTAL = ITEMS.register("redstone_flux_crystal", () -> new SortedItem(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS), FILLER_AFTER_REDSTONE));

    public static final RegistrySupplier<Item> SUIT_STAND = ITEMS.register("suit_stand", () -> new SuitStandItem(new Item.Properties().tab(CreativeModeTab.TAB_DECORATIONS).stacksTo(16)));
    public static final RegistrySupplier<Item> LEAD_CIRCUIT = ITEMS.register("lead_circuit", () -> new Item(new Item.Properties().tab(PalladiumCreativeModeTabs.TECHNOLOGY)));
    public static final RegistrySupplier<Item> QUARTZ_CIRCUIT = ITEMS.register("quartz_circuit", () -> new Item(new Item.Properties().tab(PalladiumCreativeModeTabs.TECHNOLOGY)));
    public static final RegistrySupplier<Item> VIBRANIUM_CIRCUIT = ITEMS.register("vibranium_circuit", () -> new Item(new Item.Properties().tab(PalladiumCreativeModeTabs.TECHNOLOGY).rarity(Rarity.RARE)));

    public static final RegistrySupplier<Item> LEAD_FLUX_CAPACITOR = ITEMS.register("lead_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().tab(PalladiumCreativeModeTabs.TECHNOLOGY), 500000, 1000, 1000));
    public static final RegistrySupplier<Item> QUARTZ_FLUX_CAPACITOR = ITEMS.register("quartz_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().tab(PalladiumCreativeModeTabs.TECHNOLOGY), 1000000, 5000, 5000));
    public static final RegistrySupplier<Item> VIBRANIUM_FLUX_CAPACITOR = ITEMS.register("vibranium_flux_capacitor", () -> new FluxCapacitorItem(new Item.Properties().tab(PalladiumCreativeModeTabs.TECHNOLOGY).rarity(Rarity.RARE), 2000000, 10000, 10000));
    
    public static final RegistrySupplier<Item> VIBRANIUM_WEAVE_BOOTS = ITEMS.register("vibranium_weave_boots", () -> new VibraniumWeaveArmorItem(VIBRANIUM_WEAVE, EquipmentSlot.FEET, (new Item.Properties()).tab(CreativeModeTab.TAB_COMBAT)));
}
