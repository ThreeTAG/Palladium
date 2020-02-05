package net.threetag.threecore.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.ThreeCoreCommonConfig;
import net.threetag.threecore.ThreeCoreServerConfig;
import net.threetag.threecore.item.CapacitorBlockItem;
import net.threetag.threecore.tileentity.*;
import net.threetag.threecore.item.ItemGroupRegistry;

import java.util.Objects;

@ObjectHolder(ThreeCore.MODID)
public class TCBlocks {

    @ObjectHolder("construction_table")
    public static final Block CONSTRUCTION_TABLE = null;

    @ObjectHolder("grinder")
    public static final Block GRINDER = null;
    @ObjectHolder("grinder")
    public static final TileEntityType<GrinderTileEntity> GRINDER_TILE_ENTITY = null;

    @ObjectHolder("hydraulic_press")
    public static final Block HYDRAULIC_PRESS = null;
    @ObjectHolder("hydraulic_press")
    public static final TileEntityType<HydraulicPressTileEntity> HYDRAULIC_PRESS_TILE_ENTITY = null;

    @ObjectHolder("fluid_composer")
    public static final Block FLUID_COMPOSER = null;
    @ObjectHolder("fluid_composer")
    public static final TileEntityType<FluidComposerTileEntity> FLUID_COMPOSER_TILE_ENTITY = null;

    @ObjectHolder("capacitor_block")
    public static final Block CAPACITOR_BLOCK = null;
    @ObjectHolder("advanced_capacitor_block")
    public static final Block ADVANCED_CAPACITOR_BLOCK = null;
    @ObjectHolder("capacitor_block")
    public static final TileEntityType<CapacitorBlockTileEntity> CAPACITOR_BLOCK_TILE_ENTITY = null;

    @ObjectHolder("stirling_generator")
    public static final Block STIRLING_GENERATOR = null;
    @ObjectHolder("stirling_generator")
    public static final TileEntityType<StirlingGeneratorTileEntity> STIRLING_GENERATOR_TILE_ENTITY = null;

    @ObjectHolder("solar_panel")
    public static final Block SOLAR_PANEL = null;
    @ObjectHolder("solar_panel")
    public static final TileEntityType<SolarPanelTileEntity> SOLAR_PANEL_TILE_ENTITY = null;

    @ObjectHolder("gold_conduit")
    public static final Block GOLD_CONDUIT = null;
    @ObjectHolder("copper_conduit")
    public static final Block COPPER_CONDUIT = null;
    @ObjectHolder("silver_conduit")
    public static final Block SILVER_CONDUIT = null;
    @ObjectHolder("conduit")
    public static final TileEntityType<EnergyConduitTileEntity> CONDUIT_TILE_ENTITY = null;

    // Storage Blocks
    @ObjectHolder("copper_block")
    public static final Block COPPER_BLOCK = null;
    @ObjectHolder("tin_block")
    public static final Block TIN_BLOCK = null;
    @ObjectHolder("lead_block")
    public static final Block LEAD_BLOCK = null;
    @ObjectHolder("silver_block")
    public static final Block SILVER_BLOCK = null;
    @ObjectHolder("palladium_block")
    public static final Block PALLADIUM_BLOCK = null;
    @ObjectHolder("vibranium_block")
    public static final Block VIBRANIUM_BLOCK = null;
    @ObjectHolder("osmium_block")
    public static final Block OSMIUM_BLOCK = null;
    @ObjectHolder("uranium_block")
    public static final Block URANIUM_BLOCK = null;
    @ObjectHolder("titanium_block")
    public static final Block TITANIUM_BLOCK = null;
    @ObjectHolder("iridium_block")
    public static final Block IRIDIUM_BLOCK = null;
    @ObjectHolder("uru_block")
    public static final Block URU_BLOCK = null;
    @ObjectHolder("bronze_block")
    public static final Block BRONZE_BLOCK = null;
    @ObjectHolder("intertium_block")
    public static final Block INTERTIUM_BLOCK = null;
    @ObjectHolder("steel_block")
    public static final Block STEEL_BLOCK = null;
    @ObjectHolder("gold_titanium_alloy_block")
    public static final Block GOLD_TITANIUM_ALLOY_BLOCK = null;
    @ObjectHolder("adamantium_block")
    public static final Block ADAMANTIUM_BLOCK = null;

