package net.threetag.threecore.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.base.client.gui.FluidComposerScreen;
import net.threetag.threecore.base.client.gui.GrinderScreen;
import net.threetag.threecore.base.client.gui.HydraulicPressScreen;
import net.threetag.threecore.base.inventory.FluidComposerContainer;
import net.threetag.threecore.base.inventory.GrinderContainer;
import net.threetag.threecore.base.inventory.HydraulicPressContainer;
import net.threetag.threecore.compat.jei.fluidcomposing.FluidComposingCategory;
import net.threetag.threecore.compat.jei.grinding.GrindingCategory;
import net.threetag.threecore.compat.jei.pressing.PressingCategory;

import java.util.Objects;

@JeiPlugin
public class ThreeCoreJEIPlugin implements IModPlugin {

    public static final ResourceLocation RECIPE_GUI_TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/jei.png");
    public static final ResourceLocation GRINDING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "grinding");
    public static final ResourceLocation PRESSING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "pressing");
    public static final ResourceLocation FLUID_COMPOSING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "fluid_composing");

    private GrindingCategory grindingCategory;
    private PressingCategory pressingCategory;
    private FluidComposingCategory fluidComposingCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ThreeCore.MODID, "plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(
                grindingCategory = new GrindingCategory(guiHelper),
                pressingCategory = new PressingCategory(guiHelper),
                fluidComposingCategory = new FluidComposingCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Objects.requireNonNull(grindingCategory);
        Objects.requireNonNull(pressingCategory);
        Objects.requireNonNull(fluidComposingCategory);

        registration.addRecipes(ThreeCoreRecipeFactory.getGrinderRecipes(), GRINDING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getPressingRecipes(), PRESSING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getFluidComposingRecipes(), FLUID_COMPOSING_CATEGORY);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GrinderScreen.class, 66, 36, 28, 23, GRINDING_CATEGORY);
        registration.addRecipeClickArea(HydraulicPressScreen.class, 96, 36, 28, 23, PRESSING_CATEGORY);
        registration.addRecipeClickArea(FluidComposerScreen.class, 107, 44, 28, 23, FLUID_COMPOSING_CATEGORY);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(GrinderContainer.class, GRINDING_CATEGORY, 1, 1, 4, 36);
        registration.addRecipeTransferHandler(HydraulicPressContainer.class, PRESSING_CATEGORY, 1, 2, 4, 36);
        registration.addRecipeTransferHandler(FluidComposerContainer.class, FLUID_COMPOSING_CATEGORY, 5, 9, 14, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ThreeCoreBase.GRINDER), GRINDING_CATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ThreeCoreBase.HYDRAULIC_PRESS), PRESSING_CATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ThreeCoreBase.FLUID_COMPOSER), FLUID_COMPOSING_CATEGORY);
    }
}