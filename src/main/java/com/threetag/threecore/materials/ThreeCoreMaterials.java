package com.threetag.threecore.materials;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.materials.block.BlockVibranium;
import com.threetag.threecore.util.recipe.RecipeUtil;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.*;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import static net.minecraft.world.biome.Biome.COUNT_RANGE;
import static net.minecraft.world.biome.Biome.createCompositeFeature;

public class ThreeCoreMaterials {

    public ThreeCoreMaterials() {
        MinecraftForge.EVENT_BUS.register(this);
        registerBlocks(null);
        registerItems(null);
        //generateRecipes();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ThreeCoreMaterialsConfig.generateConfig(), ThreeCore.MODID + "-materials.toml");
    }

    private void setup(final FMLCommonSetupEvent e) {
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, COPPER_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.COPPER));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, TIN_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.TIN));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, LEAD_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.LEAD));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, SILVER_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.SILVER));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, PALLADIUM_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.PALLADIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, VIBRANIUM_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.VIBRANIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, OSMIUM_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.OSMIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, URANIUM_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.URANIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, TITANIUM_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.TITANIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, IRIDIUM_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.IRIDIUM));
        ForgeRegistries.BIOMES.getValues().forEach((b) -> addOreFeature(b, URU_ORE.getDefaultState(), ThreeCoreMaterialsConfig.INSTANCE.URU));
    }

    public void addOreFeature(Biome biome, IBlockState ore, ThreeCoreMaterialsConfig.OreConfig config) {
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

    @SubscribeEvent
    public void onTooltip(ItemTooltipEvent e) {
        ItemTags.getCollection().getRegisteredTags().forEach(resourceLocation -> ItemTags.getCollection().get(resourceLocation).getAllElements().forEach(item -> {
            if (e.getItemStack().getItem() == item)
                e.getToolTip().add(new TextComponentString(resourceLocation.toString()));
        }));
    }

    public static ItemGroup ITEM_GROUP = new ItemGroup("threecore_materials") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(IRIDIUM_INGOT);
        }
    };

    // Storage Blocks
    // TODO Harvest levels
    public static final Block COPPER_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "copper_block");
    public static final Block TIN_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "tin_block");
    public static final Block LEAD_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(4.0F, 12.0F)).setRegistryName(ThreeCore.MODID, "lead_block");
    public static final Block SILVER_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "silver_block");
    public static final Block PALLADIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "palladium_block");
    public static final Block VIBRANIUM_BLOCK = new BlockVibranium(Block.Properties.create(Material.ROCK).hardnessAndResistance(15.0F, 18.0F)).setRegistryName(ThreeCore.MODID, "vibranium_block");
    public static final Block OSMIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "osmium_block");
    public static final Block URANIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "uranium_block");
    public static final Block TITANIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10.0F, 12.0F)).setRegistryName(ThreeCore.MODID, "titanium_block");
    public static final Block IRIDIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10.0F, 15.0F)).setRegistryName(ThreeCore.MODID, "iridium_block");
    public static final Block URU_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(10.0F, 15.0F)).setRegistryName(ThreeCore.MODID, "uru_block");
    public static final Block BRONZE_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "bronze_block");
    public static final Block INTERTIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "intertium_block");
    public static final Block STEEL_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 6.0F)).setRegistryName(ThreeCore.MODID, "steel_block");
    public static final Block GOLD_TITANIUM_ALLOY_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(7.0F, 8.0F)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_block");
    public static final Block ADAMANTIUM_BLOCK = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(15F, 18.0F)).setRegistryName(ThreeCore.MODID, "adamantium_block");

    // Ores
    public static final Block COPPER_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "copper_ore");
    public static final Block TIN_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "tin_ore");
    public static final Block LEAD_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "lead_ore");
    public static final Block SILVER_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "silver_ore");
    public static final Block PALLADIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "palladium_ore");
    public static final Block VIBRANIUM_ORE = new BlockVibranium(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "vibranium_ore");
    public static final Block OSMIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "osmium_ore");
    public static final Block URANIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uranium_ore");
    public static final Block TITANIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "titanium_ore");
    public static final Block IRIDIUM_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "iridium_ore");
    public static final Block URU_ORE = new Block(Block.Properties.create(Material.ROCK).hardnessAndResistance(3.0F, 5.0F)).setRegistryName(ThreeCore.MODID, "uru_ore");

    // Ingots
    public static final Item COPPER_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_ingot");
    public static final Item TIN_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_ingot");
    public static final Item LEAD_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_ingot");
    public static final Item SILVER_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_ingot");
    public static final Item PALLADIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_ingot");
    public static final Item VIBRANIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_ingot");
    public static final Item OSMIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_ingot");
    public static final Item URANIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_ingot");
    public static final Item TITANIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_ingot");
    public static final Item IRIDIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_ingot");
    public static final Item URU_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_ingot");
    public static final Item BRONZE_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_ingot");
    public static final Item INTERTIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_ingot");
    public static final Item STEEL_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_ingot");
    public static final Item GOLD_TITANIUM_ALLOY_INGOT = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_ingot");
    public static final Item ADAMANTIUM_INGOT = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_ingot");

    // Ingots
    public static final Item COPPER_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_nugget");
    public static final Item TIN_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_nugget");
    public static final Item LEAD_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_nugget");
    public static final Item SILVER_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_nugget");
    public static final Item PALLADIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_nugget");
    public static final Item VIBRANIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_nugget");
    public static final Item OSMIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_nugget");
    public static final Item URANIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_nugget");
    public static final Item TITANIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_nugget");
    public static final Item IRIDIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_nugget");
    public static final Item URU_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_nugget");
    public static final Item BRONZE_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_nugget");
    public static final Item INTERTIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_nugget");
    public static final Item STEEL_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_nugget");
    public static final Item GOLD_TITANIUM_ALLOY_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_nugget");
    public static final Item ADAMANTIUM_NUGGET = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_nugget");

    // Dusts
    public static final Item IRON_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "iron_dust");
    public static final Item GOLD_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_dust");
    public static final Item COPPER_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_dust");
    public static final Item TIN_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_dust");
    public static final Item LEAD_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_dust");
    public static final Item SILVER_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_dust");
    public static final Item PALLADIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_dust");
    public static final Item VIBRANIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_dust");
    public static final Item OSMIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_dust");
    public static final Item URANIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_dust");
    public static final Item TITANIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_dust");
    public static final Item IRIDIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_dust");
    public static final Item URU_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_dust");
    public static final Item BRONZE_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_dust");
    public static final Item INTERTIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_dust");
    public static final Item STEEL_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_dust");
    public static final Item GOLD_TITANIUM_ALLOY_DUST = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_dust");
    public static final Item ADAMANTIUM_DUST = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_dust");

    // Plates
    public static final Item IRON_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "iron_plate");
    public static final Item GOLD_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_plate");
    public static final Item COPPER_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "copper_plate");
    public static final Item TIN_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "tin_plate");
    public static final Item LEAD_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "lead_plate");
    public static final Item SILVER_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "silver_plate");
    public static final Item PALLADIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "palladium_plate");
    public static final Item VIBRANIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "vibranium_plate");
    public static final Item OSMIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "osmium_plate");
    public static final Item URANIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "uranium_plate");
    public static final Item TITANIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "titanium_plate");
    public static final Item IRIDIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.UNCOMMON)).setRegistryName(ThreeCore.MODID, "iridium_plate");
    public static final Item URU_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.EPIC)).setRegistryName(ThreeCore.MODID, "uru_plate");
    public static final Item BRONZE_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "bronze_plate");
    public static final Item INTERTIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "intertium_plate");
    public static final Item STEEL_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "steel_plate");
    public static final Item GOLD_TITANIUM_ALLOY_PLATE = new Item(new Item.Properties().group(ITEM_GROUP)).setRegistryName(ThreeCore.MODID, "gold_titanium_alloy_plate");
    public static final Item ADAMANTIUM_PLATE = new Item(new Item.Properties().group(ITEM_GROUP).rarity(EnumRarity.RARE)).setRegistryName(ThreeCore.MODID, "adamantium_plate");

    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> e) {
        IForgeRegistry<Block> registry = ForgeRegistries.BLOCKS;

        registry.register(COPPER_BLOCK);
        registry.register(TIN_BLOCK);
        registry.register(LEAD_BLOCK);
        registry.register(SILVER_BLOCK);
        registry.register(PALLADIUM_BLOCK);
        registry.register(VIBRANIUM_BLOCK);
        registry.register(OSMIUM_BLOCK);
        registry.register(URANIUM_BLOCK);
        registry.register(TITANIUM_BLOCK);
        registry.register(IRIDIUM_BLOCK);
        registry.register(URU_BLOCK);
        registry.register(BRONZE_BLOCK);
        registry.register(INTERTIUM_BLOCK);
        registry.register(STEEL_BLOCK);
        registry.register(GOLD_TITANIUM_ALLOY_BLOCK);
        registry.register(ADAMANTIUM_BLOCK);

        registry.register(COPPER_ORE);
        registry.register(TIN_ORE);
        registry.register(LEAD_ORE);
        registry.register(SILVER_ORE);
        registry.register(PALLADIUM_ORE);
        registry.register(VIBRANIUM_ORE);
        registry.register(OSMIUM_ORE);
        registry.register(URANIUM_ORE);
        registry.register(TITANIUM_ORE);
        registry.register(IRIDIUM_ORE);
        registry.register(URU_ORE);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> e) {
        IForgeRegistry<Item> registry = ForgeRegistries.ITEMS;

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

        registry.register(COPPER_INGOT);
        registry.register(TIN_INGOT);
        registry.register(LEAD_INGOT);
        registry.register(SILVER_INGOT);
        registry.register(PALLADIUM_INGOT);
        registry.register(VIBRANIUM_INGOT);
        registry.register(OSMIUM_INGOT);
        registry.register(URANIUM_INGOT);
        registry.register(TITANIUM_INGOT);
        registry.register(IRIDIUM_INGOT);
        registry.register(URU_INGOT);
        registry.register(BRONZE_INGOT);
        registry.register(INTERTIUM_INGOT);
        registry.register(STEEL_INGOT);
        registry.register(GOLD_TITANIUM_ALLOY_INGOT);
        registry.register(ADAMANTIUM_INGOT);

        registry.register(COPPER_NUGGET);
        registry.register(TIN_NUGGET);
        registry.register(LEAD_NUGGET);
        registry.register(SILVER_NUGGET);
        registry.register(PALLADIUM_NUGGET);
        registry.register(VIBRANIUM_NUGGET);
        registry.register(OSMIUM_NUGGET);
        registry.register(URANIUM_NUGGET);
        registry.register(TITANIUM_NUGGET);
        registry.register(IRIDIUM_NUGGET);
        registry.register(URU_NUGGET);
        registry.register(BRONZE_NUGGET);
        registry.register(INTERTIUM_NUGGET);
        registry.register(STEEL_NUGGET);
        registry.register(GOLD_TITANIUM_ALLOY_NUGGET);
        registry.register(ADAMANTIUM_NUGGET);

        registry.register(IRON_DUST);
        registry.register(GOLD_DUST);
        registry.register(COPPER_DUST);
        registry.register(TIN_DUST);
        registry.register(LEAD_DUST);
        registry.register(SILVER_DUST);
        registry.register(PALLADIUM_DUST);
        registry.register(VIBRANIUM_DUST);
        registry.register(OSMIUM_DUST);
        registry.register(URANIUM_DUST);
        registry.register(TITANIUM_DUST);
        registry.register(IRIDIUM_DUST);
        registry.register(URU_DUST);
        registry.register(BRONZE_DUST);
        registry.register(INTERTIUM_DUST);
        registry.register(STEEL_DUST);
        registry.register(GOLD_TITANIUM_ALLOY_DUST);
        registry.register(ADAMANTIUM_DUST);

        registry.register(IRON_PLATE);
        registry.register(GOLD_PLATE);
        registry.register(COPPER_PLATE);
        registry.register(TIN_PLATE);
        registry.register(LEAD_PLATE);
        registry.register(SILVER_PLATE);
        registry.register(PALLADIUM_PLATE);
        registry.register(VIBRANIUM_PLATE);
        registry.register(OSMIUM_PLATE);
        registry.register(URANIUM_PLATE);
        registry.register(TITANIUM_PLATE);
        registry.register(IRIDIUM_PLATE);
        registry.register(URU_PLATE);
        registry.register(BRONZE_PLATE);
        registry.register(INTERTIUM_PLATE);
        registry.register(STEEL_PLATE);
        registry.register(GOLD_TITANIUM_ALLOY_PLATE);
        registry.register(ADAMANTIUM_PLATE);
    }

    public static Item makeItem(Block block) {
        return new ItemBlock(block, new Item.Properties().group(ITEM_GROUP)).setRegistryName(block.getRegistryName());
    }

    public static Item makeItem(Block block, EnumRarity rarity) {
        return new ItemBlock(block, new Item.Properties().group(ITEM_GROUP).rarity(rarity)).setRegistryName(block.getRegistryName());
    }

}
