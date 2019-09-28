package net.threetag.threecore.compat.jei.pressing;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.base.ThreeCoreBase;
import net.threetag.threecore.base.recipe.PressingRecipe;
import net.threetag.threecore.compat.jei.ThreeCoreJEIPlugin;
import net.threetag.threecore.util.energy.EnergyUtil;

public class PressingCategory<T> implements IRecipeCategory<PressingRecipe> {

    protected static final int castSlot = 0;
    protected static final int inputSlot = 1;
    protected static final int outputSlot = 2;

    private final String title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public PressingCategory(IGuiHelper guiHelper) {
        this.title = I18n.format("gui.jei.category.threecore.pressing");
        this.background = guiHelper.drawableBuilder(ThreeCoreJEIPlugin.RECIPE_GUI_TEXTURE, 0, 26, 116, 60).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ThreeCoreBase.HYDRAULIC_PRESS));
        this.arrow = guiHelper.drawableBuilder(ThreeCoreJEIPlugin.RECIPE_GUI_TEXTURE, 107, 0, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return ThreeCoreJEIPlugin.PRESSING_CATEGORY;
    }

    @Override
    public Class<? extends PressingRecipe> getRecipeClass() {
        return PressingRecipe.class;
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
    public void setIngredients(PressingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PressingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(castSlot, true, 0, 21);
        guiItemStacks.init(inputSlot, true, 32, 21);
        guiItemStacks.init(outputSlot, false, 94, 21);

        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(PressingRecipe recipe, double mouseX, double mouseY) {
        arrow.draw(58, 22);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.fontRenderer;
        {
            String energyString = I18n.format("threecore.util.energy_display", recipe.getEnergy(), EnergyUtil.ENERGY_UNIT);
            int stringWidth = fontRenderer.getStringWidth(energyString);
            fontRenderer.drawString(energyString, background.getWidth() - stringWidth, 50, 0xFF808080);
        }
        float experience = recipe.getExperience();
        if (experience > 0) {
            String experienceString = I18n.format("gui.jei.category.threecore.pressing.experience", experience);
            int stringWidth = fontRenderer.getStringWidth(experienceString);
            fontRenderer.drawString(experienceString, background.getWidth() - stringWidth, 3, 0xFF808080);
        }
    }
}
