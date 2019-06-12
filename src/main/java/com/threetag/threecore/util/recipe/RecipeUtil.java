package com.threetag.threecore.util.recipe;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.base.ThreeCoreBase;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class RecipeUtil {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File RECIPE_DIR = null;

    public static void init() {
        CraftingHelper.register(new ResourceLocation(ThreeCore.MODID, "tag_exists"), new RecipeConditionTagExists());
    }

    public static <T extends IRecipe<?>> IRecipeType<T> register(final ResourceLocation name) {
        return Registry.register(Registry.RECIPE_TYPE, name, new IRecipeType<T>() {
            public String toString() {
                return name.toString();
            }
        });
    }

    public static void generateThreeCoreRecipes() {
        RecipeUtil.addShapelessRecipe("copper_ingot_from_copper_block", "copper_ingot", new ItemStack(ThreeCoreBase.COPPER_INGOT, 9), "forge:storage_blocks/copper");
        RecipeUtil.addShapedRecipe("copper_block", new ItemStack(ThreeCoreBase.COPPER_BLOCK), "###", "###", "###", '#', "forge:ingots/copper");
        RecipeUtil.addShapelessRecipe("copper_nugget", new ItemStack(ThreeCoreBase.COPPER_NUGGET, 9), "forge:ingots/copper");
        RecipeUtil.addShapedRecipe("copper_ingot_from_copper_nuggets", "copper_ingot", new ItemStack(ThreeCoreBase.COPPER_INGOT), "###", "###", "###", '#', "forge:nuggets/copper");
        RecipeUtil.addSmeltingRecipe("copper_ingot", new ItemStack(ThreeCoreBase.COPPER_INGOT), "forge:ores/copper", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("copper_dust_from_copper_ore", "forge:dusts/copper#2", "forge:ores/copper", 0.7F, 400);
        RecipeUtil.addGrinderRecipe("copper_dust_from_copper_ingot", "forge:dusts/copper", "forge:ingots/copper", 0F, 200);
        RecipeUtil.addSmeltingRecipe("copper_ingot_from_copper_dust", new ItemStack(ThreeCoreBase.COPPER_INGOT), "forge:dusts/copper", 0F, 100);

        RecipeUtil.addShapelessRecipe("tin_ingot_from_tin_block", "tin_ingot", new ItemStack(ThreeCoreBase.TIN_INGOT, 9), "forge:storage_blocks/tin");
        RecipeUtil.addShapedRecipe("tin_block", new ItemStack(ThreeCoreBase.TIN_BLOCK), "###", "###", "###", '#', "forge:ingots/tin");
        RecipeUtil.addShapelessRecipe("tin_nugget", new ItemStack(ThreeCoreBase.TIN_NUGGET, 9), "forge:ingots/tin");
        RecipeUtil.addShapedRecipe("tin_ingot_from_tin_nuggets", "tin_ingot", new ItemStack(ThreeCoreBase.TIN_INGOT), "###", "###", "###", '#', "forge:nuggets/tin");
        RecipeUtil.addSmeltingRecipe("tin_ingot", new ItemStack(ThreeCoreBase.TIN_INGOT), "forge:ores/tin", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("tin_dust_from_tin_ore", "forge:dusts/tin#2", "forge:ores/tin", 0.7F, 400);
        RecipeUtil.addGrinderRecipe("tin_dust_from_tin_ingot", "forge:dusts/tin", "forge:ingots/tin", 0F, 200);
        RecipeUtil.addSmeltingRecipe("tin_ingot_from_tin_dust", new ItemStack(ThreeCoreBase.TIN_INGOT), "forge:dusts/tin", 0F, 100);

        RecipeUtil.addShapelessRecipe("lead_ingot_from_lead_block", "lead_ingot", new ItemStack(ThreeCoreBase.LEAD_INGOT, 9), "forge:storage_blocks/lead");
        RecipeUtil.addShapedRecipe("lead_block", new ItemStack(ThreeCoreBase.LEAD_BLOCK), "###", "###", "###", '#', "forge:ingots/lead");
        RecipeUtil.addShapelessRecipe("lead_nugget", new ItemStack(ThreeCoreBase.LEAD_NUGGET, 9), "forge:ingots/lead");
        RecipeUtil.addShapedRecipe("lead_ingot_from_lead_nuggets", "lead_ingot", new ItemStack(ThreeCoreBase.LEAD_INGOT), "###", "###", "###", '#', "forge:nuggets/lead");
        RecipeUtil.addSmeltingRecipe("lead_ingot", new ItemStack(ThreeCoreBase.LEAD_INGOT), "forge:ores/lead", 0.7F, 160);
        RecipeUtil.addGrinderRecipe("lead_dust_from_lead_ore", "forge:dusts/lead#2", "forge:ores/lead", 0.7F, 320);
        RecipeUtil.addGrinderRecipe("lead_dust_from_lead_ingot", "forge:dusts/lead", "forge:ingots/lead", 0F, 160);
        RecipeUtil.addSmeltingRecipe("lead_ingot_from_lead_dust", new ItemStack(ThreeCoreBase.LEAD_INGOT), "forge:dusts/lead", 0F, 80);

        RecipeUtil.addShapelessRecipe("silver_ingot_from_silver_block", "silver_ingot", new ItemStack(ThreeCoreBase.SILVER_INGOT, 9), "forge:storage_blocks/silver");
        RecipeUtil.addShapedRecipe("silver_block", new ItemStack(ThreeCoreBase.SILVER_BLOCK), "###", "###", "###", '#', "forge:ingots/silver");
        RecipeUtil.addShapelessRecipe("silver_nugget", new ItemStack(ThreeCoreBase.SILVER_NUGGET, 9), "forge:ingots/silver");
        RecipeUtil.addShapedRecipe("silver_ingot_from_silver_nuggets", "silver_ingot", new ItemStack(ThreeCoreBase.SILVER_INGOT), "###", "###", "###", '#', "forge:nuggets/silver");
        RecipeUtil.addSmeltingRecipe("silver_ingot", new ItemStack(ThreeCoreBase.SILVER_INGOT), "forge:ores/silver", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("silver_dust_from_silver_ore", "forge:dusts/silver#2", "forge:ores/silver", 0.7F, 400);
        RecipeUtil.addGrinderRecipe("silver_dust_from_silver_ingot", "forge:dusts/silver", "forge:ingots/silver", 0F, 200);
        RecipeUtil.addSmeltingRecipe("silver_ingot_from_silver_dust", new ItemStack(ThreeCoreBase.SILVER_INGOT), "forge:dusts/silver", 0F, 100);

        RecipeUtil.addShapelessRecipe("palladium_ingot_from_palladium_block", "palladium_ingot", new ItemStack(ThreeCoreBase.PALLADIUM_INGOT, 9), "forge:storage_blocks/palladium");
        RecipeUtil.addShapedRecipe("palladium_block", new ItemStack(ThreeCoreBase.PALLADIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/palladium");
        RecipeUtil.addShapelessRecipe("palladium_nugget", new ItemStack(ThreeCoreBase.PALLADIUM_NUGGET, 9), "forge:ingots/palladium");
        RecipeUtil.addShapedRecipe("palladium_ingot_from_palladium_nuggets", "palladium_ingot", new ItemStack(ThreeCoreBase.PALLADIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/palladium");
        RecipeUtil.addSmeltingRecipe("palladium_ingot", new ItemStack(ThreeCoreBase.PALLADIUM_INGOT), "forge:ores/palladium", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("palladium_dust_from_palladium_ore", "forge:dusts/palladium#2", "forge:ores/palladium", 0.7F, 400);
        RecipeUtil.addGrinderRecipe("palladium_dust_from_palladium_ingot", "forge:dusts/palladium", "forge:ingots/palladium", 0F, 200);
        RecipeUtil.addSmeltingRecipe("palladium_ingot_from_palladium_dust", new ItemStack(ThreeCoreBase.PALLADIUM_INGOT), "forge:dusts/palladium", 0F, 100);

        RecipeUtil.addShapelessRecipe("vibranium_ingot_from_vibranium_block", "vibranium_ingot", new ItemStack(ThreeCoreBase.VIBRANIUM_INGOT, 9), "forge:storage_blocks/vibranium");
        RecipeUtil.addShapedRecipe("vibranium_block", new ItemStack(ThreeCoreBase.VIBRANIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/vibranium");
        RecipeUtil.addShapelessRecipe("vibranium_nugget", new ItemStack(ThreeCoreBase.VIBRANIUM_NUGGET, 9), "forge:ingots/vibranium");
        RecipeUtil.addShapedRecipe("vibranium_ingot_from_vibranium_nuggets", "vibranium_ingot", new ItemStack(ThreeCoreBase.VIBRANIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/vibranium");
        RecipeUtil.addSmeltingRecipe("vibranium_ingot", new ItemStack(ThreeCoreBase.VIBRANIUM_INGOT), "forge:ores/vibranium", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("vibranium_dust_from_vibranium_ore", "forge:dusts/vibranium#2", "forge:ores/vibranium", 1.1F, 1200);
        RecipeUtil.addGrinderRecipe("vibranium_dust_from_vibranium_ingot", "forge:dusts/vibranium", "forge:ingots/vibranium", 0F, 600);
        RecipeUtil.addSmeltingRecipe("vibranium_ingot_from_vibranium_dust", new ItemStack(ThreeCoreBase.VIBRANIUM_INGOT), "forge:dusts/vibranium", 0F, 300);

        RecipeUtil.addShapelessRecipe("osmium_ingot_from_osmium_block", "osmium_ingot", new ItemStack(ThreeCoreBase.OSMIUM_INGOT, 9), "forge:storage_blocks/osmium");
        RecipeUtil.addShapedRecipe("osmium_block", new ItemStack(ThreeCoreBase.OSMIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/osmium");
        RecipeUtil.addShapelessRecipe("osmium_nugget", new ItemStack(ThreeCoreBase.OSMIUM_NUGGET, 9), "forge:ingots/osmium");
        RecipeUtil.addShapedRecipe("osmium_ingot_from_osmium_nuggets", "osmium_ingot", new ItemStack(ThreeCoreBase.OSMIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/osmium");
        RecipeUtil.addSmeltingRecipe("osmium_ingot", new ItemStack(ThreeCoreBase.OSMIUM_INGOT), "forge:ores/osmium", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("osmium_dust_from_osmium_ore", "forge:dusts/osmium#2", "forge:ores/osmium", 0.7F, 400);
        RecipeUtil.addGrinderRecipe("osmium_dust_from_osmium_ingot", "forge:dusts/osmium", "forge:ingots/osmium", 0F, 200);
        RecipeUtil.addSmeltingRecipe("osmium_ingot_from_osmium_dust", new ItemStack(ThreeCoreBase.OSMIUM_INGOT), "forge:dusts/osmium", 0F, 100);

        RecipeUtil.addShapelessRecipe("uranium_ingot_from_uranium_block", "uranium_ingot", new ItemStack(ThreeCoreBase.URANIUM_INGOT, 9), "forge:storage_blocks/uranium");
        RecipeUtil.addShapedRecipe("uranium_block", new ItemStack(ThreeCoreBase.URANIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/uranium");
        RecipeUtil.addShapelessRecipe("uranium_nugget", new ItemStack(ThreeCoreBase.URANIUM_NUGGET, 9), "forge:ingots/uranium");
        RecipeUtil.addShapedRecipe("uranium_ingot_from_uranium_nuggets", "uranium_ingot", new ItemStack(ThreeCoreBase.URANIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/uranium");
        RecipeUtil.addSmeltingRecipe("uranium_ingot", new ItemStack(ThreeCoreBase.URANIUM_INGOT), "forge:ores/uranium", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("uranium_dust_from_uranium_ore", "forge:dusts/uranium#2", "forge:ores/uranium", 0.7F, 400);
        RecipeUtil.addGrinderRecipe("uranium_dust_from_uranium_ingot", "forge:dusts/uranium", "forge:ingots/uranium", 0F, 200);
        RecipeUtil.addSmeltingRecipe("uranium_ingot_from_uranium_dust", new ItemStack(ThreeCoreBase.URANIUM_INGOT), "forge:dusts/uranium", 0F, 100);

        RecipeUtil.addShapelessRecipe("titanium_ingot_from_titanium_block", "titanium_ingot", new ItemStack(ThreeCoreBase.TITANIUM_INGOT, 9), "forge:storage_blocks/titanium");
        RecipeUtil.addShapedRecipe("titanium_block", new ItemStack(ThreeCoreBase.TITANIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/titanium");
        RecipeUtil.addShapelessRecipe("titanium_nugget", new ItemStack(ThreeCoreBase.TITANIUM_NUGGET, 9), "forge:ingots/titanium");
        RecipeUtil.addShapedRecipe("titanium_ingot_from_titanium_nuggets", "titanium_ingot", new ItemStack(ThreeCoreBase.TITANIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/titanium");
        RecipeUtil.addSmeltingRecipe("titanium_ingot", new ItemStack(ThreeCoreBase.TITANIUM_INGOT), "forge:ores/titanium", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("titanium_dust_from_titanium_ore", "forge:dusts/titanium#2", "forge:ores/titanium", 0.7F, 800);
        RecipeUtil.addGrinderRecipe("titanium_dust_from_titanium_ingot", "forge:dusts/titanium", "forge:ingots/titanium", 0F, 400);
        RecipeUtil.addSmeltingRecipe("titanium_ingot_from_titanium_dust", new ItemStack(ThreeCoreBase.TITANIUM_INGOT), "forge:dusts/titanium", 0F, 200);

        RecipeUtil.addShapelessRecipe("iridium_ingot_from_iridium_block", "iridium_ingot", new ItemStack(ThreeCoreBase.IRIDIUM_INGOT, 9), "forge:storage_blocks/iridium");
        RecipeUtil.addShapedRecipe("iridium_block", new ItemStack(ThreeCoreBase.IRIDIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/iridium");
        RecipeUtil.addShapelessRecipe("iridium_nugget", new ItemStack(ThreeCoreBase.IRIDIUM_NUGGET, 9), "forge:ingots/iridium");
        RecipeUtil.addShapedRecipe("iridium_ingot_from_iridium_nuggets", "iridium_ingot", new ItemStack(ThreeCoreBase.IRIDIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/iridium");
        RecipeUtil.addSmeltingRecipe("iridium_ingot", new ItemStack(ThreeCoreBase.IRIDIUM_INGOT), "forge:ores/iridium", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("iridium_dust_from_iridium_ore", "forge:dusts/iridium#2", "forge:ores/iridium", 0.9F, 800);
        RecipeUtil.addGrinderRecipe("iridium_dust_from_iridium_ingot", "forge:dusts/iridium", "forge:ingots/iridium", 0F, 400);
        RecipeUtil.addSmeltingRecipe("iridium_ingot_from_iridium_dust", new ItemStack(ThreeCoreBase.IRIDIUM_INGOT), "forge:dusts/iridium", 0F, 200);

        RecipeUtil.addShapelessRecipe("uru_ingot_from_uru_block", "uru_ingot", new ItemStack(ThreeCoreBase.URU_INGOT, 9), "forge:storage_blocks/uru");
        RecipeUtil.addShapedRecipe("uru_block", new ItemStack(ThreeCoreBase.URU_BLOCK), "###", "###", "###", '#', "forge:ingots/uru");
        RecipeUtil.addShapelessRecipe("uru_nugget", new ItemStack(ThreeCoreBase.URU_NUGGET, 9), "forge:ingots/uru");
        RecipeUtil.addShapedRecipe("uru_ingot_from_uru_nuggets", "uru_ingot", new ItemStack(ThreeCoreBase.URU_INGOT), "###", "###", "###", '#', "forge:nuggets/uru");
        RecipeUtil.addSmeltingRecipe("uru_ingot", new ItemStack(ThreeCoreBase.URU_INGOT), "forge:ores/uru", 0.7F, 200);
        RecipeUtil.addGrinderRecipe("uru_dust_from_uru_ore", "forge:dusts/uru#2", "forge:ores/uru", 1.5F, 800);
        RecipeUtil.addGrinderRecipe("uru_dust_from_uru_ingot", "forge:dusts/uru", "forge:ingots/uru", 0F, 400);
        RecipeUtil.addSmeltingRecipe("uru_ingot_from_uru_dust", new ItemStack(ThreeCoreBase.URU_INGOT), "forge:dusts/uru", 0F, 200);

        RecipeUtil.addShapelessRecipe("bronze_ingot_from_bronze_block", "bronze_ingot", new ItemStack(ThreeCoreBase.BRONZE_INGOT, 9), "forge:storage_blocks/bronze");
        RecipeUtil.addShapedRecipe("bronze_block", new ItemStack(ThreeCoreBase.BRONZE_BLOCK), "###", "###", "###", '#', "forge:ingots/bronze");
        RecipeUtil.addShapelessRecipe("bronze_nugget", new ItemStack(ThreeCoreBase.BRONZE_NUGGET, 9), "forge:ingots/bronze");
        RecipeUtil.addShapedRecipe("bronze_ingot_from_bronze_nuggets", "bronze_ingot", new ItemStack(ThreeCoreBase.BRONZE_INGOT), "###", "###", "###", '#', "forge:nuggets/bronze");
        RecipeUtil.addGrinderRecipe("bronze_dust_from_bronze_ingot", "forge:dusts/bronze", "forge:ingots/bronze", 0F, 400);
        RecipeUtil.addSmeltingRecipe("bronze_ingot_from_bronze_dust", new ItemStack(ThreeCoreBase.BRONZE_INGOT), "forge:dusts/bronze", 0F, 200);

        RecipeUtil.addShapelessRecipe("intertium_ingot_from_intertium_block", "intertium_ingot", new ItemStack(ThreeCoreBase.INTERTIUM_INGOT, 9), "forge:storage_blocks/intertium");
        RecipeUtil.addShapedRecipe("intertium_block", new ItemStack(ThreeCoreBase.INTERTIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/intertium");
        RecipeUtil.addShapelessRecipe("intertium_nugget", new ItemStack(ThreeCoreBase.INTERTIUM_NUGGET, 9), "forge:ingots/intertium");
        RecipeUtil.addShapedRecipe("intertium_ingot_from_intertium_nuggets", "intertium_ingot", new ItemStack(ThreeCoreBase.INTERTIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/intertium");
        RecipeUtil.addGrinderRecipe("intertium_dust_from_intertium_ingot", "forge:dusts/intertium", "forge:ingots/intertium", 0F, 400);
        RecipeUtil.addSmeltingRecipe("intertium_ingot_from_intertium_dust", new ItemStack(ThreeCoreBase.INTERTIUM_INGOT), "forge:dusts/intertium", 0F, 200);

        RecipeUtil.addShapelessRecipe("steel_ingot_from_steel_block", "steel_ingot", new ItemStack(ThreeCoreBase.STEEL_INGOT, 9), "forge:storage_blocks/steel");
        RecipeUtil.addShapedRecipe("steel_block", new ItemStack(ThreeCoreBase.STEEL_BLOCK), "###", "###", "###", '#', "forge:ingots/steel");
        RecipeUtil.addShapelessRecipe("steel_nugget", new ItemStack(ThreeCoreBase.STEEL_NUGGET, 9), "forge:ingots/steel");
        RecipeUtil.addShapedRecipe("steel_ingot_from_steel_nuggets", "steel_ingot", new ItemStack(ThreeCoreBase.STEEL_INGOT), "###", "###", "###", '#', "forge:nuggets/steel");
        RecipeUtil.addGrinderRecipe("steel_dust_from_steel_ingot", "forge:dusts/steel", "forge:ingots/steel", 0F, 400);
        RecipeUtil.addSmeltingRecipe("steel_ingot_from_steel_dust", new ItemStack(ThreeCoreBase.STEEL_INGOT), "forge:dusts/steel", 0F, 200);

        RecipeUtil.addShapelessRecipe("gold_titanium_alloy_ingot_from_gold_titanium_alloy_block", "gold_titanium_alloy_ingot", new ItemStack(ThreeCoreBase.GOLD_TITANIUM_ALLOY_INGOT, 9), "forge:storage_blocks/gold_titanium_alloy");
        RecipeUtil.addShapedRecipe("gold_titanium_alloy_block", new ItemStack(ThreeCoreBase.GOLD_TITANIUM_ALLOY_BLOCK), "###", "###", "###", '#', "forge:ingots/gold_titanium_alloy");
        RecipeUtil.addShapelessRecipe("gold_titanium_alloy_nugget", new ItemStack(ThreeCoreBase.GOLD_TITANIUM_ALLOY_NUGGET, 9), "forge:ingots/gold_titanium_alloy");
        RecipeUtil.addShapedRecipe("gold_titanium_alloy_ingot_from_gold_titanium_alloy_nuggets", "gold_titanium_alloy_ingot", new ItemStack(ThreeCoreBase.GOLD_TITANIUM_ALLOY_INGOT), "###", "###", "###", '#', "forge:nuggets/gold_titanium_alloy");
        RecipeUtil.addGrinderRecipe("gold_titanium_alloy_dust_from_gold_titanium_alloy_ingot", "forge:dusts/gold_titanium_alloy", "forge:ingots/gold_titanium_alloy", 0F, 560);
        RecipeUtil.addSmeltingRecipe("gold_titanium_alloy_ingot_from_gold_titanium_alloy_dust", new ItemStack(ThreeCoreBase.GOLD_TITANIUM_ALLOY_INGOT), "forge:dusts/gold_titanium_alloy", 0F, 280);

        RecipeUtil.addShapelessRecipe("adamantium_ingot_from_adamantium_block", "adamantium_ingot", new ItemStack(ThreeCoreBase.ADAMANTIUM_INGOT, 9), "forge:storage_blocks/adamantium");
        RecipeUtil.addShapedRecipe("adamantium_block", new ItemStack(ThreeCoreBase.ADAMANTIUM_BLOCK), "###", "###", "###", '#', "forge:ingots/adamantium");
        RecipeUtil.addShapelessRecipe("adamantium_nugget", new ItemStack(ThreeCoreBase.ADAMANTIUM_NUGGET, 9), "forge:ingots/adamantium");
        RecipeUtil.addShapedRecipe("adamantium_ingot_from_adamantium_nuggets", "adamantium_ingot", new ItemStack(ThreeCoreBase.ADAMANTIUM_INGOT), "###", "###", "###", '#', "forge:nuggets/adamantium");
        RecipeUtil.addGrinderRecipe("adamantium_dust_from_adamantium_ingot", "forge:dusts/adamantium", "forge:ingots/adamantium", 0F, 1200);
        RecipeUtil.addSmeltingRecipe("adamantium_ingot_from_adamantium_dust", new ItemStack(ThreeCoreBase.ADAMANTIUM_INGOT), "forge:dusts/adamantium", 0F, 600);

        // Misc
        RecipeUtil.addGrinderRecipe("coal_dust", "forge:dusts/coal", Items.COAL, 0F, 200);
        RecipeUtil.addGrinderRecipe("charcoal_dust", "forge:dusts/charcoal", Items.CHARCOAL, 0F, 200);
        RecipeUtil.addGrinderRecipe("gravel_from_cobblestone", Blocks.GRAVEL, Blocks.COBBLESTONE, Blocks.SAND, 0.15F, 0F, 100);
        RecipeUtil.addGrinderRecipe("bonemeal", new ItemStack(Items.BONE_MEAL, 6), Items.BONE, 0F, 300);
        RecipeUtil.addGrinderRecipe("sand", Blocks.SAND, Blocks.GRAVEL, Items.FLINT, 0.15F, 0F, 100);
        RecipeUtil.addGrinderRecipe("gravel_from_netherrack", Blocks.GRAVEL, Blocks.NETHERRACK, 0F, 100);
        RecipeUtil.addGrinderRecipe("obsidian_dust", "forge:dusts/obsidian#4", Blocks.OBSIDIAN, 0F, 1000);
        RecipeUtil.addGrinderRecipe("cobblestone", Blocks.COBBLESTONE, Blocks.STONE, 0F, 100);
        RecipeUtil.addGrinderRecipe("cracked_stone_bricks", Blocks.CRACKED_STONE_BRICKS, Blocks.STONE_BRICKS, 0F, 200);
        RecipeUtil.addGrinderRecipe("wood_dust_from_logs", "forge:dusts/wood#8", "minecraft:logs", 0F, 200);
        RecipeUtil.addGrinderRecipe("wood_dust_from_planks", "forge:dusts/wood#2", "minecraft:logs", 0F, 100);
        RecipeUtil.addGrinderRecipe("blaze_rod", new ItemStack(Items.BLAZE_POWDER, 4), Items.BLAZE_ROD, 0F, 100);

        // Alloy
        RecipeUtil.addShapelessRecipe("bronze_dust", new ItemStack(ThreeCoreBase.BRONZE_DUST, 4), "forge:dusts/copper", "forge:dusts/copper", "forge:dusts/copper", "forge:dusts/tin");
        RecipeUtil.addShapelessRecipe("intertium_dust", new ItemStack(ThreeCoreBase.INTERTIUM_DUST, 3), "forge:dusts/osmium", "forge:dusts/iron", "forge:dusts/iron");
        RecipeUtil.addShapelessRecipe("adamantium_dust", new ItemStack(ThreeCoreBase.ADAMANTIUM_DUST, 3), "forge:dusts/vibranium", "forge:dusts/steel", "forge:dusts/steel");
        RecipeUtil.addShapelessRecipe("steel_dust", new ItemStack(ThreeCoreBase.STEEL_DUST, 3), "forge:dusts/iron", "forge:dusts/coal", "forge:dusts/coal", "forge:dusts/coal", "forge:dusts/coal");
        RecipeUtil.addShapelessRecipe("gold_titanium_alloy_dust", new ItemStack(ThreeCoreBase.GOLD_TITANIUM_ALLOY_DUST, 3), "forge:dusts/gold", "forge:dusts/titanium", "forge:dusts/titanium");
    }

    public static void setupDir() {
        if (RECIPE_DIR == null) {
            RECIPE_DIR = new File("recipes");
        }

        if (!RECIPE_DIR.exists()) {
            RECIPE_DIR.mkdir();
        }
    }

    public static ItemStack parseItemStackExt(JsonObject json, boolean readNBT) {
        if (json.has("tag")) {
            Tag<Item> tag = ItemTags.getCollection().get(new ResourceLocation(JSONUtils.getString(json, "tag")));

            if (tag == null || tag.getAllElements().size() <= 0)
                throw new JsonSyntaxException("Unknown tag '" + JSONUtils.getString(json, "tag") + "'");

            Item item = Lists.newArrayList(tag.getAllElements()).get(0);

            if (readNBT && json.has("nbt")) {
                // Lets hope this works? Needs test
                try {
                    JsonElement element = json.get("nbt");
                    CompoundNBT nbt;
                    if (element.isJsonObject())
                        nbt = JsonToNBT.getTagFromJson(GSON.toJson(element));
                    else
                        nbt = JsonToNBT.getTagFromJson(element.getAsString());

                    CompoundNBT tmp = new CompoundNBT();
                    if (nbt.contains("ForgeCaps")) {
                        tmp.put("ForgeCaps", nbt.get("ForgeCaps"));
                        nbt.remove("ForgeCaps");
                    }

                    tmp.put("tag", nbt);
                    tmp.putString("id", ForgeRegistries.ITEMS.getKey(item).toString());
                    tmp.putInt("Count", JSONUtils.getInt(json, "count", 1));

                    return ItemStack.read(tmp);
                } catch (CommandSyntaxException e) {
                    throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
                }
            }

            return new ItemStack(item, JSONUtils.getInt(json, "count", 1));
        } else {
            return CraftingHelper.getItemStack(json, readNBT);
        }
    }

    public static Map<String, Object> getCondition(Object o) {
        if (o instanceof String) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", ThreeCore.MODID + ":tag_exists");
            map.put("tag", o.toString().split("#")[0]);
            return map;
        } else if (o instanceof Item) {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey((Item) o);
            if (key.getNamespace().equalsIgnoreCase("minecraft") || key.getNamespace().equalsIgnoreCase(ThreeCore.MODID))
                return null;
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("type", "forge:item_exists");
            map.put("item", key.toString());
            return map;
        } else if (o instanceof ItemStack) {
            return getCondition(((ItemStack) o).getItem());
        } else if (o instanceof Block) {
            return getCondition(Item.getItemFromBlock((Block) o));
        }

        return null;
    }

    public static List<Map<String, Object>> generateConditions(List<Object> objects) {
        List<Map<String, Object>> list = new LinkedList<>();
        for (Object o : objects) {
            Map<String, Object> condition = getCondition(o);
            if (condition != null)
                list.add(condition);
        }
        return list;
    }

    public static void addGrinderRecipe(String name, Object output, Object input, float xp, int energy) {
        addGrinderRecipe(name, null, output, input, null, 0, xp, energy);
    }

    public static void addGrinderRecipe(String name, String group, Object output, Object input, float xp, int energy) {
        addGrinderRecipe(name, group, output, input, null, 0, xp, energy);
    }

    public static void addGrinderRecipe(String name, Object output, Object input, Object byproduct, float byproductChance, float xp, int energy) {
        addGrinderRecipe(name, null, output, input, byproduct, byproductChance, xp, energy);
    }

    public static void addGrinderRecipe(String name, String group, Object output, Object input, Object byproduct, float byproductChance, float xp, int energy) {
        setupDir();
        Map<String, Object> json = new LinkedHashMap<>();

        List<Object> test = Arrays.asList(output, input);
        List<Map<String, Object>> conditions = generateConditions(test);
        if (conditions.size() > 0)
            json.put("conditions", conditions.toArray(new Map[conditions.size()]));

        json.put("type", ThreeCoreBase.GRINDER_RECIPE_SERIALIZER.getRegistryName().toString());
        json.put("result", serializeItem(output, false));
        if (byproduct != null) {
            Map<String, Object> byproductMap = serializeItem(byproduct, false);
            byproductMap.put("chance", byproductChance);
            json.put("byproduct", byproductMap);
        }
        json.put("ingredient", serializeItem(input));
        json.put("experience", xp);
        json.put("energy", energy);

        if (group != null) {
            json.put("group", group);
        }

        File f = new File(RECIPE_DIR, name + ".json");

        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(json, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addSmeltingRecipe(String name, ItemStack output, Object input, float xp, int cookingTime) {
        addSmeltingRecipe(name, null, output, input, xp, cookingTime);
    }

    public static void addSmeltingRecipe(String name, String group, ItemStack output, Object input, float xp, int cookingTime) {
        setupDir();
        Map<String, Object> json = new LinkedHashMap<>();

        List<Object> test = Arrays.asList(output, input);
        List<Map<String, Object>> conditions = generateConditions(test);
        if (conditions.size() > 0)
            json.put("conditions", conditions.toArray(new Map[conditions.size()]));

        json.put("type", "smelting");
        json.put("result", output.getItem().getRegistryName().toString());
        json.put("ingredient", serializeItem(input));
        json.put("experience", xp);
        json.put("cookingtime", cookingTime);

        if (group != null) {
            json.put("group", group);
        }

        File f = new File(RECIPE_DIR, name + ".json");

        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(json, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addShapedRecipe(String name, ItemStack result, Object... components) {
        addShapedRecipe(name, null, result, components);
    }

    public static void addShapedRecipe(String name, String group, ItemStack result, Object... components) {
        setupDir();
        Map<String, Object> json = new LinkedHashMap<>();
        List<Object> test = new LinkedList<>();
        test.add(result);

        List<String> pattern = new ArrayList<>();
        int i = 0;
        while (i < components.length && components[i] instanceof String) {
            pattern.add((String) components[i]);
            i++;
        }
        json.put("pattern", pattern);

        Map<String, Map<String, Object>> key = new LinkedHashMap<>();
        Character curKey = null;
        for (; i < components.length; i++) {
            Object o = components[i];
            if (o instanceof Character) {
                if (curKey != null)
                    throw new IllegalArgumentException("Provided two char ac_keys in a row");
                curKey = (Character) o;
            } else {
                if (curKey == null)
                    throw new IllegalArgumentException("Providing object without a char key");
                key.put(Character.toString(curKey), serializeItem(o));

                if (!test.contains(o))
                    test.add(o);

                curKey = null;
            }
        }

        json.put("key", key);
        json.put("type", "minecraft:crafting_shaped");
        json.put("result", serializeItem(result));

        if (group != null) {
            json.put("group", group);
        }

        List<Map<String, Object>> conditions = generateConditions(test);
        if (conditions.size() > 0)
            json.put("conditions", conditions.toArray(new Map[conditions.size()]));

        File f = new File(RECIPE_DIR, name + ".json");

        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(json, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addShapelessRecipe(String name, ItemStack result, Object... components) {
        addShapelessRecipe(name, null, result, components);
    }

    public static void addShapelessRecipe(String name, String group, ItemStack result, Object... components) {
        setupDir();
        Map<String, Object> json = new LinkedHashMap<>();

        List<Object> test = new LinkedList<>();
        test.add(result);
        for (Object o : components)
            if (!test.contains(o))
                test.add(o);
        List<Map<String, Object>> conditions = generateConditions(test);
        if (conditions.size() > 0)
            json.put("conditions", conditions.toArray(new Map[conditions.size()]));

        List<Map<String, Object>> ingredients = new ArrayList<>();
        for (Object o : components) {
            ingredients.add(serializeItem(o));
        }
        json.put("ingredients", ingredients);
        json.put("type", "minecraft:crafting_shapeless");
        json.put("result", serializeItem(result));

        if (group != null) {
            json.put("group", group);
        }

        File f = new File(RECIPE_DIR, name + ".json");

        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(json, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> serializeItem(Object thing) {
        return serializeItem(thing, true);
    }

    public static Map<String, Object> serializeItem(Object thing, boolean ingredient) {
        if (thing instanceof Item) {
            return serializeItem(new ItemStack((Item) thing), ingredient);
        }
        if (thing instanceof Block) {
            return serializeItem(new ItemStack((Block) thing), ingredient);
        }
        if (thing instanceof ItemStack) {
            ItemStack stack = (ItemStack) thing;
            Map<String, Object> ret = new LinkedHashMap<>();
            ret.put("item", stack.getItem().getRegistryName().toString());

            if (stack.getCount() > 1) {
                ret.put("count", stack.getCount());
            }

            if (stack.hasTag()) {
                if (ingredient)
                    ret.put("type", "minecraft:item_nbt");
                ret.put("nbt", stack.getTag().toString());
            }

            return ret;
        }
        if (thing instanceof String) {
            String[] s = ((String) thing).split("#");
            int count = s.length > 1 ? Integer.parseInt(s[1]) : 1;
            Map<String, Object> ret = new LinkedHashMap<>();
            ret.put("tag", s[0]);
            if (count > 1)
                ret.put("count", count);
            return ret;
        }

        throw new IllegalArgumentException("Not a block, item, stack, or tag name");
    }

}