    // Ores
    @ObjectHolder("copper_ore")
    public static final Block COPPER_ORE = null;
    @ObjectHolder("tin_ore")
    public static final Block TIN_ORE = null;
    @ObjectHolder("lead_ore")
    public static final Block LEAD_ORE = null;
    @ObjectHolder("silver_ore")
    public static final Block SILVER_ORE = null;
    @ObjectHolder("palladium_ore")
    public static final Block PALLADIUM_ORE = null;
    @ObjectHolder("vibranium_ore")
    public static final Block VIBRANIUM_ORE = null;
    @ObjectHolder("osmium_ore")
    public static final Block OSMIUM_ORE = null;
    @ObjectHolder("uranium_ore")
    public static final Block URANIUM_ORE = null;
    @ObjectHolder("titanium_ore")
    public static final Block TITANIUM_ORE = null;
    @ObjectHolder("iridium_ore")
    public static final Block IRIDIUM_ORE = null;
    @ObjectHolder("uru_ore")
    public static final Block URU_ORE = null;

    @ObjectHolder("white_concrete_slab")
    public static final Block WHITE_CONCRETE_SLAB = null;
    @ObjectHolder("orange_concrete_slab")
    public static final Block ORANGE_CONCRETE_SLAB = null;
    @ObjectHolder("magenta_concrete_slab")
    public static final Block MAGENTA_CONCRETE_SLAB = null;
    @ObjectHolder("light_blue_concrete_slab")
    public static final Block LIGHT_BLUE_CONCRETE_SLAB = null;
    @ObjectHolder("yellow_concrete_slab")
    public static final Block YELLOW_CONCRETE_SLAB = null;
    @ObjectHolder("lime_concrete_slab")
    public static final Block LIME_CONCRETE_SLAB = null;
    @ObjectHolder("pink_concrete_slab")
    public static final Block PINK_CONCRETE_SLAB = null;
    @ObjectHolder("gray_concrete_slab")
    public static final Block GRAY_CONCRETE_SLAB = null;
    @ObjectHolder("light_gray_concrete_slab")
    public static final Block LIGHT_GRAY_CONCRETE_SLAB = null;
    @ObjectHolder("cyan_concrete_slab")
    public static final Block CYAN_CONCRETE_SLAB = null;
    @ObjectHolder("purple_concrete_slab")
    public static final Block PURPLE_CONCRETE_SLAB = null;
    @ObjectHolder("blue_concrete_slab")
    public static final Block BLUE_CONCRETE_SLAB = null;
    @ObjectHolder("brown_concrete_slab")
    public static final Block BROWN_CONCRETE_SLAB = null;
    @ObjectHolder("green_concrete_slab")
    public static final Block GREEN_CONCRETE_SLAB = null;
    @ObjectHolder("red_concrete_slab")
    public static final Block RED_CONCRETE_SLAB = null;
    @ObjectHolder("black_concrete_slab")
    public static final Block BLACK_CONCRETE_SLAB = null;

