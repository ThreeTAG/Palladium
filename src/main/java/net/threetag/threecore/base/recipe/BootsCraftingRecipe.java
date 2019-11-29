package net.threetag.threecore.base.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.recipe.RecipeUtil;

public class BootsCraftingRecipe extends AbstractConstructionTableRecipe {

    public static final IRecipeType<BootsCraftingRecipe> RECIPE_TYPE = RecipeUtil.register("boots_crafting");

    public BootsCraftingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack recipeOutput) {
        super(id, group, recipeItems, toolIngredient, recipeOutput);
    }

    @Override
    public IRecipeSerializer<BootsCraftingRecipe> getSerializer() {
        return TCBaseRecipeSerializers.BOOTS_CRAFTING;
    }

    @Override
    public IRecipeType<BootsCraftingRecipe> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer extends AbstractConstructionTableRecipe.Serializer<BootsCraftingRecipe> {

        public Serializer() {
            super(new String[]{
                    "XX",
                    "XX",
                    "XXXX"
            });
        }

        @Override
        public BootsCraftingRecipe create(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack result) {
            return new BootsCraftingRecipe(id, group, recipeItems, toolIngredient, result);
        }
    }
}
