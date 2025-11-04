package net.threetag.palladium.compat.jei;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladium.compat.jei.util.ManagedCycleTimer;
import net.threetag.palladium.item.PalladiumItems;
import net.threetag.palladium.item.recipe.SizedIngredient;
import net.threetag.palladium.item.recipe.TailoringRecipe;

import java.util.Arrays;
import java.util.List;

public class TailoringCategory implements IRecipeCategory<TailoringRecipe> {

    public static final RecipeType<TailoringRecipe> RECIPE_TYPE =
            RecipeType.create(Palladium.MOD_ID, "tailoring", TailoringRecipe.class);

    private static final ResourceLocation SLOT_TEXTURE = Palladium.id("textures/gui/jei/tailoring_slot.png");

    private final IFocusFactory focusFactory;
    private final IDrawable icon;
    private final IDrawable background;
    private final IDrawable scrollHandle;

    private final ManagedCycleTimer ingredientCycleTimer;
    private final ManagedCycleTimer scrollTimer;

    private final int slotWidth = 16, slotHeight = 18;
    private final int boxWidth = 64, boxHeight = 54; // area for input slots, excluding borders
    private final int maxVisibleInputs = (boxWidth / slotWidth) * (boxHeight / slotHeight);
    private final int maxCol = 4, maxRow = 3;

    private final int scrollBarHeight = boxHeight; // length of the scroll bar area in which the handle can move, which is the same as boxHeight
    private final int scrollHandleHeight = 15;
    private final int scrollTrackLength = scrollBarHeight - scrollHandleHeight; // length the handle can move along the scroll bar

    private int inputsYOffset = 0;
    private int scrollHandleY = 0;
    private ItemStack hoveredStack = ItemStack.EMPTY;
    private TailoringRecipe currentRecipe = null;
    private ScrollData scrollData = null;

    public TailoringCategory(IGuiHelper guiHelper, IFocusFactory focusFactory) {
        this.focusFactory = focusFactory;

        this.icon = guiHelper.createDrawableItemStack(PalladiumItems.TAILORING_BENCH.get().getDefaultInstance());
        this.background = guiHelper.createDrawable(TailoringScreen.TEXTURE, 84, 17, 141, 75);
        this.scrollHandle = guiHelper.createDrawable(TailoringScreen.TEXTURE, 77, 189, 12, scrollHandleHeight);

        this.ingredientCycleTimer = new ManagedCycleTimer(10);
        this.scrollTimer = new ManagedCycleTimer(0);
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
    public IDrawable getBackground() {
        return background;
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
        // Retrieve the gui coordinates
        int x = 1, y = 1;
        boolean isMouseOverInputs = isMouseInInputs(mouseX, mouseY, x, y, boxWidth, boxHeight);

        if (this.currentRecipe != recipe) {
            onRecipeChange(recipe);

            this.currentRecipe = recipe;
        }

        // Set up
        ingredientCycleTimer.onDraw();
        hoveredStack = ItemStack.EMPTY;
        doScrollLogic(recipe, isMouseOverInputs);

        var guiPos = getGuiPosition(guiGraphics);
        int scissorX = guiPos.getFirst() + x;
        int scissorY = guiPos.getSecond() + y;

        // Draw input slots and items within a scissor box
        guiGraphics.enableScissor(scissorX, scissorY, scissorX + boxWidth, scissorY + boxHeight);
        renderInputs(recipe, guiGraphics, x, y - inputsYOffset, mouseX, mouseY, isMouseOverInputs);
        guiGraphics.disableScissor();

        // Draw scroll bar
        if (shouldScroll(recipe)) {
            int scrollBarX = x + boxWidth + 3;
            int scrollBarY = y + scrollHandleY;
            scrollHandle.draw(guiGraphics, scrollBarX, scrollBarY);
        }

        // Draw hovered input tooltip
        renderInputTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean handleInput(TailoringRecipe recipe, double mouseX, double mouseY, InputConstants.Key input) {
        if (!hoveredStack.isEmpty()) {
            var recipeGui = PalladiumJEIPlugin.getRecipesGui();
            if (recipeGui != null) {
                recipeGui.show(focusFactory.createFocus(RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, hoveredStack));

                return true;
            }
        }
        return false;
    }

    private void onRecipeChange(TailoringRecipe recipe) {
        this.scrollData = calculateScroll(recipe);
        this.scrollTimer.start();

        this.ingredientCycleTimer.start();
    }

    private Pair<Integer, Integer> getGuiPosition(GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        PoseStack.Pose currPose = poseStack.last(); // recipe's Pose
        poseStack.popPose(); // Temporarily remove recipe's Pose

        PoseStack.Pose parentPose = poseStack.last(); // parent Pose, this is where we can get the translate coordinates
        int guiX = (int) parentPose.pose().m30();
        int guiY = (int) parentPose.pose().m31();
        poseStack.poseStack.add(currPose); // Re-add recipe's Pose

        return Pair.of(guiX, guiY);
    }

    private void renderInputs(TailoringRecipe recipe, GuiGraphics guiGraphics, int x, int y, double mouseX, double mouseY, boolean isMouseInArea) {
        var ingredients = recipe.getSizedIngredients();

        for (int i = 0; i < ingredients.size(); i++) {
            SizedIngredient ingredient = ingredients.get(i);
            var ingredientItems = ingredient.ingredient().getItems();
            int index = ingredientCycleTimer.getValue(ingredientItems.length);
            ItemStack stack = ingredientItems[index].copyWithCount(ingredient.count());

            int localX = x + (i % maxCol) * slotWidth;
            int localY = y + (i / maxCol) * slotHeight; // +1 to center vertically in slot

            boolean isHovered = isMouseInArea && isMouseOverSlot(mouseX, mouseY, localX, localY);
            guiGraphics.blit(SLOT_TEXTURE, localX, localY, isHovered ? slotWidth : 0, 0, slotWidth, slotHeight, slotWidth * 2, slotWidth * 2);

            localY += 1; // Center item vertically in slot
            guiGraphics.renderItem(stack, localX, localY);
            guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, localX, localY);

            if (isHovered) {
                hoveredStack = stack;
            }
        }
    }

    private void renderInputTooltip(GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (!hoveredStack.isEmpty()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, hoveredStack, (int) mouseX, (int) mouseY);
        }
    }

