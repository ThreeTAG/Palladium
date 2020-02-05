package net.threetag.threecore.compat.jei.fluidcomposing;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.item.recipe.FluidComposingRecipe;
import net.threetag.threecore.tileentity.FluidComposerTileEntity;
import net.threetag.threecore.compat.jei.ThreeCoreJEIPlugin;
import net.threetag.threecore.util.energy.EnergyUtil;

import java.util.Arrays;

public class FluidComposingCategory<T> implements IRecipeCategory<FluidComposingRecipe> {

    private final String title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawableStatic tankOverlay;

    public FluidComposingCategory(IGuiHelper guiHelper) {
        this.title = I18n.format("gui.jei.category.threecore.fluid_composing");
        this.background = guiHelper.drawableBuilder(ThreeCoreJEIPlugin.RECIPE_GUI_TEXTURE, 0, 86, 134, 62).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(TCBlocks.FLUID_COMPOSER));
        this.arrow = guiHelper.drawableBuilder(ThreeCoreJEIPlugin.RECIPE_GUI_TEXTURE, 107, 0, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
        this.tankOverlay = guiHelper.createDrawable(ThreeCoreJEIPlugin.RECIPE_GUI_TEXTURE, 135, 87, 16, 60);
    }

    @Override
    public ResourceLocation getUid() {
        return ThreeCoreJEIPlugin.FLUID_COMPOSING_CATEGORY;
    }

    @Override
    public Class<? extends FluidComposingRecipe> getRecipeClass() {
        return FluidComposingRecipe.class;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(FluidComposingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setInputs(VanillaTypes.FLUID, Arrays.asList(recipe.getInputFluid().getFluids()));
        ingredients.setOutput(VanillaTypes.FLUID, recipe.getResult(null));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FluidComposingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        IGuiFluidStackGroup fluidStacks = recipeLayout.getFluidStacks();

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int index = x + (y * 3);
                itemStacks.init(index, true, 24 + x * 18, 0 + y * 18);
            }
        }

        fluidStacks.init(0, true, 1, 1, 16, 60, FluidComposerTileEntity.TANK_CAPACITY, true, this.tankOverlay);
        fluidStacks.init(1, false, 117, 1, 16, 60, FluidComposerTileEntity.TANK_CAPACITY, true, this.tankOverlay);

        itemStacks.set(ingredients);
        fluidStacks.set(ingredients);
    }

    @Override
    public void draw(FluidComposingRecipe recipe, double mouseX, double mouseY) {
        arrow.draw(87, 18);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.fontRenderer;
        String s = I18n.format("threecore.util.energy_display", recipe.getRequiredEnergy(), EnergyUtil.ENERGY_UNIT);
        int length = fontRenderer.getStringWidth(s);
        fontRenderer.drawString(s, 134 / 2 - length / 2, 55, 0xFF808080);
    }
}
