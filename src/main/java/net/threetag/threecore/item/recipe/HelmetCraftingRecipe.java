package net.threetag.threecore.item.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.util.RecipeUtil;

public class HelmetCraftingRecipe extends AbstractConstructionTableRecipe {

    public static final IRecipeType<HelmetCraftingRecipe> RECIPE_TYPE = RecipeUtil.register("helmet_crafting");

    public HelmetCraftingRecipe(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack recipeOutput) {
        super(id, group, recipeItems, toolIngredient, recipeOutput);
    }

    @Override
    public IRecipeSerializer<HelmetCraftingRecipe> getSerializer() {
        return TCBaseRecipeSerializers.HELMET_CRAFTING;
    }

    @Override
    public IRecipeType<HelmetCraftingRecipe> getType() {
        return RECIPE_TYPE;
    }

    public static class Serializer extends AbstractConstructionTableRecipe.Serializer<HelmetCraftingRecipe> {

        public Serializer() {
            super(new String[]{
                    "XXXX",
                    "XXXX",
                    "XXXX",
                    "XXXX"
            });
        }

        @Override
        public HelmetCraftingRecipe create(ResourceLocation id, String group, NonNullList<Ingredient> recipeItems, Ingredient toolIngredient, ItemStack result) {
            return new HelmetCraftingRecipe(id, group, recipeItems, toolIngredient, result);
        }
    }
}
