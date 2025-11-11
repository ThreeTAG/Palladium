package net.threetag.palladium.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.SuitStand;
import net.threetag.palladium.item.recipe.SizedIngredient;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import net.threetag.palladium.menu.TailoringMenu;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.TailoringCraftMessage;
import org.joml.Quaternionf;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

public class TailoringScreen extends AbstractContainerScreen<TailoringMenu> {

    public static final ResourceLocation TEXTURE = Palladium.id("textures/gui/container/tailoring_table.png");
    protected static final ResourceLocation RECIPE_BOOK_TEXTURE = new ResourceLocation("textures/gui/recipe_book.png");
    public static final Quaternionf SUIT_STAND_ANGLE = new Quaternionf().rotationXYZ(0.43633232F, 0.0F, (float) Math.PI);
    private static final Component SEARCH_HINT = Component.translatable("gui.recipebook.search_hint")
            .withStyle(ChatFormatting.ITALIC)
            .withStyle(ChatFormatting.GRAY);
    private static List<TailoringRecipe> AVAILABLE_RECIPES = new ArrayList<>();
    private static int DISPLAYED_RECIPE_INDEX = -1;
    private static TailoringRecipe DISPLAYED_RECIPE;

    private SuitStand suitStandPreview;
    private boolean recipeBookOpen = false;
    private CycleButton cycleBackButton;
    private CycleButton cycleNextButton;
    private RecipeBookButton recipeBookButton;
    private CreateButton createButton;
    private RecipeList recipeList;
    private EditBox searchBox;
    private boolean scrolling;
    private int startIndex;
    private float scrollOffs;
    private int tickCount;

    // To "listen" for changes in the displayed recipe
    private TailoringRecipe cachedDisplayedRecipe = null;

