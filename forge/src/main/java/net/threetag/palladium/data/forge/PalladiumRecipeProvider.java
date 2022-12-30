package net.threetag.palladium.data.forge;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.block.PalladiumBlocks;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.tags.PalladiumItemTags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("NullableProblems")
public class PalladiumRecipeProvider extends RecipeProvider implements IConditionBuilder {

    private static final ImmutableList<ItemLike> LEAD_SMELTABLES = ImmutableList.of(PalladiumItems.RAW_LEAD.get(), PalladiumItems.LEAD_ORE.get(), PalladiumItems.DEEPSLATE_LEAD_ORE.get());
    private static final ImmutableList<ItemLike> VIBRANIUM_SMELTABLES = ImmutableList.of(PalladiumItems.RAW_VIBRANIUM.get(), PalladiumItems.VIBRANIUM_ORE.get());

    public PalladiumRecipeProvider(DataGenerator arg) {
        super(arg);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        oreSmelting(consumer, LEAD_SMELTABLES, PalladiumItems.LEAD_INGOT.get(), 0.7F, 200, "lead_ingot");
        oreSmelting(consumer, VIBRANIUM_SMELTABLES, PalladiumItems.VIBRANIUM_INGOT.get(), 1F, 600, "vibranium_ingot");

        oreBlasting(consumer, LEAD_SMELTABLES, PalladiumItems.LEAD_INGOT.get(), 0.7F, 100, "lead_ingot");
        oreBlasting(consumer, VIBRANIUM_SMELTABLES, PalladiumItems.VIBRANIUM_INGOT.get(), 1F, 300, "vibranium_ingot");

        nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, PalladiumItems.LEAD_INGOT.get(), PalladiumItems.LEAD_BLOCK.get(), "lead_ingot_from_lead_block", "lead_ingot");
        nineBlockStorageRecipesRecipesWithCustomUnpacking(consumer, PalladiumItems.VIBRANIUM_INGOT.get(), PalladiumItems.VIBRANIUM_BLOCK.get(), "vibranium_ingot_from_vibranium_block", "vibranium_ingot");

        nineBlockStorageRecipes(consumer, PalladiumItems.RAW_LEAD.get(), PalladiumItems.RAW_LEAD_BLOCK.get());
        nineBlockStorageRecipes(consumer, PalladiumItems.RAW_TITANIUM.get(), PalladiumItems.RAW_TITANIUM_BLOCK.get());
        nineBlockStorageRecipes(consumer, PalladiumItems.RAW_VIBRANIUM.get(), PalladiumItems.RAW_VIBRANIUM_BLOCK.get());

        ShapedRecipeBuilder.shaped(PalladiumItems.HAMMER.get()).pattern("III").pattern("ISI").pattern(" S ").define('I', PalladiumItemTags.INGOTS_IRON).define('S', PalladiumItemTags.WOODEN_STICKS).unlockedBy("has_iron", has(PalladiumItemTags.INGOTS_IRON)).save(consumer);

        ShapelessRecipeBuilder.shapeless(Items.PURPLE_DYE).requires(PalladiumBlocks.HEART_SHAPED_HERB.get()).group("purple_dye").unlockedBy("has_flower", has(PalladiumBlocks.HEART_SHAPED_HERB.get())).save(consumer, new ResourceLocation(Palladium.MOD_ID, "purple_dye_from_heart_shaped_herb"));

        UpgradeRecipeBuilder.smithing(Ingredient.of(Items.LEATHER_BOOTS), Ingredient.of(PalladiumItemTags.INGOTS_VIBRANIUM), PalladiumItems.VIBRANIUM_WEAVE_BOOTS.get()).unlocks("has_vibranium", has(PalladiumItemTags.INGOTS_VIBRANIUM)).save(consumer, new ResourceLocation(Palladium.MOD_ID, "vibranium_weave_boots_smithing"));
    }

    public static void oreSmelting(Consumer<FinishedRecipe> finishedRecipeConsumer, List<ItemLike> ingredients, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, ingredients, result, experience, cookingTime, group, "_from_smelting");
    }

    public static void oreBlasting(Consumer<FinishedRecipe> finishedRecipeConsumer, List<ItemLike> ingredients, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(finishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, ingredients, result, experience, cookingTime, group, "_from_blasting");
    }

    public static void oreCooking(Consumer<FinishedRecipe> finishedRecipeConsumer, SimpleCookingSerializer<?> cookingSerializer, List<ItemLike> ingredients, ItemLike result, float experience, int cookingTime, String group, String recipeName) {
        for (ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.cooking(Ingredient.of(itemlike), result, experience, cookingTime, cookingSerializer).group(group).unlockedBy(getHasName(itemlike), has(itemlike)).save(finishedRecipeConsumer, new ResourceLocation(Palladium.MOD_ID, getItemName(result) + recipeName + "_" + getItemName(itemlike)));
        }
    }

    public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike unpacked, ItemLike packed) {
        nineBlockStorageRecipes(finishedRecipeConsumer, unpacked, packed, getSimpleRecipeName(packed), null, getSimpleRecipeName(unpacked), null);
    }

    public static void nineBlockStorageRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike unpacked, ItemLike packed, String packingRecipeName, @Nullable String packingRecipeGroup, String unpackingRecipeName, @Nullable String unpackingRecipeGroup) {
        ShapelessRecipeBuilder.shapeless(unpacked, 9).requires(packed).group(unpackingRecipeGroup).unlockedBy(getHasName(packed), has(packed)).save(finishedRecipeConsumer, new ResourceLocation(Palladium.MOD_ID, unpackingRecipeName));
        ShapedRecipeBuilder.shaped(packed).define('#', unpacked).pattern("###").pattern("###").pattern("###").group(packingRecipeGroup).unlockedBy(getHasName(unpacked), has(unpacked)).save(finishedRecipeConsumer, new ResourceLocation(Palladium.MOD_ID, packingRecipeName));
    }

    public static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> finishedRecipeConsumer, ItemLike unpacked, ItemLike packed, String unpackingRecipeName, String unpackingRecipeGroup) {
        nineBlockStorageRecipes(finishedRecipeConsumer, unpacked, packed, getSimpleRecipeName(packed), null, unpackingRecipeName, unpackingRecipeGroup);
    }

    @Override
    public String getName() {
        return "Palladium " + super.getName();
    }
}
