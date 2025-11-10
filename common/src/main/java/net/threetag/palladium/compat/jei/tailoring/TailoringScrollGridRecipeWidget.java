package net.threetag.palladium.compat.jei.tailoring;

import com.mojang.blaze3d.platform.InputConstants;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.inputs.IJeiInputHandler;
import mezz.jei.api.gui.inputs.IJeiUserInput;
import mezz.jei.api.gui.placement.IPlaceable;
import mezz.jei.api.gui.widgets.IRecipeWidget;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladium.compat.jei.PalladiumJEIPlugin;
import net.threetag.palladium.compat.jei.util.CycleTimer;
import net.threetag.palladium.item.recipe.SizedIngredient;

import java.util.List;

public class TailoringScrollGridRecipeWidget implements IRecipeWidget, IPlaceable<TailoringScrollGridRecipeWidget>, IJeiInputHandler {

    private static final ResourceLocation SLOT_TEXTURE = Palladium.id("textures/gui/jei/tailoring_slot.png");

    private static final int SLOT_WIDTH = 16;
    private static final int SLOT_HEIGHT = 18;
    private static final int CONTENT_WIDTH = 66;
    private static final int CONTENT_HEIGHT = 56;
    private static final int SCROLLBAR_PADDING = 1;
    private static final int SCROLLBAR_WIDTH = 14;
    private static final int SCROLL_HANDLE_WIDTH = SCROLLBAR_WIDTH - 2;
    private static final int SCROLL_HANDLE_HEIGHT = 15;
    private static final int SCROLL_SPACING = CONTENT_HEIGHT - SCROLL_HANDLE_HEIGHT - 2; // -2 to account for borders

    private final IFocusFactory focusFactory;
    private final List<SizedIngredient> slots;
    private final int columns;
    private final int visibleRows;
    private final int hiddenRows;
    private final float scrollAmount;
    private final Rect2i area;
    private final CycleTimer ingredientCycleTimer;

    private int inputRowOffset = 0; // offset unit, in rows
    private int scrollHandleOffset = 0;
    private ItemStack hoveredStack = ItemStack.EMPTY;

    public TailoringScrollGridRecipeWidget(List<SizedIngredient> slots, int columns, int visibleRows, IFocusFactory focusFactory) {
        this.slots = slots;

        this.columns = columns;
        this.visibleRows = visibleRows;
        this.focusFactory = focusFactory;
        int totalRows = (int) Math.ceil((double) slots.size() / columns);
        this.hiddenRows = Math.max(totalRows - visibleRows, 0); // subtraction could be negative
        this.scrollAmount = hiddenRows > 0 ? (float) (SCROLL_SPACING) / hiddenRows : 0f;

        this.area = new Rect2i(0, 0, CONTENT_WIDTH + SCROLLBAR_PADDING + SCROLLBAR_WIDTH, CONTENT_HEIGHT);
        this.ingredientCycleTimer = new CycleTimer(10);
    }

    @Override
    public ScreenRectangle getArea() {
        return new ScreenRectangle(0, 0, this.area.getWidth(), this.area.getHeight());
    }

    @Override
    public TailoringScrollGridRecipeWidget setPosition(int xPos, int yPos) {
        this.area.setPosition(xPos, yPos);
        return this;
    }

    @Override
    public int getWidth() {
        // Width of the entire scroll widget: scrollable area + scrollbar + spacing between scrollbar and scrollable area, if any
        return this.area.getWidth();
    }

    @Override
    public int getHeight() {
        return this.area.getHeight();
    }

    @Override
    public ScreenPosition getPosition() {
        return new ScreenPosition(area.getX(), area.getY());
    }

