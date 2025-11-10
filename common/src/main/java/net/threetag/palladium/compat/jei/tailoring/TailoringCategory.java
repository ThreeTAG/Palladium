package net.threetag.palladium.compat.jei.tailoring;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.item.recipe.SizedIngredient;
import net.threetag.palladium.item.recipe.TailoringRecipe;

import java.util.Arrays;
import java.util.List;

public class TailoringCategory implements IRecipeCategory<TailoringRecipe> {

    public static final RecipeType<TailoringRecipe> RECIPE_TYPE =
            RecipeType.create(Palladium.MOD_ID, "tailoring", TailoringRecipe.class);

    private static final int BG_WIDTH = 141;
    private static final int BG_HEIGHT = 75;

    private final IFocusFactory focusFactory;
    private final IDrawable icon;
    private final IDrawable background;

    public TailoringCategory(IGuiHelper guiHelper, IFocusFactory focusFactory) {
        this.focusFactory = focusFactory;

        this.icon = guiHelper.createDrawableItemStack(PalladiumItems.TAILORING_BENCH.get().getDefaultInstance());
        this.background = guiHelper.createDrawable(TailoringScreen.TEXTURE, 84, 17, BG_WIDTH, BG_HEIGHT);
    }

    @Override
    public RecipeType<TailoringRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("gui.palladium.category.tailoring");
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
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TailoringRecipe recipe, IFocusGroup focuses) {
        // Tool
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 58).addIngredients(recipe.getToolIngredient());

        // Input
        var ingredients = recipe.getSizedIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            SizedIngredient sizedIngredient = ingredients.get(i);
            List<ItemStack> acceptedItems = Arrays.stream(sizedIngredient.ingredient().getItems()).map(stack -> new ItemStack(stack.getItem(), sizedIngredient.count())).toList();

            // Will not show in GUI, but to inform JEI of accepted inputs for ingredient lookup
            builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(acceptedItems);
            // builder.addInputSlot().addItemStacks(acceptedItems);
        }

        // Results
        var results = recipe.getResults();
        EquipmentSlot[] orderedSlots = {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        };
        ItemStack[] resultStacks = Arrays.stream(orderedSlots)
                .map(results::get)
                .toArray(ItemStack[]::new);

        for (int i = 0; i < resultStacks.length; i++) {
            ItemStack stack = resultStacks[i];
            if (stack == null) {
                continue;
            }

            builder.addSlot(RecipeIngredientRole.OUTPUT, 124, 2 + (i * 18)).addItemStack(stack);
        }
    }

    @Override
    public void draw(TailoringRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);

        // guiGraphics.blit(TailoringScreen.TEXTURE, 0, 0, 81, 56, 6, 6, 1, 1, 256, 256);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, TailoringRecipe recipe, IFocusGroup focuses) {
        /*var slotsViews = builder.getRecipeSlots();
        var inputSlots = slotsViews.getSlots(RecipeIngredientRole.INPUT);
        inputSlots.remove(0); // Remove tool slot
        builder.addScrollGridWidget(inputSlots, 4, 3);*/

        var scrollWidget = new TailoringScrollGridRecipeWidget(recipe.getSizedIngredients(), 4, 3, focusFactory);
        builder.addWidget(scrollWidget);
        builder.addInputHandler(scrollWidget);
    }
}