    private void doScrollLogic(TailoringRecipe recipe, boolean isMouseOverInputs) {
        scrollTimer.onDraw();

        var shouldScroll = shouldScroll(recipe);
        if (!shouldScroll) {
            this.inputsYOffset = 0;
            this.scrollHandleY = 0;

            this.scrollTimer.stop();

            return;
        }

        this.scrollTimer.setPaused(isMouseOverInputs);

        int cycle = scrollTimer.getValue(scrollData.maxScrollCycles);
        int localizedCycle = scrollData.getLocalizedCycle(cycle);

        this.inputsYOffset = localizedCycle * slotHeight;
        this.scrollHandleY = localizedCycle * scrollData.scrollOffset;

        // Clamp to bottom edge
        if (scrollData.isCycleAtBottomRow(cycle)) {
            this.scrollHandleY = scrollBarHeight - scrollHandleHeight;
        }
    }

    private boolean isMouseOverSlot(double mouseX, double mouseY, int slotX, int slotY) {
        return mouseX >= slotX && mouseX < slotX + slotWidth && mouseY >= slotY && mouseY < slotY + slotHeight;
    }

    private boolean isMouseInInputs(double mouseX, double mouseY, int x, int y, int width, int height) {
        int x2 = x + width;
        int y2 = y + height;

        return mouseX >= x && mouseX < x2 && mouseY >= y && mouseY < y2;
    }

    private ScrollData calculateScroll(TailoringRecipe recipe) {
        int totalIngredients = recipe.getSizedIngredients().size();
        int totalRows = (int) Math.ceil((double) totalIngredients / maxCol);

        int hiddenRows = totalRows - maxRow;
        if (hiddenRows <= 0) {
            return new ScrollData(0, 0);
        }

        return new ScrollData(scrollTrackLength / hiddenRows, hiddenRows * 2);
    }

    private boolean shouldScroll(TailoringRecipe recipe) {
        int totalIngredients = recipe.getSizedIngredients().size();
        return totalIngredients > maxVisibleInputs;
    }

    /**
     * Data class for scroll calculations.
     *
     * @param scrollOffset    How far can the scroll handle move per row, based on number of rows
     * @param maxScrollCycles Maximum number of scroll cycles (rows that can be scrolled)
     */
    private record ScrollData(int scrollOffset, int maxScrollCycles) {
        /*
         * Self Notes:
         * If there are N hidden rows, where N >= 1, there are 2N scroll cycles: 0 + N + (N - 1)
         * The breakdown is as follows:
         *   - 0, displays the first few visible rows, that is, all hidden rows are below view
         *   - 1 to N, scrolls down one row at a time until the last hidden row is at the bottom of the view area
         *   - N+1 to 2N-1, scrolls back up one row at a time until the first hidden row is just above the view area
         *
         * Example:
         * If there are 3 hidden rows, there are 6 scroll cycles (0 to 5):
         *   - Cycle 0: hidden rows 1, 2, 3 are below view (initial state)
         *   - Cycle 1: hidden row 1 is now visible (Behaviour: Scroll down)
         *   - Cycle 2: hidden row 1,2 is now visible
         *   - Cycle 3: hidden row 1,2,3 is now visible
         *   - Cycle 4: hidden row 1,2 is now visible (Behaviour: Scroll up)
         *   - Cycle 5: hidden row 1 is now visible
         *  <---- Cycle resets, back to Cycle 0, initial state, repeat ---->
         */


        /**
         * Check if the current cycle is at the bottom row of the scroll.
         *
         * @param currentCycle The current scroll cycle
         * @return True if at bottom row, false otherwise
         */
        public boolean isCycleAtBottomRow(int currentCycle) {
            // First half of maxScrollCycles is scrolling down
            return currentCycle == (maxScrollCycles / 2);
        }

        public boolean isScrollingUp(int currentCycle) {
            // Second half of maxScrollCycles is scrolling up
            return currentCycle >= (maxScrollCycles / 2);
        }

        public int getLocalizedCycle(int currentCycle) {
            if (isScrollingUp(currentCycle)) {
                return maxScrollCycles - currentCycle;
            }
            return currentCycle;
        }
    }
}
