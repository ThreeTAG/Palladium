package net.threetag.threecore.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ThreeCoreCommonConfig;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.item.CapacitorBlockItem;
import net.threetag.threecore.item.ItemGroupRegistry;
import net.threetag.threecore.item.TCItems;

import java.util.function.Supplier;

public class TCBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ThreeCore.MODID);

    public static final RegistryObject<Block> CONSTRUCTION_TABLE = BLOCKS.register("construction_table", () -> new ConstructionTableBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> MULTIVERSAL_ITERATOR = register("multiversal_iterator", () -> new MultiversalIteratorBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)), ItemGroupRegistry.getTechnologyGroup());
    public static final RegistryObject<Block> GRINDER = register("grinder", () -> new GrinderBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));
    public static final RegistryObject<Block> HYDRAULIC_PRESS = register("hydraulic_press", () -> new HydraulicPressBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));
    public static final RegistryObject<Block> FLUID_COMPOSER = register("fluid_composer", () -> new FluidComposerBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));
    public static final RegistryObject<Block> CAPACITOR_BLOCK = BLOCKS.register("capacitor_block", () -> new CapacitorBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F), CapacitorBlock.Type.NORMAL));
    public static final RegistryObject<Item> CAPACITOR_BLOCK_ITEM = TCItems.ITEMS.register("capacitor_block", () -> new CapacitorBlockItem(CAPACITOR_BLOCK.get(), new Item.Properties().maxStackSize(1).group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)), ThreeCoreServerConfig.ENERGY.CAPACITOR));
    public static final RegistryObject<Block> ADVANCED_CAPACITOR_BLOCK = BLOCKS.register("advanced_capacitor_block", () -> new CapacitorBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F), CapacitorBlock.Type.ADVANCED));
    public static final RegistryObject<Item> ADVANCED_CAPACITOR_BLOCK_ITEM = TCItems.ITEMS.register("advanced_capacitor_block", () -> new CapacitorBlockItem(ADVANCED_CAPACITOR_BLOCK.get(), new Item.Properties().maxStackSize(1).group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)), ThreeCoreServerConfig.ENERGY.ADVANCED_CAPACITOR));
    public static final RegistryObject<Block> STIRLING_GENERATOR = register("stirling_generator", () -> new StirlingGeneratorBlock(Block.Properties.create(Material.IRON).setLightLevel(value -> value.get(BlockStateProperties.LIT) ? 13 : 0).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));
    public static final RegistryObject<Block> SOLAR_PANEL = register("solar_panel", () -> new SolarPanelBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));
    public static final RegistryObject<Block> GOLD_CONDUIT = register("gold_conduit", () -> new EnergyConduitBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F), EnergyConduitBlock.ConduitType.GOLD, 2F / 16F), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));
    public static final RegistryObject<Block> COPPER_CONDUIT = register("copper_conduit", () -> new EnergyConduitBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F), EnergyConduitBlock.ConduitType.COPPER, 2F / 16F), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));
    public static final RegistryObject<Block> SILVER_CONDUIT = register("silver_conduit", () -> new EnergyConduitBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F), EnergyConduitBlock.ConduitType.SILVER, 2F / 16F), ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY));


    // Storage Blocks
    public static final RegistryObject<Block> COPPER_BLOCK = register("copper_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> TIN_BLOCK = register("tin_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> LEAD_BLOCK = register("lead_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(4.0F, 12.0F)));
    public static final RegistryObject<Block> SILVER_BLOCK = register("silver_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> PALLADIUM_BLOCK = register("palladium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> VIBRANIUM_BLOCK = register("vibranium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(15.0F, 18.0F)), Rarity.RARE);
    public static final RegistryObject<Block> OSMIUM_BLOCK = register("osmium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> URANIUM_BLOCK = register("uranium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> TITANIUM_BLOCK = register("titanium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(10.0F, 12.0F)));
    public static final RegistryObject<Block> IRIDIUM_BLOCK = register("iridium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(10.0F, 15.0F)), Rarity.UNCOMMON);
    public static final RegistryObject<Block> URU_BLOCK = register("uru_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(10.0F, 15.0F)), Rarity.EPIC);
    public static final RegistryObject<Block> BRONZE_BLOCK = register("bronze_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> INTERTIUM_BLOCK = register("intertium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> STEEL_BLOCK = register("steel_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)));
    public static final RegistryObject<Block> GOLD_TITANIUM_ALLOY_BLOCK = register("gold_titanium_alloy_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(7.0F, 8.0F)));
    public static final RegistryObject<Block> ADAMANTIUM_BLOCK = register("adamantium_block", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(15F, 18.0F)), Rarity.RARE);

    // Ores
    public static final RegistryObject<Block> COPPER_ORE = register("copper_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> TIN_ORE = register("tin_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> LEAD_ORE = register("lead_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> SILVER_ORE = register("silver_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> PALLADIUM_ORE = register("palladium_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> VIBRANIUM_ORE = register("vibranium_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F).setLightLevel(value -> 4)), Rarity.RARE);
    public static final RegistryObject<Block> OSMIUM_ORE = register("osmium_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> URANIUM_ORE = register("uranium_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> TITANIUM_ORE = register("titanium_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F)));
    public static final RegistryObject<Block> IRIDIUM_ORE = register("iridium_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F)), Rarity.UNCOMMON);
    public static final RegistryObject<Block> URU_ORE = register("uru_ore", () -> new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F)), Rarity.EPIC);

    public static final RegistryObject<Block> WHITE_CONCRETE_SLAB = register("white_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.WHITE_CONCRETE)));
    public static final RegistryObject<Block> ORANGE_CONCRETE_SLAB = register("orange_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.ORANGE_CONCRETE)));
    public static final RegistryObject<Block> MAGENTA_CONCRETE_SLAB = register("magenta_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.MAGENTA_CONCRETE)));
    public static final RegistryObject<Block> LIGHT_BLUE_CONCRETE_SLAB = register("light_blue_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.LIGHT_BLUE_CONCRETE)));
    public static final RegistryObject<Block> YELLOW_CONCRETE_SLAB = register("yellow_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.YELLOW_CONCRETE)));
    public static final RegistryObject<Block> LIME_CONCRETE_SLAB = register("lime_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.LIME_CONCRETE)));
    public static final RegistryObject<Block> PINK_CONCRETE_SLAB = register("pink_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.PINK_CONCRETE)));
    public static final RegistryObject<Block> GRAY_CONCRETE_SLAB = register("gray_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.GRAY_CONCRETE)));
    public static final RegistryObject<Block> LIGHT_GRAY_CONCRETE_SLAB = register("light_gray_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.LIGHT_GRAY_CONCRETE)));
    public static final RegistryObject<Block> CYAN_CONCRETE_SLAB = register("cyan_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.CYAN_CONCRETE)));
    public static final RegistryObject<Block> PURPLE_CONCRETE_SLAB = register("purple_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.PURPLE_CONCRETE)));
    public static final RegistryObject<Block> BLUE_CONCRETE_SLAB = register("blue_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.BLUE_CONCRETE)));
    public static final RegistryObject<Block> BROWN_CONCRETE_SLAB = register("brown_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.BROWN_CONCRETE)));
    public static final RegistryObject<Block> GREEN_CONCRETE_SLAB = register("green_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.GREEN_CONCRETE)));
    public static final RegistryObject<Block> RED_CONCRETE_SLAB = register("red_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.RED_CONCRETE)));
    public static final RegistryObject<Block> BLACK_CONCRETE_SLAB = register("black_concrete_slab", () -> new SlabBlock(Block.Properties.from(Blocks.BLACK_CONCRETE)));

    public static final RegistryObject<Block> WHITE_CONCRETE_STAIRS = register("white_concrete_stairs", () -> new StairsBlock(Blocks.WHITE_CONCRETE::getDefaultState, Block.Properties.from(Blocks.WHITE_CONCRETE)));
    public static final RegistryObject<Block> ORANGE_CONCRETE_STAIRS = register("orange_concrete_stairs", () -> new StairsBlock(Blocks.ORANGE_CONCRETE::getDefaultState, Block.Properties.from(Blocks.ORANGE_CONCRETE)));
    public static final RegistryObject<Block> MAGENTA_CONCRETE_STAIRS = register("magenta_concrete_stairs", () -> new StairsBlock(Blocks.MAGENTA_CONCRETE::getDefaultState, Block.Properties.from(Blocks.MAGENTA_CONCRETE)));
    public static final RegistryObject<Block> LIGHT_BLUE_CONCRETE_STAIRS = register("light_blue_concrete_stairs", () -> new StairsBlock(Blocks.LIGHT_BLUE_CONCRETE::getDefaultState, Block.Properties.from(Blocks.LIGHT_BLUE_CONCRETE)));
    public static final RegistryObject<Block> YELLOW_CONCRETE_STAIRS = register("yellow_concrete_stairs", () -> new StairsBlock(Blocks.YELLOW_CONCRETE::getDefaultState, Block.Properties.from(Blocks.YELLOW_CONCRETE)));
    public static final RegistryObject<Block> LIME_CONCRETE_STAIRS = register("lime_concrete_stairs", () -> new StairsBlock(Blocks.LIME_CONCRETE::getDefaultState, Block.Properties.from(Blocks.LIME_CONCRETE)));
    public static final RegistryObject<Block> PINK_CONCRETE_STAIRS = register("pink_concrete_stairs", () -> new StairsBlock(Blocks.PINK_CONCRETE::getDefaultState, Block.Properties.from(Blocks.PINK_CONCRETE)));
    public static final RegistryObject<Block> GRAY_CONCRETE_STAIRS = register("gray_concrete_stairs", () -> new StairsBlock(Blocks.GRAY_CONCRETE::getDefaultState, Block.Properties.from(Blocks.GRAY_CONCRETE)));
    public static final RegistryObject<Block> LIGHT_GRAY_CONCRETE_STAIRS = register("light_gray_concrete_stairs", () -> new StairsBlock(Blocks.LIGHT_GRAY_CONCRETE::getDefaultState, Block.Properties.from(Blocks.LIGHT_GRAY_CONCRETE)));
    public static final RegistryObject<Block> CYAN_CONCRETE_STAIRS = register("cyan_concrete_stairs", () -> new StairsBlock(Blocks.CYAN_CONCRETE::getDefaultState, Block.Properties.from(Blocks.CYAN_CONCRETE)));
    public static final RegistryObject<Block> PURPLE_CONCRETE_STAIRS = register("purple_concrete_stairs", () -> new StairsBlock(Blocks.PURPLE_CONCRETE::getDefaultState, Block.Properties.from(Blocks.PURPLE_CONCRETE)));
    public static final RegistryObject<Block> BLUE_CONCRETE_STAIRS = register("blue_concrete_stairs", () -> new StairsBlock(Blocks.BLUE_CONCRETE::getDefaultState, Block.Properties.from(Blocks.BLUE_CONCRETE)));
    public static final RegistryObject<Block> BROWN_CONCRETE_STAIRS = register("brown_concrete_stairs", () -> new StairsBlock(Blocks.BROWN_CONCRETE::getDefaultState, Block.Properties.from(Blocks.BROWN_CONCRETE)));
    public static final RegistryObject<Block> GREEN_CONCRETE_STAIRS = register("green_concrete_stairs", () -> new StairsBlock(Blocks.GREEN_CONCRETE::getDefaultState, Block.Properties.from(Blocks.GREEN_CONCRETE)));
    public static final RegistryObject<Block> RED_CONCRETE_STAIRS = register("red_concrete_stairs", () -> new StairsBlock(Blocks.RED_CONCRETE::getDefaultState, Block.Properties.from(Blocks.RED_CONCRETE)));
    public static final RegistryObject<Block> BLACK_CONCRETE_STAIRS = register("black_concrete_stairs", () -> new StairsBlock(Blocks.BLACK_CONCRETE::getDefaultState, Block.Properties.from(Blocks.BLACK_CONCRETE)));

    public static void initOres(BiomeLoadingEvent e) {
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(COPPER_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.COPPER));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(TIN_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.TIN));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(LEAD_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.LEAD));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(SILVER_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.SILVER));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(PALLADIUM_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.PALLADIUM));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(VIBRANIUM_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.VIBRANIUM));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(OSMIUM_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.OSMIUM));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(URANIUM_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.URANIUM));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(TITANIUM_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.TITANIUM));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(IRIDIUM_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.IRIDIUM));
        e.getGeneration().withFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createOreFeature(URU_ORE.get().getDefaultState(), ThreeCoreCommonConfig.MATERIALS.URU));
    }

    public static ConfiguredFeature<?, ?> createOreFeature(BlockState ore, ThreeCoreCommonConfig.Materials.OreConfig config) {
        return Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, ore, config.size.get())).withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(config.minHeight.get(), config.minHeight.get(), config.maxHeight.get()))).square().func_242731_b(config.size.get());
    }

    public static void initRenderTypes() {
        RenderTypeLookup.setRenderLayer(CONSTRUCTION_TABLE.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(GRINDER.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(HYDRAULIC_PRESS.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(FLUID_COMPOSER.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(STIRLING_GENERATOR.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(VIBRANIUM_BLOCK.get(), RenderType.getTranslucent());
    }

    public static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        TCItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)));
        return registryObject;
    }

    public static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, Supplier<Item> itemSupplier) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        TCItems.ITEMS.register(id, itemSupplier);
        return registryObject;
    }

    public static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, ItemGroup itemGroup) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        TCItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().group(itemGroup)));
        return registryObject;
    }

    public static <T extends Block> RegistryObject<T> register(String id, Supplier<T> blockSupplier, Rarity rarity) {
        RegistryObject<T> registryObject = BLOCKS.register(id, blockSupplier);
        TCItems.ITEMS.register(id, () -> new BlockItem(registryObject.get(), new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).rarity(rarity)));
        return registryObject;
    }

}
