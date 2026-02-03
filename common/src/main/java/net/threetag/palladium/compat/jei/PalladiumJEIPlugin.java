package net.threetag.palladium.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.MultiversalIteratorScreen;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladium.compat.jei.multiversalvariants.MultiversalVariantRecipe;
import net.threetag.palladium.compat.jei.multiversalvariants.MultiversalVariantsCategory;
import net.threetag.palladium.compat.jei.tailoring.TailoringCategory;
import net.threetag.palladium.compat.jei.tailoring.TailoringTransferHandler;
import net.threetag.palladium.item.MultiversalExtrapolatorItem;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.item.recipe.PalladiumRecipeSerializers;
import net.threetag.palladium.multiverse.MultiverseManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@JeiPlugin
public class PalladiumJEIPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return Palladium.id("jei");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeCategories(new TailoringCategory(jeiHelpers.getGuiHelper()));
        registration.addRecipeCategories(new MultiversalVariantsCategory(jeiHelpers.getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        RecipeManager recipeManager = level.getRecipeManager();

        registration.addRecipes(TailoringCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(PalladiumRecipeSerializers.TAILORING.get()));
        registration.addRecipes(MultiversalVariantsCategory.RECIPE_TYPE, MultiversalVariantRecipe.getRecipes(level));
        registration.addRecipes(RecipeTypes.CRAFTING, addSpecialCraftingRecipes(level));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(PalladiumItems.TAILORING_BENCH.get().getDefaultInstance(), TailoringCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(PalladiumItems.MULTIVERSAL_ITERATOR.get().getDefaultInstance(), MultiversalVariantsCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(TailoringScreen.class, 175, 47, 22, 15, TailoringCategory.RECIPE_TYPE);
        registration.addRecipeClickArea(MultiversalIteratorScreen.class, 74, 49, 20, 16, MultiversalVariantsCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new TailoringTransferHandler(registration.getTransferHelper()), TailoringCategory.RECIPE_TYPE);
    }

    private static List<CraftingRecipe> addSpecialCraftingRecipes(Level level) {
        String group = "jei.palladium.multiversal_extrapolator_cloning";
        return MultiverseManager.getInstance(level).getUniverses().values().stream().map(universe -> {
            List<CraftingRecipe> recipes = new ArrayList<>();
            var id = universe.getId();
            var stack = PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get().getDefaultInstance();
            MultiversalExtrapolatorItem.setUniverse(stack, universe);
            var result = stack.copy();
            result.setCount(2);

            NonNullList<Ingredient> inputs = NonNullList.of(Ingredient.EMPTY,
                    Ingredient.of(stack),
                    Ingredient.of(PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get()),
                    Ingredient.of(PalladiumItems.QUARTZ_CIRCUIT.get()));

            recipes.add(new ShapelessRecipe(
                    Palladium.id("jei.extrapolator_transfer." + id.getNamespace() + "." + id.getPath()),
                    group,
                    CraftingBookCategory.MISC,
                    result,
                    inputs
            ));

            inputs = NonNullList.of(Ingredient.EMPTY,
                    Ingredient.of(stack),
                    Ingredient.of(PalladiumItems.VIBRANIUM_CIRCUIT.get()),
                    Ingredient.of(Items.DIAMOND),
                    Ingredient.of(Items.DIAMOND),
                    Ingredient.of(Items.DIAMOND),
                    Ingredient.of(Items.DIAMOND),
                    Ingredient.of(Items.DIAMOND),
                    Ingredient.of(Items.DIAMOND),
                    Ingredient.of(Items.DIAMOND)
            );

            recipes.add(new ShapelessRecipe(
                    Palladium.id("jei.extrapolator_cloning." + id.getNamespace() + "." + id.getPath()),
                    group,
                    CraftingBookCategory.MISC,
                    result,
                    inputs
            ));

            return recipes;
        }).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