    public TailoringScreen(TailoringMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 232;
        this.imageHeight = 189;
        this.inventoryLabelX = 36;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void init() {
        super.init();

        this.suitStandPreview = new SuitStand(Objects.requireNonNull(this.minecraft).level, 0.0, 0.0, 0.0);
        this.suitStandPreview.setNoBasePlate(true);
        this.suitStandPreview.setShowArms(true);
        this.suitStandPreview.yBodyRot = 210.0F;
        this.suitStandPreview.setXRot(25.0F);
        this.suitStandPreview.yHeadRot = this.suitStandPreview.getYRot();
        this.suitStandPreview.yHeadRotO = this.suitStandPreview.getYRot();

        this.addRenderableWidget(this.cycleBackButton = new CycleButton(this.leftPos + 8, this.topPos + 45, false, button -> cycle(false)));
        this.addRenderableWidget(this.cycleNextButton = new CycleButton(this.leftPos + 70, this.topPos + 45, true, button -> cycle(true)));
        this.addRenderableWidget(this.recipeBookButton = new RecipeBookButton(this.leftPos + 176, this.topPos + 74, button -> toggleRecipeBook()));
        this.addRenderableWidget(this.createButton = new CreateButton(this.leftPos + 105, this.topPos + 74, button -> {
            if (DISPLAYED_RECIPE != null)
                PalladiumNetwork.NETWORK.sendToServer(new TailoringCraftMessage(DISPLAYED_RECIPE.getId()));
        }));

        this.recipeList = new RecipeList(this.minecraft, 131, 126, this.leftPos - 147 + 5, this.topPos + 32, AVAILABLE_RECIPES, TailoringScreen::setSelectRecipe);

        String string = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(this.minecraft.font, this.leftPos - 147 + 23, this.topPos + 14, 108, 9 + 3, Component.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(16777215);
        this.searchBox.setValue(string);
        this.searchBox.setHint(SEARCH_HINT);
        this.searchBox.setResponder(s -> this.recipeList.populate(s));
        this.recipeList.populate(string);

        if (this.recipeBookOpen) {
            this.addRenderableWidget(this.recipeList);
            this.addRenderableWidget(this.searchBox);
        }
    }

    private void toggleRecipeBook() {
        this.recipeBookOpen = !this.recipeBookOpen;

        if (this.recipeBookOpen) {
            this.leftPos = 177 + (this.width - this.imageWidth - 200) / 2;
            this.addRenderableWidget(this.recipeList);
            this.addRenderableWidget(this.searchBox);
        } else {
            this.leftPos = (this.width - this.imageWidth) / 2;
            this.removeWidget(this.recipeList);
            this.removeWidget(this.searchBox);
        }

        this.cycleBackButton.setX(this.leftPos + 8);
        this.cycleNextButton.setX(this.leftPos + 70);
        this.recipeBookButton.setX(this.leftPos + 176);
        this.createButton.setX(this.leftPos + 105);
        this.recipeList.setLeftPos(this.leftPos - 147 + 5);
        this.searchBox.setX(this.leftPos - 147 + 23);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.onRecipeChanged();

        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.tickCount++;
        this.createButton.active = DISPLAYED_RECIPE != null && this.menu.canCraft(this.minecraft.player, DISPLAYED_RECIPE);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.menu.toolSlot.icon = DISPLAYED_RECIPE != null ? DISPLAYED_RECIPE.getToolIcon() : null;

        int i = this.leftPos;
        int j = this.topPos;

        if (this.recipeBookOpen) {
            guiGraphics.blit(RECIPE_BOOK_TEXTURE, i - 147 - 3, j, 1, 1, 147, 166);
        }

        guiGraphics.blit(TEXTURE, i, j, 0, 0, this.imageWidth, this.imageHeight);

        int k = (int) (39.0F * this.scrollOffs);
        guiGraphics.blit(TEXTURE, i + 152, j + 18 + k, 65 + (this.isScrollBarActive() ? 0 : 12), 189, 12, 15);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.suitStandPreview.setItemSlot(slot, ItemStack.EMPTY);
        }

        if (DISPLAYED_RECIPE != null) {
            DISPLAYED_RECIPE.getResults().forEach((slot, stack) -> this.suitStandPreview.setItemSlot(slot, stack));
        }

        InventoryScreen.renderEntityInInventory(guiGraphics, this.leftPos + 45, this.topPos + 80, 30, SUIT_STAND_ANGLE, null, this.suitStandPreview);

        if (DISPLAYED_RECIPE != null) {
            int lastVisible = this.startIndex + 12;
            this.renderButtons(guiGraphics, this.leftPos + 85, this.topPos + 16, lastVisible);
            this.renderRecipes(guiGraphics, this.leftPos + 85, this.topPos + 16, lastVisible);
        }
    }

    private void renderButtons(GuiGraphics guiGraphics, int x, int y, int lastVisibleElementIndex) {
        List<SizedIngredient> list = DISPLAYED_RECIPE.getSizedIngredients();

        for (int i = this.startIndex; i < lastVisibleElementIndex && i < list.size(); ++i) {
            int j = i - this.startIndex;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 2;

            guiGraphics.blit(TEXTURE, k, m, 48, list.get(i).test(this.menu.playerInventory) ? 189 : 207, 16, 18);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int startIndex) {
        List<SizedIngredient> list = DISPLAYED_RECIPE.getSizedIngredients();

        for (int i = this.startIndex; i < startIndex && i < list.size(); ++i) {
            int j = i - this.startIndex;
            int k = x + j % 4 * 16;
            int l = j / 4;
            int m = y + l * 18 + 3;
            var ingredient = list.get(i);
            var stack = ingredient.getDisplayItem(this.tickCount).copyWithCount(ingredient.count());

            guiGraphics.renderItem(stack, k, m);
            guiGraphics.renderItemDecorations(Objects.requireNonNull(this.minecraft).font, stack, k, m);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
        super.renderTooltip(guiGraphics, x, y);
        if (DISPLAYED_RECIPE != null) {
            int i = this.leftPos + 84;
            int j = this.topPos + 16;
            int k = this.startIndex + 12;
            List<SizedIngredient> list = DISPLAYED_RECIPE.getSizedIngredients();

            for (int l = this.startIndex; l < k && l < list.size(); ++l) {
                int m = l - this.startIndex;
                int n = i + m % 4 * 16;
                int o = j + m / 4 * 18 + 2;
                if (x >= n && x < n + 16 && y >= o && y < o + 18) {
                    guiGraphics.renderTooltip(this.font, list.get(l).getDisplayItem(this.tickCount), x, y);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.recipeList.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }

        this.scrolling = false;
        if (DISPLAYED_RECIPE != null) {
            int i = this.leftPos + 152;
            int j = this.topPos + 18;
            if (mouseX >= (double) i && mouseX < (double) (i + 12) && mouseY >= (double) j && mouseY < (double) (j + 54)) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.recipeList.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
            return true;
        } else if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + 14;
            int j = i + 54;
            this.scrollOffs = ((float) mouseY - (float) i - 7.5F) / ((float) (j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) this.getOffscreenRows()) + 0.5) * 4;
            return true;
        } else {
            return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        if (this.isScrollBarActive() & this.isMouseOverIngredients(mouseX, mouseY)) {
            int i = this.getOffscreenRows();
            float f = (float) delta / (float) i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int) ((double) (this.scrollOffs * (float) i) + 0.5) * 4;
        } else if (this.recipeList.isMouseOver(mouseX, mouseY)) {
            this.recipeList.mouseScrolled(mouseX, mouseY, delta);
        }

        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.searchBox.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (this.searchBox.isFocused() && this.searchBox.isVisible() && keyCode != 256) {
            return true;
        } else if (Objects.requireNonNull(this.minecraft).options.keyChat.matches(keyCode, scanCode) && !this.searchBox.isFocused()) {
            this.searchBox.setFocused(true);
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    public boolean isMouseOverIngredients(double mouseX, double mouseY) {
        int i = this.leftPos;
        int j = this.topPos;

        return mouseX >= i + 84 && mouseY >= j + 17 && mouseX <= i + 84 + 81 && mouseY <= mouseY + 17 + 56;
    }

    private void onRecipeChanged() {
        if (this.cachedDisplayedRecipe == DISPLAYED_RECIPE) {
            return;
        }

        // Reset scrolling when the displayed recipe changes
        this.scrollOffs = 0.0F;
        this.startIndex = 0;

        this.cachedDisplayedRecipe = DISPLAYED_RECIPE;
    }

    protected int getOffscreenRows() {
        return (DISPLAYED_RECIPE.getSizedIngredients().size() + 4 - 1) / 4 - 3;
    }

    private boolean isScrollBarActive() {
        return DISPLAYED_RECIPE != null && DISPLAYED_RECIPE.getSizedIngredients().size() > 12;
    }

    public static void setAvailableRecipes(List<TailoringRecipe> recipes) {
        AVAILABLE_RECIPES = recipes;

        if (!AVAILABLE_RECIPES.isEmpty() && DISPLAYED_RECIPE == null) {
            DISPLAYED_RECIPE = AVAILABLE_RECIPES.get(0);
            DISPLAYED_RECIPE_INDEX = 0;
        } else if (!AVAILABLE_RECIPES.contains(DISPLAYED_RECIPE)) {
            DISPLAYED_RECIPE = null;
            DISPLAYED_RECIPE_INDEX = -1;
        } else {
            DISPLAYED_RECIPE_INDEX = AVAILABLE_RECIPES.indexOf(DISPLAYED_RECIPE);
        }
    }

    public static void cycle(boolean next) {
        if (AVAILABLE_RECIPES.isEmpty()) {
            DISPLAYED_RECIPE_INDEX = -1;
            DISPLAYED_RECIPE = null;
        } else {
            if (next) {
                DISPLAYED_RECIPE_INDEX++;

                if (DISPLAYED_RECIPE_INDEX >= AVAILABLE_RECIPES.size()) {
                    DISPLAYED_RECIPE_INDEX = 0;
                }
            } else {
                DISPLAYED_RECIPE_INDEX--;

                if (DISPLAYED_RECIPE_INDEX < 0) {
                    DISPLAYED_RECIPE_INDEX = AVAILABLE_RECIPES.size() - 1;
                }
            }

            DISPLAYED_RECIPE = DISPLAYED_RECIPE_INDEX >= 0 ? AVAILABLE_RECIPES.get(DISPLAYED_RECIPE_INDEX) : null;
        }
    }

    public static void setSelectRecipe(TailoringRecipe recipe) {
        if (AVAILABLE_RECIPES.contains(recipe)) {
            DISPLAYED_RECIPE = recipe;
            DISPLAYED_RECIPE_INDEX = AVAILABLE_RECIPES.indexOf(recipe);
        } else {
            DISPLAYED_RECIPE_INDEX = -1;
            DISPLAYED_RECIPE = null;
        }
    }

    private static class CycleButton extends Button {

        private final boolean next;

        protected CycleButton(int x, int y, boolean next, OnPress onPress) {
            super(x, y, 11, 17, Component.empty(), onPress, DEFAULT_NARRATION);
            this.next = next;
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            this.active = !AVAILABLE_RECIPES.isEmpty();

            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), this.next ? 0 : 14, !this.active ? 225 : this.isHovered ? 207 : 189, this.getWidth(), this.getHeight());
        }
    }

    private static class RecipeBookButton extends Button {

        protected RecipeBookButton(int x, int y, OnPress onPress) {
            super(x, y, 20, 18, Component.empty(), onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 27, this.isHovered ? 208 : 189, this.getWidth(), this.getHeight());
        }
    }

    private static class CreateButton extends Button {

        protected CreateButton(int x, int y, OnPress onPress) {
            super(x, y, 60, 18, Component.translatable("container.palladium.tailoring.craft"), onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), 90, this.active ? (this.isHovered ? 225 : 189) : 207, this.getWidth(), this.getHeight());
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            int i = this.active ? 16777215 : 10526880;
            this.renderString(guiGraphics, Minecraft.getInstance().font, i | Mth.ceil(this.alpha * 255.0F) << 24);
        }
    }

    private static class RecipeList extends AbstractSelectionList<RecipeListEntry> {

        private final int listWidth;
        private final List<TailoringRecipe> recipes;
        private final Consumer<TailoringRecipe> onClick;

        public RecipeList(Minecraft minecraft, int width, int height, int x, int y, List<TailoringRecipe> recipes, Consumer<TailoringRecipe> onClick) {
            super(minecraft, width, height, y, y + height, 16);
            this.setLeftPos(x);
            this.setRenderBackground(false);
            this.setRenderTopAndBottom(false);
            this.listWidth = width;
            this.recipes = recipes;
            this.onClick = onClick;
        }

        public void populate(String search) {
            this.clearEntries();

            for (TailoringRecipe recipe : this.recipes) {
                if (search == null || search.isEmpty() || recipe.getTitle().getString().toLowerCase(Locale.ROOT).contains(search)) {
                    this.addEntry(new RecipeListEntry(recipe, this.onClick));
                }
            }
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {

        }

        @Override
        public int getRowWidth() {
            return this.listWidth - 15;
        }

        @Override
        protected int getScrollbarPosition() {
            return this.x0 + this.listWidth - 6;
        }

        @Override
        protected boolean isSelectedItem(int index) {
            return this.getEntry(index).recipe == DISPLAYED_RECIPE;
        }
    }

    private static class RecipeListEntry extends AbstractSelectionList.Entry<RecipeListEntry> {

        private final TailoringRecipe recipe;
        private final Consumer<TailoringRecipe> onClick;

        private RecipeListEntry(TailoringRecipe recipe, Consumer<TailoringRecipe> onClick) {
            this.recipe = recipe;
            this.onClick = onClick;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.drawString(Minecraft.getInstance().font, this.recipe.getTitle(), left + 1, top + 2, 0xffffff, false);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.onClick.accept(this.recipe);
            return true;
        }
    }
}
