package net.threetag.palladium.compat.jei.multiversalvariants;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.MultiversalIteratorScreen;
import net.threetag.palladium.item.PalladiumItems;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

public class MultiversalVariantsCategory implements IRecipeCategory<MultiversalVariantRecipe> {

    public static final RecipeType<MultiversalVariantRecipe> RECIPE_TYPE =
            RecipeType.create(Palladium.MOD_ID, "multiversal_variants", MultiversalVariantRecipe.class);
    private static final int BG_WIDTH = 108;
    private static final int BG_HEIGHT = 54;

    private final IDrawable icon;
    private final IDrawable background;

    public MultiversalVariantsCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableItemStack(PalladiumItems.MULTIVERSAL_ITERATOR.get().getDefaultInstance());
        this.background = guiHelper.createDrawable(MultiversalIteratorScreen.TEXTURE, 34, 16, BG_WIDTH, BG_HEIGHT);
    }

    @Override
    public RecipeType<MultiversalVariantRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.palladium.category.multiversal_variants");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public int getWidth() {
        return BG_WIDTH;
    }

    @Override
    public int getHeight() {
        return BG_HEIGHT;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MultiversalVariantRecipe recipe, IFocusGroup focusGroup) {
        builder.addSlot(RecipeIngredientRole.INPUT, 4, 4).addItemStack(recipe.getExtrapolator());
        builder.addSlot(RecipeIngredientRole.INPUT, 14, 33).addItemStacks(recipe.getInputs().stream().map(Item::getDefaultInstance).collect(Collectors.toList()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 74, 33).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(MultiversalVariantRecipe recipe, IRecipeSlotsView view, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
        guiGraphics.drawString(Minecraft.getInstance().font, recipe.getUniverseTitle(), 27, 8, 0xffffffff);
    }
}
