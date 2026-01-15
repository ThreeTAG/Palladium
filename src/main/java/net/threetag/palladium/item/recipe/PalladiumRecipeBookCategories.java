package net.threetag.palladium.item.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

public class PalladiumRecipeBookCategories {

    public static final DeferredRegister<RecipeBookCategory> RECIPE_BOOK_CATEGORIES = DeferredRegister.create(Registries.RECIPE_BOOK_CATEGORY, Palladium.MOD_ID);

    public static final DeferredHolder<RecipeBookCategory, RecipeBookCategory> TAILORING = RECIPE_BOOK_CATEGORIES.register("tailoring", RecipeBookCategory::new);

}
