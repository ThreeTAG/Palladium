package net.threetag.palladium.item.recipe;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipePropertySet;
import net.threetag.palladium.Palladium;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PalladiumRecipePropertySets {

    private static final Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> PROPERTY_SETS = new HashMap<>();

    public static final ResourceKey<RecipePropertySet> TAILORING_TOOL = register("tailoring_tool", recipe -> recipe instanceof TailoringRecipe r ? Optional.of(r.toolIngredient()) : Optional.empty());

    private static ResourceKey<RecipePropertySet> register(String name, RecipeManager.IngredientExtractor ingredientExtractor) {
        var key = ResourceKey.create(RecipePropertySet.TYPE_KEY, Palladium.id(name));
        PROPERTY_SETS.put(key, ingredientExtractor);
        return key;
    }

    public static void addPropertySets(Map<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> sets) {
        for (Map.Entry<ResourceKey<RecipePropertySet>, RecipeManager.IngredientExtractor> e : PROPERTY_SETS.entrySet()) {
            if (!sets.containsKey(e.getKey())) {
                sets.put(e.getKey(), e.getValue());
            }
        }
    }

}