    @ObjectHolder("white_concrete_stairs")
    public static final Block WHITE_CONCRETE_STAIRS = null;
    @ObjectHolder("orange_concrete_stairs")
    public static final Block ORANGE_CONCRETE_STAIRS = null;
    @ObjectHolder("magenta_concrete_stairs")
    public static final Block MAGENTA_CONCRETE_STAIRS = null;
    @ObjectHolder("light_blue_concrete_stairs")
    public static final Block LIGHT_BLUE_CONCRETE_STAIRS = null;
    @ObjectHolder("yellow_concrete_stairs")
    public static final Block YELLOW_CONCRETE_STAIRS = null;
    @ObjectHolder("lime_concrete_stairs")
    public static final Block LIME_CONCRETE_STAIRS = null;
    @ObjectHolder("pink_concrete_stairs")
    public static final Block PINK_CONCRETE_STAIRS = null;
    @ObjectHolder("gray_concrete_stairs")
    public static final Block GRAY_CONCRETE_STAIRS = null;
    @ObjectHolder("light_gray_concrete_stairs")
    public static final Block LIGHT_GRAY_CONCRETE_STAIRS = null;
    @ObjectHolder("cyan_concrete_stairs")
    public static final Block CYAN_CONCRETE_STAIRS = null;
    @ObjectHolder("purple_concrete_stairs")
    public static final Block PURPLE_CONCRETE_STAIRS = null;
    @ObjectHolder("blue_concrete_stairs")
    public static final Block BLUE_CONCRETE_STAIRS = null;
    @ObjectHolder("brown_concrete_stairs")
    public static final Block BROWN_CONCRETE_STAIRS = null;
    @ObjectHolder("green_concrete_stairs")
    public static final Block GREEN_CONCRETE_STAIRS = null;
    @ObjectHolder("red_concrete_stairs")
    public static final Block RED_CONCRETE_STAIRS = null;
    @ObjectHolder("black_concrete_stairs")
    public static final Block BLACK_CONCRETE_STAIRS = null;

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        // Ores
        ForgeRegistries.BIOMES.getValues().forEach((b) -> {
            addOreFeature(b, COPPER_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.COPPER);
            addOreFeature(b, TIN_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.TIN);
            addOreFeature(b, LEAD_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.LEAD);
            addOreFeature(b, SILVER_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.SILVER);
            addOreFeature(b, PALLADIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.PALLADIUM);
            addOreFeature(b, VIBRANIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.VIBRANIUM);
            addOreFeature(b, OSMIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.OSMIUM);
            addOreFeature(b, URANIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.URANIUM);
            addOreFeature(b, TITANIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.TITANIUM);
            addOreFeature(b, IRIDIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.IRIDIUM);
            addOreFeature(b, URU_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.URU);
        });

        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            try {
                // TESR
                ClientRegistry.bindTileEntitySpecialRenderer(HydraulicPressTileEntity.class, (TileEntityRenderer<HydraulicPressTileEntity>) Class.forName("net.threetag.threecore.client.render.tileentity.tileentity.HydraulicPressTileEntityRenderer").newInstance());
                ClientRegistry.bindTileEntitySpecialRenderer(FluidComposerTileEntity.class, (TileEntityRenderer<FluidComposerTileEntity>) Class.forName("net.threetag.threecore.client.render.tileentity.tileentity.FluidComposerTileEntityRenderer").newInstance());
                ClientRegistry.bindTileEntitySpecialRenderer(StirlingGeneratorTileEntity.class, (TileEntityRenderer<StirlingGeneratorTileEntity>) Class.forName("net.threetag.threecore.client.render.tileentity.tileentity.StirlingGeneratorTileEntityRenderer").newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void addOreFeature(Biome biome, BlockState ore, ThreeCoreCommonConfig.Materials.OreConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ore, config.size.get()), Placement.COUNT_RANGE, new CountRangeConfig(config.count.get(), config.minHeight.get(), 0, config.maxHeight.get() - config.minHeight.get())));
    }

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> e) {
        IForgeRegistry<Block> registry = e.getRegistry();

        registry.register(new ConstructionTableBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "construction_table"));
        registry.register(new GrinderBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "grinder"));
        registry.register(new HydraulicPressBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "hydraulic_press"));
        registry.register(new FluidComposerBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "fluid_composer"));
        registry.register(new CapacitorBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F), CapacitorBlock.Type.NORMAL).setRegistryName(ThreeCore.MODID, "capacitor_block"));
        registry.register(new CapacitorBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F), CapacitorBlock.Type.ADVANCED).setRegistryName(ThreeCore.MODID, "advanced_capacitor_block"));
        registry.register(new StirlingGeneratorBlock(Block.Properties.create(Material.IRON).lightValue(13).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "stirling_generator"));
        registry.register(new SolarPanelBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "solar_panel"));
        registry.register(new EnergyConduitBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F), EnergyConduitBlock.ConduitType.GOLD, 2F / 16F).setRegistryName(ThreeCore.MODID, "gold_conduit"));
        registry.register(new EnergyConduitBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F), EnergyConduitBlock.ConduitType.COPPER, 2F / 16F).setRegistryName(ThreeCore.MODID, "copper_conduit"));
        registry.register(new EnergyConduitBlock(Block.Properties.create(Material.IRON).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F), EnergyConduitBlock.ConduitType.SILVER, 2F / 16F).setRegistryName(ThreeCore.MODID, "silver_conduit"));

        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "copper_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "tin_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(4.0F, 12.0F)).setRegistryName(ThreeCore.MODID, "lead_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "silver_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "palladium_block"));
        registry.register(new VibraniumBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(15.0F, 18.0F)).setRegistryName(ThreeCore.MODID, "vibranium_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "osmium_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "uranium_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(10.0F, 12.0F)).setRegistryName(ThreeCore.MODID, "titanium_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(10.0F, 15.0F)).setRegistryName(ThreeCore.MODID, "iridium_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(10.0F, 15.0F)).setRegistryName(ThreeCore.MODID, "uru_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "bronze_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "intertium_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "steel_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(7.0F, 8.0F)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_block"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(15F, 18.0F)).setRegistryName(ThreeCore.MODID, "adamantium_block"));

        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "copper_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "tin_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "lead_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "silver_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "palladium_ore"));
        registry.register(new VibraniumBlock(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F).lightValue(4)).setRegistryName(ThreeCore.MODID, "vibranium_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "osmium_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(2).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uranium_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "titanium_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "iridium_ore"));
        registry.register(new Block(Block.Properties.create(Material.ROCK).harvestTool(ToolType.PICKAXE).harvestLevel(3).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uru_ore"));

        for (DyeColor color : DyeColor.values()) {
            e.getRegistry().register(new SlabBlock(Block.Properties.create(Material.ROCK, color).hardnessAndResistance(1.8F)).setRegistryName(color.getName() + "_concrete_slab"));
        }

        for (DyeColor color : DyeColor.values()) {
            e.getRegistry().register(new StairsBlock(() -> ForgeRegistries.BLOCKS.getValue(new ResourceLocation("minecraft", color.getName() + "_concrete")).getDefaultState(), Block.Properties.create(Material.ROCK, color).hardnessAndResistance(1.8F)).setRegistryName(color.getName() + "_concrete_stairs"));
        }
    }

    @SubscribeEvent
    public void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(TileEntityType.Builder.create(GrinderTileEntity::new, GRINDER).build(null).setRegistryName(ThreeCore.MODID, "grinder"));
        e.getRegistry().register(TileEntityType.Builder.create(HydraulicPressTileEntity::new, HYDRAULIC_PRESS).build(null).setRegistryName(ThreeCore.MODID, "hydraulic_press"));
        e.getRegistry().register(TileEntityType.Builder.create(FluidComposerTileEntity::new, FLUID_COMPOSER).build(null).setRegistryName(ThreeCore.MODID, "fluid_composer"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new CapacitorBlockTileEntity(CapacitorBlock.Type.NORMAL), CAPACITOR_BLOCK, ADVANCED_CAPACITOR_BLOCK).build(null).setRegistryName(ThreeCore.MODID, "capacitor_block"));
        e.getRegistry().register(TileEntityType.Builder.create(StirlingGeneratorTileEntity::new, STIRLING_GENERATOR).build(null).setRegistryName(ThreeCore.MODID, "stirling_generator"));
        e.getRegistry().register(TileEntityType.Builder.create(SolarPanelTileEntity::new, SOLAR_PANEL).build(null).setRegistryName(ThreeCore.MODID, "solar_panel"));
        e.getRegistry().register(TileEntityType.Builder.create(() -> new EnergyConduitTileEntity(EnergyConduitBlock.ConduitType.GOLD), GOLD_CONDUIT, COPPER_CONDUIT, SILVER_CONDUIT).build(null).setRegistryName(ThreeCore.MODID, "conduit"));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> registry = e.getRegistry();

        registry.register(makeItem(CONSTRUCTION_TABLE, ItemGroup.DECORATIONS));
        registry.register(makeItem(GRINDER, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(makeItem(HYDRAULIC_PRESS, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(makeItem(FLUID_COMPOSER, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(new CapacitorBlockItem(CAPACITOR_BLOCK, new Item.Properties().maxStackSize(1).group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)), ThreeCoreServerConfig.ENERGY.CAPACITOR).setRegistryName(CAPACITOR_BLOCK.getRegistryName()));
        registry.register(new CapacitorBlockItem(ADVANCED_CAPACITOR_BLOCK, new Item.Properties().maxStackSize(1).group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)), ThreeCoreServerConfig.ENERGY.ADVANCED_CAPACITOR).setRegistryName(ADVANCED_CAPACITOR_BLOCK.getRegistryName()));
        registry.register(makeItem(STIRLING_GENERATOR, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(makeItem(SOLAR_PANEL, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(makeItem(GOLD_CONDUIT, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(makeItem(COPPER_CONDUIT, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(makeItem(SILVER_CONDUIT, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));

        registry.register(makeItem(COPPER_BLOCK));
        registry.register(makeItem(TIN_BLOCK));
        registry.register(makeItem(LEAD_BLOCK));
        registry.register(makeItem(SILVER_BLOCK));
        registry.register(makeItem(PALLADIUM_BLOCK));
        registry.register(makeItem(VIBRANIUM_BLOCK, Rarity.RARE));
        registry.register(makeItem(OSMIUM_BLOCK));
        registry.register(makeItem(URANIUM_BLOCK));
        registry.register(makeItem(TITANIUM_BLOCK));
        registry.register(makeItem(IRIDIUM_BLOCK, Rarity.UNCOMMON));
        registry.register(makeItem(URU_BLOCK, Rarity.EPIC));
        registry.register(makeItem(BRONZE_BLOCK));
        registry.register(makeItem(INTERTIUM_BLOCK));
        registry.register(makeItem(STEEL_BLOCK));
        registry.register(makeItem(GOLD_TITANIUM_ALLOY_BLOCK));
        registry.register(makeItem(ADAMANTIUM_BLOCK, Rarity.RARE));

        registry.register(makeItem(COPPER_ORE));
        registry.register(makeItem(TIN_ORE));
        registry.register(makeItem(LEAD_ORE));
        registry.register(makeItem(SILVER_ORE));
        registry.register(makeItem(PALLADIUM_ORE));
        registry.register(makeItem(VIBRANIUM_ORE, Rarity.RARE));
        registry.register(makeItem(OSMIUM_ORE));
        registry.register(makeItem(URANIUM_ORE));
        registry.register(makeItem(TITANIUM_ORE));
        registry.register(makeItem(IRIDIUM_ORE, Rarity.UNCOMMON));
        registry.register(makeItem(URU_ORE, Rarity.EPIC));

        for (DyeColor color : DyeColor.values()) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ThreeCore.MODID, color.getName() + "_concrete_slab"));
            if (block != null)
                e.getRegistry().register(new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }

        for (DyeColor color : DyeColor.values()) {
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ThreeCore.MODID, color.getName() + "_concrete_stairs"));
            if (block != null)
                e.getRegistry().register(new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(Objects.requireNonNull(block.getRegistryName())));
        }
    }

    public static Item makeItem(Block block) {
        return new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(block.getRegistryName());
    }

    public static Item makeItem(Block block, Rarity rarity) {
        return new BlockItem(block, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS).rarity(rarity)).setRegistryName(block.getRegistryName());
    }

    public static Item makeItem(Block block, ItemGroup itemGroup) {
        return new BlockItem(block, new Item.Properties().group(itemGroup)).setRegistryName(block.getRegistryName());
    }

}
