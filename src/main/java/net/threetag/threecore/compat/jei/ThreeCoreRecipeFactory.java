package net.threetag.threecore.compat.jei;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.base.recipe.FluidComposingRecipe;
import net.threetag.threecore.base.recipe.GrinderRecipe;
import net.threetag.threecore.base.recipe.PressingRecipe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ThreeCoreRecipeFactory {

    public static List<GrinderRecipe> getGrinderRecipes() {
        List<GrinderRecipe> list = new ArrayList<>();
        ClientWorld world = Minecraft.getInstance().world;
        RecipeManager recipeManager = world.getRecipeManager();
        list.addAll(getRecipes(recipeManager, GrinderRecipe.RECIPE_TYPE));
        return list;
    }

    public static List<PressingRecipe> getPressingRecipes() {
        List<PressingRecipe> list = new ArrayList<>();
        ClientWorld world = Minecraft.getInstance().world;
        RecipeManager recipeManager = world.getRecipeManager();
        list.addAll(getRecipes(recipeManager, PressingRecipe.RECIPE_TYPE));
        return list;
    }

    public static List<FluidComposingRecipe> getFluidComposingRecipes() {
        List<FluidComposingRecipe> list = new ArrayList<>();
        ClientWorld world = Minecraft.getInstance().world;
        RecipeManager recipeManager = world.getRecipeManager();
        list.addAll(getRecipes(recipeManager, FluidComposingRecipe.RECIPE_TYPE));
        return list;
    }

    private static <C extends IInventory, T extends IRecipe<C>> Collection<T> getRecipes(RecipeManager recipeManager, IRecipeType<T> recipeType) {
        Map<ResourceLocation, IRecipe<C>> recipesMap = recipeManager.getRecipes(recipeType);
        return (Collection<T>) recipesMap.values();
    }

}
