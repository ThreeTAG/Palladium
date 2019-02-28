package com.threetag.threecore.base;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.ThreeCoreCommonConfig;
import com.threetag.threecore.base.block.BlockVibranium;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import static net.minecraft.world.biome.Biome.COUNT_RANGE;
import static net.minecraft.world.biome.Biome.createCompositeFeature;

public class ThreeCoreBase {

    public ThreeCoreBase() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent e) {
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

    public void addOreFeature(Biome biome, IBlockState ore, ThreeCoreCommonConfig.Materials.OreConfig config) {
        biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, createCompositeFeature(Feature.MINABLE, new MinableConfig(MinableConfig.IS_ROCK, ore, config.size.get()), COUNT_RANGE, new CountRangeConfig(config.count.get(), config.minHeight.get(), 0, config.maxHeight.get() - config.minHeight.get())));
    }

    public static void generateRecipes() {
        RecipeUtil.addShapelessRecipe("copper_ingot_from_copper_block", "copper_ingot", new ItemStack(COPPER_INGOT, 9), "forge:storage_blocks/copper");
        RecipeUtil.addShapedRecipe("copper_block", new ItemStack(COPPER_BLOCK), "###", "###", "###", '#', "forge:ingots/copper");
        RecipeUtil.addShapelessRecipe("copper_nugget", new ItemStack(COPPER_NUGGET, 9), "forge:ingots/copper");
        RecipeUtil.addShapedRecipe("copper_ingot_from_copper_nuggets", "copper_ingot", new ItemStack(COPPER_INGOT), "###", "###", "###", '#', "forge:nuggets/copper");
        RecipeUtil.addSmeltingRecipe("copper_ingot", new ItemStack(COPPER_INGOT), "forge:ores/copper", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("tin_ingot_from_tin_block", "tin_ingot", new ItemStack(TIN_INGOT, 9), "forge:storage_blocks/tin");
        RecipeUtil.addShapedRecipe("tin_block", new ItemStack(TIN_BLOCK), "###", "###", "###", '#', "forge:ingots/tin");
        RecipeUtil.addShapelessRecipe("tin_nugget", new ItemStack(TIN_NUGGET, 9), "forge:ingots/tin");
        RecipeUtil.addShapedRecipe("tin_ingot_from_tin_nuggets", "tin_ingot", new ItemStack(TIN_INGOT), "###", "###", "###", '#', "forge:nuggets/tin");
        RecipeUtil.addSmeltingRecipe("tin_ingot", new ItemStack(TIN_INGOT), "forge:ores/tin", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("lead_ingot_from_lead_block", "lead_ingot", new ItemStack(LEAD_INGOT, 9), "forge:storage_blocks/lead");
        RecipeUtil.addShapedRecipe("lead_block", new ItemStack(LEAD_BLOCK), "###", "###", "###", '#', "forge:ingots/lead");
        RecipeUtil.addShapelessRecipe("lead_nugget", new ItemStack(LEAD_NUGGET, 9), "forge:ingots/lead");
        RecipeUtil.addShapedRecipe("lead_ingot_from_lead_nuggets", "lead_ingot", new ItemStack(LEAD_INGOT), "###", "###", "###", '#', "forge:nuggets/lead");
        RecipeUtil.addSmeltingRecipe("lead_ingot", new ItemStack(LEAD_INGOT), "forge:ores/lead", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("silver_ingot_from_silver_block", "silver_ingot", new ItemStack(SILVER_INGOT, 9), "forge:storage_blocks/silver");
        RecipeUtil.addShapedRecipe("silver_block", new ItemStack(SILVER_BLOCK), "###", "###", "###", '#', "forge:ingots/silver");
        RecipeUtil.addShapelessRecipe("silver_nugget", new ItemStack(SILVER_NUGGET, 9), "forge:ingots/silver");
        RecipeUtil.addShapedRecipe("silver_ingot_from_silver_nuggets", "silver_ingot", new ItemStack(SILVER_INGOT), "###", "###", "###", '#', "forge:nuggets/silver");
        RecipeUtil.addSmeltingRecipe("silver_ingot", new ItemStack(SILVER_INGOT), "forge:ores/silver", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("palladium_ingot_from_palladium_block", "palladium_ingot", new ItemStack(PALLADIUM_INGOT, 9), "forge:storage_blocks/palladium");
        RecipeUtil.addShapedRecipe("palladium_block", new ItemStack(PALLADIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/palladium");
        RecipeUtil.addShapelessRecipe("palladium_nugget", new ItemStack(PALLADIUM_NUGGET, 9), "forge:ingots/palladium");
        RecipeUtil.addShapedRecipe("palladium_ingot_from_palladium_nuggets", "palladium_ingot", new ItemStack(PALLADIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/palladium");
        RecipeUtil.addSmeltingRecipe("palladium_ingot", new ItemStack(PALLADIUM_INGOT), "forge:ores/palladium", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("vibranium_ingot_from_vibranium_block", "vibranium_ingot", new ItemStack(VIBRANIUM_INGOT, 9), "forge:storage_blocks/vibranium");
        RecipeUtil.addShapedRecipe("vibranium_block", new ItemStack(VIBRANIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/vibranium");
        RecipeUtil.addShapelessRecipe("vibranium_nugget", new ItemStack(VIBRANIUM_NUGGET, 9), "forge:ingots/vibranium");
        RecipeUtil.addShapedRecipe("vibranium_ingot_from_vibranium_nuggets", "vibranium_ingot", new ItemStack(VIBRANIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/vibranium");
        RecipeUtil.addSmeltingRecipe("vibranium_ingot", new ItemStack(VIBRANIUM_INGOT), "forge:ores/vibranium", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("osmium_ingot_from_osmium_block", "osmium_ingot", new ItemStack(OSMIUM_INGOT, 9), "forge:storage_blocks/osmium");
        RecipeUtil.addShapedRecipe("osmium_block", new ItemStack(OSMIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/osmium");
        RecipeUtil.addShapelessRecipe("osmium_nugget", new ItemStack(OSMIUM_NUGGET, 9), "forge:ingots/osmium");
        RecipeUtil.addShapedRecipe("osmium_ingot_from_osmium_nuggets", "osmium_ingot", new ItemStack(OSMIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/osmium");
        RecipeUtil.addSmeltingRecipe("osmium_ingot", new ItemStack(OSMIUM_INGOT), "forge:ores/osmium", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("uranium_ingot_from_uranium_block", "uranium_ingot", new ItemStack(URANIUM_INGOT, 9), "forge:storage_blocks/uranium");
        RecipeUtil.addShapedRecipe("uranium_block", new ItemStack(URANIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/uranium");
        RecipeUtil.addShapelessRecipe("uranium_nugget", new ItemStack(URANIUM_NUGGET, 9), "forge:ingots/uranium");
        RecipeUtil.addShapedRecipe("uranium_ingot_from_uranium_nuggets", "uranium_ingot", new ItemStack(URANIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/uranium");
        RecipeUtil.addSmeltingRecipe("uranium_ingot", new ItemStack(URANIUM_INGOT), "forge:ores/uranium", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("titanium_ingot_from_titanium_block", "titanium_ingot", new ItemStack(TITANIUM_INGOT, 9), "forge:storage_blocks/titanium");
        RecipeUtil.addShapedRecipe("titanium_block", new ItemStack(TITANIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/titanium");
        RecipeUtil.addShapelessRecipe("titanium_nugget", new ItemStack(TITANIUM_NUGGET, 9), "forge:ingots/titanium");
        RecipeUtil.addShapedRecipe("titanium_ingot_from_titanium_nuggets", "titanium_ingot", new ItemStack(TITANIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/titanium");
        RecipeUtil.addSmeltingRecipe("titanium_ingot", new ItemStack(TITANIUM_INGOT), "forge:ores/titanium", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("iridium_ingot_from_iridium_block", "iridium_ingot", new ItemStack(IRIDIUM_INGOT, 9), "forge:storage_blocks/iridium");
        RecipeUtil.addShapedRecipe("iridium_block", new ItemStack(IRIDIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/iridium");
        RecipeUtil.addShapelessRecipe("iridium_nugget", new ItemStack(IRIDIUM_NUGGET, 9), "forge:ingots/iridium");
        RecipeUtil.addShapedRecipe("iridium_ingot_from_iridium_nuggets", "iridium_ingot", new ItemStack(IRIDIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/iridium");
        RecipeUtil.addSmeltingRecipe("iridium_ingot", new ItemStack(IRIDIUM_INGOT), "forge:ores/iridium", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("uru_ingot_from_uru_block", "uru_ingot", new ItemStack(URU_INGOT, 9), "forge:storage_blocks/uru");
        RecipeUtil.addShapedRecipe("uru_block", new ItemStack(URU_BLOCK), "###", "###", "###", '#', "forge:ingots/uru");
        RecipeUtil.addShapelessRecipe("uru_nugget", new ItemStack(URU_NUGGET, 9), "forge:ingots/uru");
        RecipeUtil.addShapedRecipe("uru_ingot_from_uru_nuggets", "uru_ingot", new ItemStack(URU_INGOT), "###", "###", "###", '#', "forge:nuggets/uru");
        RecipeUtil.addSmeltingRecipe("uru_ingot", new ItemStack(URU_INGOT), "forge:ores/uru", 0.7F, 200);
        RecipeUtil.addShapelessRecipe("bronze_ingot_from_bronze_block", "bronze_ingot", new ItemStack(BRONZE_INGOT, 9), "forge:storage_blocks/bronze");
        RecipeUtil.addShapedRecipe("bronze_block", new ItemStack(BRONZE_BLOCK), "###", "###", "###", '#', "forge:ingots/bronze");
        RecipeUtil.addShapelessRecipe("bronze_nugget", new ItemStack(BRONZE_NUGGET, 9), "forge:ingots/bronze");
        RecipeUtil.addShapedRecipe("bronze_ingot_from_bronze_nuggets", "bronze_ingot", new ItemStack(BRONZE_INGOT), "###", "###", "###", '#', "forge:nuggets/bronze");
        RecipeUtil.addShapelessRecipe("intertium_ingot_from_intertium_block", "intertium_ingot", new ItemStack(INTERTIUM_INGOT, 9), "forge:storage_blocks/intertium");
        RecipeUtil.addShapedRecipe("intertium_block", new ItemStack(INTERTIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/intertium");
        RecipeUtil.addShapelessRecipe("intertium_nugget", new ItemStack(INTERTIUM_NUGGET, 9), "forge:ingots/intertium");
        RecipeUtil.addShapedRecipe("intertium_ingot_from_intertium_nuggets", "intertium_ingot", new ItemStack(INTERTIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/intertium");
        RecipeUtil.addShapelessRecipe("steel_ingot_from_steel_block", "steel_ingot", new ItemStack(STEEL_INGOT, 9), "forge:storage_blocks/steel");
        RecipeUtil.addShapedRecipe("steel_block", new ItemStack(STEEL_BLOCK), "###", "###", "###", '#', "forge:ingots/steel");
        RecipeUtil.addShapelessRecipe("steel_nugget", new ItemStack(STEEL_NUGGET, 9), "forge:ingots/steel");
        RecipeUtil.addShapedRecipe("steel_ingot_from_steel_nuggets", "steel_ingot", new ItemStack(STEEL_INGOT), "###", "###", "###", '#', "forge:nuggets/steel");
        RecipeUtil.addShapelessRecipe("gold_titanium_alloy_ingot_from_gold_titanium_alloy_block", "gold_titanium_alloy_ingot", new ItemStack(GOLD_TITANIUM_ALLOY_INGOT, 9), "forge:storage_blocks/gold_titanium_alloy");
        RecipeUtil.addShapedRecipe("gold_titanium_alloy_block", new ItemStack(GOLD_TITANIUM_ALLOY_BLOCK), "###", "###", "###", '#', "forge:ingots/gold_titanium_alloy");
        RecipeUtil.addShapelessRecipe("gold_titanium_alloy_nugget", new ItemStack(GOLD_TITANIUM_ALLOY_NUGGET, 9), "forge:ingots/gold_titanium_alloy");
        RecipeUtil.addShapedRecipe("gold_titanium_alloy_ingot_from_gold_titanium_alloy_nuggets", "gold_titanium_alloy_ingot", new ItemStack(GOLD_TITANIUM_ALLOY_INGOT), "###", "###", "###", '#', "forge:nuggets/gold_titanium_alloy");
        RecipeUtil.addShapelessRecipe("adamantium_ingot_from_adamantium_block", "adamantium_ingot", new ItemStack(ADAMANTIUM_INGOT, 9), "forge:storage_blocks/adamantium");
        RecipeUtil.addShapedRecipe("adamantium_block", new ItemStack(ADAMANTIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/adamantium");
        RecipeUtil.addShapelessRecipe("adamantium_nugget", new ItemStack(ADAMANTIUM_NUGGET, 9), "forge:ingots/adamantium");
        RecipeUtil.addShapedRecipe("adamantium_ingot_from_adamantium_nuggets", "adamantium_ingot", new ItemStack(ADAMANTIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/adamantium");

        // Alloy
        RecipeUtil.addShapelessRecipe("bronze_dust", new ItemStack(BRONZE_DUST, 4), "forge:dusts/copper", "forge:dusts/copper", "forge:dusts/copper", "forge:dusts/tin");
        RecipeUtil.addShapelessRecipe("intertium_dust", new ItemStack(INTERTIUM_DUST, 3), "forge:dusts/osmium", "forge:dusts/iron", "forge:dusts/iron");
        RecipeUtil.addShapelessRecipe("adamantium_dust", new ItemStack(ADAMANTIUM_DUST, 3), "forge:dusts/vibranium", "forge:dusts/steel", "forge:dusts/steel");
        RecipeUtil.addShapelessRecipe("steel_dust", new ItemStack(STEEL_DUST, 3), "forge:dusts/iron", "forge:dusts/coal", "forge:dusts/coal", "forge:dusts/coal", "forge:dusts/coal");
        RecipeUtil.addShapelessRecipe("gold_titanium_alloy_dust", new ItemStack(GOLD_TITANIUM_ALLOY_DUST, 3), "forge:dusts/gold", "forge:dusts/titanium", "forge:dusts/titanium");
    }

//    @SubscribeEvent
//    public void onTooltip(ItemTooltipEvent e) {
//        ItemTags.getCollection().getRegisteredTags().forEach(resourceLocation -> ItemTags.getCollection().get(resourceLocation).getAllElements().forEach(item -> {
//            if (e.getItemStack().getItem() == item)
//                e.getToolTip().add(new TextComponentString(resourceLocation.toString()));
//        }));
//    }

    public static ItemGroup ITEM_GROUP = new ItemGroup("threecore_materials") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IRIDIUM_INGOT);
        }
    };

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
    public void registerItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> registry = e.getRegistry();

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
