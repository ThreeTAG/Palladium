package net.threetag.threecore.util.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RecipeUtil {

    public static <T extends IRecipe<?>> IRecipeType<T> register(final ResourceLocation name) {
        return Registry.register(Registry.RECIPE_TYPE, name, new IRecipeType<T>() {
            public String toString() {
                return name.toString();
            }
        });
    }

}
