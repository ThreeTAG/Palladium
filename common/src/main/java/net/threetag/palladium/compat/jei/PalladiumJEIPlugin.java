package net.threetag.palladium.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.compat.jei.tailoring.TailoringCategory;
import net.threetag.palladium.compat.jei.tailoring.TailoringTransferHandler;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.item.recipe.PalladiumRecipeSerializers;

import java.util.Objects;

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
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Objects.requireNonNull(Minecraft.getInstance().level);
        RecipeManager recipeManager = level.getRecipeManager();

        registration.addRecipes(TailoringCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(PalladiumRecipeSerializers.TAILORING.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(PalladiumItems.TAILORING_BENCH.get().getDefaultInstance(), TailoringCategory.RECIPE_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new TailoringTransferHandler(registration.getTransferHelper()), TailoringCategory.RECIPE_TYPE);
    }
}
