package net.threetag.threecore.base.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.recipe.RecipeUtil;

public class ChestplateCraftingRecipe extends AbstractConstructionTableRecipe {

    public static final IRecipeType<ChestplateCraftingRecipe> RECIPE_TYPE = RecipeUtil.register("chestplate_crafting");

    public ChestplateCraftingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack recipeOutput) {
        super(id, group, recipeItems, toolIngredient, recipeOutput);
    }

    @Override
    public IRecipeSerializer<ChestplateCraftingRecipe> getSerializer() {
        return TCBaseRecipeSerializers.CHESTPLATE_CRAFTING;
    }

    @Override
    public IRecipeType<ChestplateCraftingRecipe> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer extends AbstractConstructionTableRecipe.Serializer<ChestplateCraftingRecipe> {

        public Serializer() {
            super(new String[]{
                    "XXXXX",
                    "XXXXX",
                    "XXXXX",
                    "XXX"
            });
        }

        @Override
        public ChestplateCraftingRecipe create(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack result) {
            return new ChestplateCraftingRecipe(id, group, recipeItems, toolIngredient, result);
        }
    }
}