    @Override
    public boolean handleInput(double mouseX, double mouseY, IJeiUserInput input) {
        final InputConstants.Key key = input.getKey();

        // Check if input was not left mouse click
        if (key.getType() != InputConstants.Type.MOUSE || key.getValue() != 0) {
            return false;
        }

        if (!hoveredStack.isEmpty()) {
            var recipeGui = PalladiumJEIPlugin.getRecipesGui();
            if (!input.isSimulate() && recipeGui != null) {
                recipeGui.show(focusFactory.createFocus(RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, hoveredStack));
            }
            return true;
        }
        else if (isMouseOverRect(mouseX, mouseY, CONTENT_WIDTH + SCROLLBAR_PADDING + 1, 1, SCROLLBAR_WIDTH, CONTENT_HEIGHT - 2)) {
            // Clicked on scrollbar area but not on any item
            return true;
        }
        return false;
    }

    @Override
    public boolean handleMouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        if (hiddenRows > 0) {
            var deltaInt = scrollDelta > 0 ? -1 : 1;
            this.inputRowOffset = Mth.clamp(this.inputRowOffset + deltaInt, 0, hiddenRows);

            this.scrollHandleOffset = (int) (scrollAmount * inputRowOffset);
            if (inputRowOffset == hiddenRows) {
                this.scrollHandleOffset = CONTENT_HEIGHT - SCROLL_HANDLE_HEIGHT - 2; // -2 to account for borders
            }

            return true;
        }

        return false;
    }

    @Override
    public void drawWidget(GuiGraphics guiGraphics, double mouseX, double mouseY) {
        var poseStack = guiGraphics.pose();

        poseStack.pushPose();
        poseStack.translate(1, 1, 0);
        drawContents(guiGraphics, mouseX - 1, mouseY - 1);
        poseStack.popPose();

        poseStack.pushPose();
        int scrollX = CONTENT_WIDTH + SCROLLBAR_PADDING + 1; // 1 is the container border
        poseStack.translate(scrollX, 1, 0);
        guiGraphics.blit(TailoringScreen.TEXTURE, 0, scrollHandleOffset, 65 + (hiddenRows == 0 ? SCROLL_HANDLE_WIDTH : 0), 189, SCROLL_HANDLE_WIDTH, SCROLL_HANDLE_HEIGHT);
        poseStack.popPose();
    }

    @Override
    public void tick() {
        this.hoveredStack = ItemStack.EMPTY;
    }

    public void drawContents(GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (slots.isEmpty()) {
            return;
        }
        ingredientCycleTimer.onDraw();

        boolean stopInputDraw = false;

        for (int row = 0; row < visibleRows; row++) {
            final int y = row * SLOT_HEIGHT;

            for (int col = 0; col < columns; col++) {
                final int x = col * SLOT_WIDTH;
                int slotIndex = (row + inputRowOffset) * columns + col;

                if (slotIndex >= slots.size()) {
                    stopInputDraw = true;
                    break;
                }

                var slot = slots.get(slotIndex);
                var ingredients = slot.ingredient().getItems();
                int index = ingredientCycleTimer.getValue(ingredients.length);
                var itemStack = ingredients[index].copyWithCount(slot.count());

                boolean mouseOver = isMouseOverRect(mouseX, mouseY, x, y, SLOT_WIDTH, SLOT_HEIGHT);

                guiGraphics.blit(SLOT_TEXTURE, x, y, mouseOver ? SLOT_WIDTH : 0, 0, SLOT_WIDTH, SLOT_HEIGHT, SLOT_WIDTH * 2, SLOT_WIDTH * 2);
                int itemY = y + 1; // center vertically
                guiGraphics.renderFakeItem(itemStack, x, itemY);
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, x, itemY);

                if (mouseOver) {
                    this.hoveredStack = itemStack;
                }
            }

            if (stopInputDraw)
                break;
        }

        if (!hoveredStack.isEmpty()) {
            guiGraphics.renderTooltip(Minecraft.getInstance().font, hoveredStack, (int) mouseX, (int) mouseY);
        }
    }

    private static boolean isMouseOverRect(double mouseX, double mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }
}
