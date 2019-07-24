package com.threetag.threecore.base;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.ThreeCoreCommonConfig;
import com.threetag.threecore.base.block.GrinderBlock;
import com.threetag.threecore.base.block.HydraulicPressBlock;
import com.threetag.threecore.base.block.VibraniumBlock;
import com.threetag.threecore.base.client.ThreeCoreBaseClient;
import com.threetag.threecore.base.client.gui.GrinderScreen;
import com.threetag.threecore.base.client.gui.HydraulicPressScreen;
import com.threetag.threecore.base.client.renderer.tileentity.HydraulicPressTileEntityRenderer;
import com.threetag.threecore.base.inventory.GrinderContainer;
import com.threetag.threecore.base.inventory.HydraulicPressContainer;
import com.threetag.threecore.base.item.CapacitorItem;
import com.threetag.threecore.base.item.HammerItem;
import com.threetag.threecore.base.recipe.GrinderRecipe;
import com.threetag.threecore.base.recipe.PressingRecipe;
import com.threetag.threecore.base.tileentity.GrinderTileEntity;
import com.threetag.threecore.base.tileentity.HydraulicPressTileEntity;
import com.threetag.threecore.util.item.ItemGroupRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(ThreeCore.MODID)
public class ThreeCoreBase {

    public ThreeCoreBase() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().register(new ThreeCoreBaseClient());
        });
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        //RecipeUtil.generateThreeCoreRecipes();

        // Ores
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, COPPER_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.COPPER));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, TIN_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.TIN));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, LEAD_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.LEAD));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, SILVER_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.SILVER));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, PALLADIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.PALLADIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, VIBRANIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.VIBRANIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, OSMIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.OSMIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, URANIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.URANIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, TITANIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.TITANIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, IRIDIUM_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.IRIDIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, URU_ORE.getDefaultState(), ThreeCoreCommonConfig.MATERIALS.URU));
    }

    public void addOreFeature(Biome biome, BlockState ore, ThreeCoreCommonConfig.Materials.OreConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(Feature.ORE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ore, config.size.get()), Placement.COUNT_RANGE, new CountRangeConfig(config.count.get(), config.minHeight.get(), 0, config.maxHeight.get() - config.minHeight.get())));
    }

    // Machines
    @ObjectHolder("grinder")
    public static final Block GRINDER = null;
    @ObjectHolder("grinder")
    public static final TileEntityType<GrinderTileEntity> GRINDER_TILE_ENTITY = null;
    @ObjectHolder("grinder")
    public static final ContainerType<GrinderContainer> GRINDER_CONTAINER = null;
    @ObjectHolder("grinding")
    public static final IRecipeSerializer<GrinderRecipe> GRINDER_RECIPE_SERIALIZER = null;

    @ObjectHolder("hydraulic_press")
    public static final Block HYDRAULIC_PRESS = null;
    @ObjectHolder("hydraulic_press")
    public static final TileEntityType<HydraulicPressTileEntity> HYDRAULIC_PRESS_TILE_ENTITY = null;
    @ObjectHolder("hydraulic_press")
    public static final ContainerType<HydraulicPressContainer> HYDRAULIC_PRESS_CONTAINER = null;
    @ObjectHolder("pressing")
    public static final IRecipeSerializer<PressingRecipe> PRESSING_RECIPE_SERIALIZER = null;

    // Misc Items
    @ObjectHolder("hammer")
    public static Item HAMMER;
    @ObjectHolder("plate_cast")
    public static Item PLATE_CAST;
    @ObjectHolder("capacitor")
    public static Item CAPACITOR;
    @ObjectHolder("advanced_capacitor")
    public static Item ADVANCED_CAPACITOR;
    @ObjectHolder("circuit")
    public static Item CIRCUIT;
    @ObjectHolder("advanced_circuit")
    public static Item ADVANCED_CIRCUIT;

    // Storage Blocks
    // TODO Harvest levels
    public static Block COPPER_BLOCK;
    public static Block TIN_BLOCK;
    public static Block LEAD_BLOCK;
    public static Block SILVER_BLOCK;
    public static Block PALLADIUM_BLOCK;
    public static Block VIBRANIUM_BLOCK;
    public static Block OSMIUM_BLOCK;
    public static Block URANIUM_BLOCK;
    public static Block TITANIUM_BLOCK;
    public static Block IRIDIUM_BLOCK;
    public static Block URU_BLOCK;
    public static Block BRONZE_BLOCK;
    public static Block INTERTIUM_BLOCK;
    public static Block STEEL_BLOCK;
    public static Block GOLD_TITANIUM_ALLOY_BLOCK;
    public static Block ADAMANTIUM_BLOCK;

    // Ores
    public static Block COPPER_ORE;
    public static Block TIN_ORE;
    public static Block LEAD_ORE;
    public static Block SILVER_ORE;
    public static Block PALLADIUM_ORE;
    public static Block VIBRANIUM_ORE;
    public static Block OSMIUM_ORE;
    public static Block URANIUM_ORE;
    public static Block TITANIUM_ORE;
    public static Block IRIDIUM_ORE;
    public static Block URU_ORE;

    // Ingots
    public static Item COPPER_INGOT;
    public static Item TIN_INGOT;
    public static Item LEAD_INGOT;
    public static Item SILVER_INGOT;
    public static Item PALLADIUM_INGOT;
    public static Item VIBRANIUM_INGOT;
    public static Item OSMIUM_INGOT;
    public static Item URANIUM_INGOT;
    public static Item TITANIUM_INGOT;
    public static Item IRIDIUM_INGOT;
    public static Item URU_INGOT;
    public static Item BRONZE_INGOT;
    public static Item INTERTIUM_INGOT;
    public static Item STEEL_INGOT;
    public static Item GOLD_TITANIUM_ALLOY_INGOT;
    public static Item ADAMANTIUM_INGOT;

    // Ingots
    public static Item COPPER_NUGGET;
    public static Item TIN_NUGGET;
    public static Item LEAD_NUGGET;
    public static Item SILVER_NUGGET;
    public static Item PALLADIUM_NUGGET;
    public static Item VIBRANIUM_NUGGET;
    public static Item OSMIUM_NUGGET;
    public static Item URANIUM_NUGGET;
    public static Item TITANIUM_NUGGET;
    public static Item IRIDIUM_NUGGET;
    public static Item URU_NUGGET;
    public static Item BRONZE_NUGGET;
    public static Item INTERTIUM_NUGGET;
    public static Item STEEL_NUGGET;
    public static Item GOLD_TITANIUM_ALLOY_NUGGET;
    public static Item ADAMANTIUM_NUGGET;

    // Dusts
    public static Item IRON_DUST;
    public static Item GOLD_DUST;
    public static Item COPPER_DUST;
    public static Item TIN_DUST;
    public static Item LEAD_DUST;
    public static Item SILVER_DUST;
    public static Item PALLADIUM_DUST;
    public static Item VIBRANIUM_DUST;
    public static Item OSMIUM_DUST;
    public static Item URANIUM_DUST;
    public static Item TITANIUM_DUST;
    public static Item IRIDIUM_DUST;
    public static Item URU_DUST;
    public static Item BRONZE_DUST;
    public static Item INTERTIUM_DUST;
    public static Item STEEL_DUST;
    public static Item GOLD_TITANIUM_ALLOY_DUST;
    public static Item ADAMANTIUM_DUST;

    // Plates
    public static Item IRON_PLATE;
    public static Item GOLD_PLATE;
    public static Item COPPER_PLATE;
    public static Item TIN_PLATE;
    public static Item LEAD_PLATE;
    public static Item SILVER_PLATE;
    public static Item PALLADIUM_PLATE;
    public static Item VIBRANIUM_PLATE;
    public static Item OSMIUM_PLATE;
    public static Item URANIUM_PLATE;
    public static Item TITANIUM_PLATE;
    public static Item IRIDIUM_PLATE;
    public static Item URU_PLATE;
    public static Item BRONZE_PLATE;
    public static Item INTERTIUM_PLATE;
    public static Item STEEL_PLATE;
    public static Item GOLD_TITANIUM_ALLOY_PLATE;
    public static Item ADAMANTIUM_PLATE;

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> e) {
        IForgeRegistry<Block> registry = e.getRegistry();

        registry.register(new GrinderBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "grinder"));
        registry.register(new HydraulicPressBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "hydraulic_press"));

        registry.register(COPPER_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "copper_block"));
        registry.register(TIN_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "tin_block"));
        registry.register(LEAD_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(4.0F, 12.0F)).setRegistryName(ThreeCore.MODID, "lead_block"));
        registry.register(SILVER_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "silver_block"));
        registry.register(PALLADIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "palladium_block"));
        registry.register(VIBRANIUM_BLOCK = new VibraniumBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(15.0F, 18.0F)).setRegistryName(ThreeCore.MODID, "vibranium_block"));
        registry.register(OSMIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "osmium_block"));
        registry.register(URANIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "uranium_block"));
        registry.register(TITANIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10.0F, 12.0F)).setRegistryName(ThreeCore.MODID, "titanium_block"));
        registry.register(IRIDIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10.0F, 15.0F)).setRegistryName(ThreeCore.MODID, "iridium_block"));
        registry.register(URU_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10.0F, 15.0F)).setRegistryName(ThreeCore.MODID, "uru_block"));
        registry.register(BRONZE_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "bronze_block"));
        registry.register(INTERTIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "intertium_block"));
        registry.register(STEEL_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "steel_block"));
        registry.register(GOLD_TITANIUM_ALLOY_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(7.0F, 8.0F)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_block"));
        registry.register(ADAMANTIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(15F, 18.0F)).setRegistryName(ThreeCore.MODID, "adamantium_block"));

        registry.register(COPPER_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "copper_ore"));
        registry.register(TIN_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "tin_ore"));
        registry.register(LEAD_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "lead_ore"));
        registry.register(SILVER_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "silver_ore"));
        registry.register(PALLADIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "palladium_ore"));
        registry.register(VIBRANIUM_ORE = new VibraniumBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F).lightValue(4)).setRegistryName(ThreeCore.MODID, "vibranium_ore"));
        registry.register(OSMIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "osmium_ore"));
        registry.register(URANIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uranium_ore"));
        registry.register(TITANIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "titanium_ore"));
        registry.register(IRIDIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "iridium_ore"));
        registry.register(URU_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uru_ore"));
    }

    @SubscribeEvent
    public void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(TileEntityType.Builder.create(GrinderTileEntity::new, GRINDER).build(null).setRegistryName(ThreeCore.MODID, "grinder"));
        e.getRegistry().register(TileEntityType.Builder.create(HydraulicPressTileEntity::new, HYDRAULIC_PRESS).build(null).setRegistryName(ThreeCore.MODID, "hydraulic_press"));
    }

    @SubscribeEvent
    public void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> e) {
        e.getRegistry().register(new ContainerType<>(GrinderContainer::new).setRegistryName(ThreeCore.MODID, "grinder"));
        e.getRegistry().register(new ContainerType<>(HydraulicPressContainer::new).setRegistryName(ThreeCore.MODID, "hydraulic_press"));
    }

    @SubscribeEvent
    public void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> e) {
        e.getRegistry().register(new GrinderRecipe.Serializer().setRegistryName(ThreeCore.MODID, "grinding"));
        e.getRegistry().register(new PressingRecipe.Serializer().setRegistryName(ThreeCore.MODID, "pressing"));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> registry = e.getRegistry();

        registry.register(makeItem(GRINDER, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(makeItem(HYDRAULIC_PRESS, ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)));
        registry.register(new HammerItem(4.5F, -2.75F, ItemTier.IRON, new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1).maxDamage(16)).setRegistryName(ThreeCore.MODID, "hammer"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY))).setRegistryName(ThreeCore.MODID, "plate_cast"));
        registry.register(new CapacitorItem(new Item.Properties().group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)).maxStackSize(1), 40000, 100).setRegistryName(ThreeCore.MODID, "capacitor"));
        registry.register(new CapacitorItem(new Item.Properties().group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY)).maxStackSize(1), 100000, 200).setRegistryName(ThreeCore.MODID, "advanced_capacitor"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY))).setRegistryName(ThreeCore.MODID, "circuit"));
        registry.register(new Item(new Item.Properties().group(ItemGroupRegistry.getItemGroup(ItemGroupRegistry.TECHNOLOGY))).setRegistryName(ThreeCore.MODID, "advanced_circuit"));

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

        registry.register(COPPER_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_ingot"));
        registry.register(TIN_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_ingot"));
        registry.register(LEAD_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_ingot"));
        registry.register(SILVER_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_ingot"));
        registry.register(PALLADIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_ingot"));
        registry.register(VIBRANIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_ingot"));
        registry.register(OSMIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_ingot"));
        registry.register(URANIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_ingot"));
        registry.register(TITANIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_ingot"));
        registry.register(IRIDIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_ingot"));
        registry.register(URU_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_ingot"));
        registry.register(BRONZE_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_ingot"));
        registry.register(INTERTIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_ingot"));
        registry.register(STEEL_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_ingot"));
        registry.register(GOLD_TITANIUM_ALLOY_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_ingot"));
        registry.register(ADAMANTIUM_INGOT = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_ingot"));

        registry.register(COPPER_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_nugget"));
        registry.register(TIN_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_nugget"));
        registry.register(LEAD_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_nugget"));
        registry.register(SILVER_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_nugget"));
        registry.register(PALLADIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_nugget"));
        registry.register(VIBRANIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_nugget"));
        registry.register(OSMIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_nugget"));
        registry.register(URANIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_nugget"));
        registry.register(TITANIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_nugget"));
        registry.register(IRIDIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_nugget"));
        registry.register(URU_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_nugget"));
        registry.register(BRONZE_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_nugget"));
        registry.register(INTERTIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_nugget"));
        registry.register(STEEL_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_nugget"));
        registry.register(GOLD_TITANIUM_ALLOY_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_nugget"));
        registry.register(ADAMANTIUM_NUGGET = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_nugget"));

        registry.register(IRON_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "iron_dust"));
        registry.register(GOLD_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_dust"));
        registry.register(COPPER_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_dust"));
        registry.register(TIN_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_dust"));
        registry.register(LEAD_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_dust"));
        registry.register(SILVER_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_dust"));
        registry.register(PALLADIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_dust"));
        registry.register(VIBRANIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_dust"));
        registry.register(OSMIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_dust"));
        registry.register(URANIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_dust"));
        registry.register(TITANIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_dust"));
        registry.register(IRIDIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_dust"));
        registry.register(URU_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_dust"));
        registry.register(BRONZE_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_dust"));
        registry.register(INTERTIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_dust"));
        registry.register(STEEL_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_dust"));
        registry.register(GOLD_TITANIUM_ALLOY_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_dust"));
        registry.register(ADAMANTIUM_DUST = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_dust"));

        registry.register(IRON_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "iron_plate"));
        registry.register(GOLD_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_plate"));
        registry.register(COPPER_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "copper_plate"));
        registry.register(TIN_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "tin_plate"));
        registry.register(LEAD_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "lead_plate"));
        registry.register(SILVER_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "silver_plate"));
        registry.register(PALLADIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "palladium_plate"));
        registry.register(VIBRANIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_plate"));
        registry.register(OSMIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "osmium_plate"));
        registry.register(URANIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "uranium_plate"));
        registry.register(TITANIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "titanium_plate"));
        registry.register(IRIDIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_plate"));
        registry.register(URU_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_plate"));
        registry.register(BRONZE_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "bronze_plate"));
        registry.register(INTERTIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "intertium_plate"));
        registry.register(STEEL_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "steel_plate"));
        registry.register(GOLD_TITANIUM_ALLOY_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_plate"));
        registry.register(ADAMANTIUM_PLATE = new Item(new Item.Properties().group(ItemGroup.MATERIALS).rarity(Rarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_plate"));
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
