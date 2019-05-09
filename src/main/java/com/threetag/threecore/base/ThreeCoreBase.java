package com.threetag.threecore.base;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.ThreeCoreCommonConfig;
import com.threetag.threecore.base.block.BlockGrinder;
import com.threetag.threecore.base.block.BlockVibranium;
import com.threetag.threecore.base.item.ItemCapacitor;
import com.threetag.threecore.base.network.MessageSyncTileEntity;
import com.threetag.threecore.base.recipe.GrinderRecipe;
import com.threetag.threecore.base.tileentity.TileEntityGrinder;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.item.*;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.xml.crypto.Data;

import static net.minecraft.world.biome.Biome.COUNT_RANGE;
import static net.minecraft.world.biome.Biome.createCompositeFeature;

public class ThreeCoreBase {

    public ThreeCoreBase() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
        //RecipeUtil.generateThreeCoreRecipes();

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

        RecipeSerializers.register(GrinderRecipe.SERIALIZER);

        ThreeCore.registerMessage(MessageSyncTileEntity.class, MessageSyncTileEntity::toBytes, MessageSyncTileEntity::new, MessageSyncTileEntity::handle);
    }

    public void addOreFeature(Biome biome, IBlockState ore, ThreeCoreCommonConfig.Materials.OreConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, ore, config.size.get()), COUNT_RANGE, new CountRangeConfig(config.count.get(), config.minHeight.get(), 0, config.maxHeight.get() - config.minHeight.get())));
    }

    public static ItemGroup ITEM_GROUP = new ItemGroup("threecore_materials") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IRIDIUM_INGOT);
        }
    };

    // Machines
    public static Block GRINDER;
    public static TileEntityType<?> TYPE_GRINDER;

    // Misc Items
    public static Item CAPACITOR;
    public static Item ADVANCED_CAPACITOR;
    public static Item CIRCUIT;
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

        registry.register(GRINDER = new BlockGrinder(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "grinder"));

        registry.register(COPPER_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "copper_block"));
        registry.register(TIN_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "tin_block"));
        registry.register(LEAD_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(4.0F, 12.0F)).setRegistryName(ThreeCore.MODID, "lead_block"));
        registry.register(SILVER_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "silver_block"));
        registry.register(PALLADIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "palladium_block"));
        registry.register(VIBRANIUM_BLOCK = new BlockVibranium(Block.Properties.create(Material.ROCK).hardnessAndResistance(15.0F, 18.0F)).setRegistryName(ThreeCore.MODID, "vibranium_block"));
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
        registry.register(VIBRANIUM_ORE = new BlockVibranium(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "vibranium_ore"));
        registry.register(OSMIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "osmium_ore"));
        registry.register(URANIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uranium_ore"));
        registry.register(TITANIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "titanium_ore"));
        registry.register(IRIDIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "iridium_ore"));
        registry.register(URU_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uru_ore"));
    }

    @SubscribeEvent
    public void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> e) {
        e.getRegistry().register(TYPE_GRINDER = TileEntityType.Builder.create(TileEntityGrinder::new).build(null).setRegistryName(ThreeCore.MODID, "grinder"));
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> registry = e.getRegistry();

        registry.register(makeItem(GRINDER));
        registry.register(CAPACITOR = new ItemCapacitor(new Item.Properties().group(ITEM_GROUP).maxStackSize(1), 40000, 100).setRegistryName(ThreeCore.MODID, "capacitor"));
        registry.register(ADVANCED_CAPACITOR = new ItemCapacitor(new Item.Properties().group(ITEM_GROUP).maxStackSize(1), 100000, 200).setRegistryName(ThreeCore.MODID, "advanced_capacitor"));
        registry.register(CIRCUIT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "circuit"));
        registry.register(ADVANCED_CIRCUIT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "advanced_circuit"));

        registry.register(makeItem(COPPER_BLOCK));
        registry.register(makeItem(TIN_BLOCK));
        registry.register(makeItem(LEAD_BLOCK));
        registry.register(makeItem(SILVER_BLOCK));
        registry.register(makeItem(PALLADIUM_BLOCK));
        registry.register(makeItem(VIBRANIUM_BLOCK, EnumRarity.RARE));
        registry.register(makeItem(OSMIUM_BLOCK));
        registry.register(makeItem(URANIUM_BLOCK));
        registry.register(makeItem(TITANIUM_BLOCK));
        registry.register(makeItem(IRIDIUM_BLOCK, EnumRarity.UNCOMMON));
        registry.register(makeItem(URU_BLOCK, EnumRarity.EPIC));
        registry.register(makeItem(BRONZE_BLOCK));
        registry.register(makeItem(INTERTIUM_BLOCK));
        registry.register(makeItem(STEEL_BLOCK));
        registry.register(makeItem(GOLD_TITANIUM_ALLOY_BLOCK));
        registry.register(makeItem(ADAMANTIUM_BLOCK, EnumRarity.RARE));

        registry.register(makeItem(COPPER_ORE));
        registry.register(makeItem(TIN_ORE));
        registry.register(makeItem(LEAD_ORE));
        registry.register(makeItem(SILVER_ORE));
        registry.register(makeItem(PALLADIUM_ORE));
        registry.register(makeItem(VIBRANIUM_ORE, EnumRarity.RARE));
        registry.register(makeItem(OSMIUM_ORE));
        registry.register(makeItem(URANIUM_ORE));
        registry.register(makeItem(TITANIUM_ORE));
        registry.register(makeItem(IRIDIUM_ORE, EnumRarity.UNCOMMON));
        registry.register(makeItem(URU_ORE, EnumRarity.EPIC));

        registry.register(COPPER_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_ingot"));
        registry.register(TIN_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_ingot"));
        registry.register(LEAD_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_ingot"));
        registry.register(SILVER_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_ingot"));
        registry.register(PALLADIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_ingot"));
        registry.register(VIBRANIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_ingot"));
        registry.register(OSMIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_ingot"));
        registry.register(URANIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_ingot"));
        registry.register(TITANIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_ingot"));
        registry.register(IRIDIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_ingot"));
        registry.register(URU_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_ingot"));
        registry.register(BRONZE_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_ingot"));
        registry.register(INTERTIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_ingot"));
        registry.register(STEEL_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_ingot"));
        registry.register(GOLD_TITANIUM_ALLOY_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_ingot"));
        registry.register(ADAMANTIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_ingot"));

        registry.register(COPPER_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_nugget"));
        registry.register(TIN_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_nugget"));
        registry.register(LEAD_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_nugget"));
        registry.register(SILVER_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_nugget"));
        registry.register(PALLADIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_nugget"));
        registry.register(VIBRANIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_nugget"));
        registry.register(OSMIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_nugget"));
        registry.register(URANIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_nugget"));
        registry.register(TITANIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_nugget"));
        registry.register(IRIDIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_nugget"));
        registry.register(URU_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_nugget"));
        registry.register(BRONZE_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_nugget"));
        registry.register(INTERTIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_nugget"));
        registry.register(STEEL_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_nugget"));
        registry.register(GOLD_TITANIUM_ALLOY_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_nugget"));
        registry.register(ADAMANTIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_nugget"));

        registry.register(IRON_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "iron_dust"));
        registry.register(GOLD_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_dust"));
        registry.register(COPPER_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_dust"));
        registry.register(TIN_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_dust"));
        registry.register(LEAD_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_dust"));
        registry.register(SILVER_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_dust"));
        registry.register(PALLADIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_dust"));
        registry.register(VIBRANIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_dust"));
        registry.register(OSMIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_dust"));
        registry.register(URANIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_dust"));
        registry.register(TITANIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_dust"));
        registry.register(IRIDIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_dust"));
        registry.register(URU_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_dust"));
        registry.register(BRONZE_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_dust"));
        registry.register(INTERTIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_dust"));
        registry.register(STEEL_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_dust"));
        registry.register(GOLD_TITANIUM_ALLOY_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_dust"));
        registry.register(ADAMANTIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_dust"));

        registry.register(IRON_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "iron_plate"));
        registry.register(GOLD_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_plate"));
        registry.register(COPPER_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_plate"));
        registry.register(TIN_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_plate"));
        registry.register(LEAD_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_plate"));
        registry.register(SILVER_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_plate"));
        registry.register(PALLADIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_plate"));
        registry.register(VIBRANIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_plate"));
        registry.register(OSMIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_plate"));
        registry.register(URANIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_plate"));
        registry.register(TITANIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_plate"));
        registry.register(IRIDIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_plate"));
        registry.register(URU_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_plate"));
        registry.register(BRONZE_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_plate"));
        registry.register(INTERTIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_plate"));
        registry.register(STEEL_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_plate"));
        registry.register(GOLD_TITANIUM_ALLOY_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_plate"));
        registry.register(ADAMANTIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_plate"));
    }

    public static Item makeItem(Block block) {
        return new ItemBlock(block, new Item.Properties().group(ITEM_GROUP)).setRegistryName(block.getRegistryName());
    }

    public static Item makeItem(Block block, EnumRarity rarity) {
        return new ItemBlock(block, new Item.Properties().group(ITEM_GROUP).rarity(rarity)).setRegistryName(block.getRegistryName());
    }

}
