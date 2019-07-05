package com.threetag.threecore.compat.jei;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.base.ThreeCoreBase;
import com.threetag.threecore.base.inventory.GrinderContainer;
import com.threetag.threecore.base.client.gui.GrinderScreen;
import com.threetag.threecore.base.inventory.HydraulicPressContainer;
import com.threetag.threecore.base.client.gui.HydraulicPressScreen;
import com.threetag.threecore.compat.jei.grinding.GrindingCategory;
import com.threetag.threecore.compat.jei.pressing.PressingCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;

@JeiPlugin
public class ThreeCoreJEIPlugin implements IModPlugin {

    public static final ResourceLocation RECIPE_GUI_TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/jei.png");
    public static final ResourceLocation GRINDING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "grinding");
    public static final ResourceLocation PRESSING_CATEGORY = new ResourceLocation(ThreeCore.MODID, "pressing");

    private GrindingCategory grindingCategory;
    private PressingCategory pressingCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(ThreeCore.MODID, "plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registration.addRecipeCategories(grindingCategory = new GrindingCategory(guiHelper), pressingCategory = new PressingCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        Objects.requireNonNull(grindingCategory);
        Objects.requireNonNull(pressingCategory);

        registration.addRecipes(ThreeCoreRecipeFactory.getGrinderRecipes(), GRINDING_CATEGORY);
        registration.addRecipes(ThreeCoreRecipeFactory.getPressingRecipes(), PRESSING_CATEGORY);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(GrinderScreen.class, 66, 36, 28, 23, GRINDING_CATEGORY);
        registration.addRecipeClickArea(HydraulicPressScreen.class, 96, 36, 28, 23, PRESSING_CATEGORY);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(GrinderContainer.class, GRINDING_CATEGORY, 1, 1, 4, 36);
        registration.addRecipeTransferHandler(HydraulicPressContainer.class, PRESSING_CATEGORY, 1, 2, 4, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ThreeCoreBase.GRINDER), GRINDING_CATEGORY);
        registration.addRecipeCatalyst(new ItemStack(ThreeCoreBase.HYDRAULIC_PRESS), PRESSING_CATEGORY);
    }
}