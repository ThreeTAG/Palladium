package net.threetag.threecore.compat.jei.grinding;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.StringTextComponent;
import net.threetag.threecore.block.TCBlocks;
import net.threetag.threecore.item.recipe.GrindingRecipe;
import net.threetag.threecore.compat.jei.ThreeCoreJEIPlugin;
import net.threetag.threecore.util.energy.EnergyUtil;
import net.threetag.threecore.util.MathUtil;
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

import java.util.Arrays;

public class GrindingCategory<T> implements IRecipeCategory<GrindingRecipe> {

    protected static final int inputSlot = 0;
    protected static final int primaryOutputSlot = 1;
    protected static final int secondaryOutputSlot = 2;

    private final String title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public GrindingCategory(IGuiHelper guiHelper) {
        this.title = I18n.format("gui.jei.category.threecore.grinding");
        this.background = guiHelper.drawableBuilder(ThreeCoreJEIPlugin.RECIPE_GUI_TEXTURE, 0, 0, 107, 26).addPadding(0, 10, 0, 0).build();
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(TCBlocks.GRINDER.get()));
        this.arrow = guiHelper.drawableBuilder(ThreeCoreJEIPlugin.RECIPE_GUI_TEXTURE, 107, 0, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public ResourceLocation getUid() {
        return ThreeCoreJEIPlugin.GRINDING_CATEGORY;
    }

    @Override
    public Class<? extends GrindingRecipe> getRecipeClass() {
        return GrindingRecipe.class;
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
    public void setIngredients(GrindingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        if (recipe.getByproduct() == null || recipe.getByproduct().isEmpty()) {
            ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
        } else {
            ingredients.setOutputs(VanillaTypes.ITEM, Arrays.asList(recipe.getRecipeOutput(), recipe.getByproduct() == null ? ItemStack.EMPTY : recipe.getByproduct()));
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, GrindingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

        itemStacks.init(inputSlot, true, 0, 4);
        itemStacks.init(primaryOutputSlot, false, 60, 4);
        itemStacks.init(secondaryOutputSlot, false, 89, 4);

        itemStacks.set(ingredients);

        itemStacks.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (slotIndex == secondaryOutputSlot && !ingredient.isEmpty()) {
                tooltip.add(new StringTextComponent(MathUtil.round(recipe.getByproductChance() * 100F, 2) + "%"));
            }
        });
    }

    @Override
    public void draw(GrindingRecipe recipe, MatrixStack stack, double mouseX, double mouseY) {
        arrow.draw(stack, 24, 4);

        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontRenderer = minecraft.fontRenderer;
        fontRenderer.func_238421_b_(stack, I18n.format("threecore.util.energy_display", recipe.getRequiredEnergy(), EnergyUtil.ENERGY_UNIT), 0, 28, 0xFF808080);
        float experience = recipe.getExperience();
        if (experience > 0) {
            String experienceString = I18n.format("gui.jei.category.threecore.grinding.experience", experience);
            int stringWidth = fontRenderer.getStringWidth(experienceString);
            fontRenderer.func_238421_b_(stack, experienceString, background.getWidth() - stringWidth, 28, 0xFF808080);
        }
    }
}
