package net.threetag.palladium.client.gui.screen.menu;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.item.recipe.PalladiumRecipeTypes;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;

@EventBusSubscriber(modid = Palladium.MOD_ID, value = Dist.CLIENT)
public class ClientTailoringRecipeBook implements Renderable, GuiEventListener, NarratableEntry {

    protected static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/gui/recipe_book.png");
    private static BiMap<ResourceKey<Recipe<?>>, TailoringRecipe> ALL_RECIPES = HashBiMap.create();
    private static List<TailoringRecipe> AVAILABLE_RECIPES = new ArrayList<>();
    private static TailoringRecipe SELECTED_RECIPE = null;
    private static boolean OPENED = false;
    private static final Component SEARCH_HINT = Component.translatable("gui.recipebook.search_hint")
            .withStyle(ChatFormatting.ITALIC)
            .withStyle(ChatFormatting.GRAY);
    private static final int WIDTH = 147;
    private static final int HEIGHT = 166;
    private static final int X_OFFSET = 2;

    private final TailoringScreen tailoringScreen;
    private EditBox searchBox;
    private RecipeList recipeList;
    private int width, height;

    public ClientTailoringRecipeBook(TailoringScreen tailoringScreen) {
        this.tailoringScreen = tailoringScreen;
    }

    public ClientTailoringRecipeBook init(int width, int height, int imageWidth) {
        this.width = width;
        this.height = height;

        var minecraft = Minecraft.getInstance();
        String string = this.searchBox != null ? this.searchBox.getValue() : "";
        this.searchBox = new EditBox(minecraft.font, this.getXOrigin() + 25, this.getYOrigin() + 14, 108, 9 + 3, Component.translatable("itemGroup.search"));
        this.searchBox.setMaxLength(50);
        this.searchBox.setVisible(true);
        this.searchBox.setTextColor(RenderUtil.FULL_WHITE);
        this.searchBox.setValue(string);
        this.searchBox.setHint(SEARCH_HINT);
        this.searchBox.setResponder(s -> this.recipeList.populate(s));

        this.recipeList = new RecipeList(minecraft, 131, 126, this.getXOrigin() + 7, this.getYOrigin() + 32, AVAILABLE_RECIPES, this::setSelectedRecipe);
        this.recipeList.populate(null);

        this.setSelectedRecipe(SELECTED_RECIPE);

        return this;
    }

    public void close() {
        OPENED = false;
        this.removeWidgets();
    }

    public void open() {
        OPENED = true;
        this.searchBox.setX(this.getXOrigin() + 25);
        this.recipeList.setX(this.getXOrigin() + 7);
        this.recipeList.populate(null);

        this.addWidgets();
    }

    public void toggleVisibility() {
        if (this.isOpened()) {
            this.close();
        } else {
            this.open();
        }
    }

    public int updateScreenPosition(int width, int imageWidth, boolean visible) {
        int i;
        if (visible) {
            i = ((width - (imageWidth + WIDTH + X_OFFSET)) / 2) + WIDTH + X_OFFSET;
        } else {
            i = (width - imageWidth) / 2;
        }

        return i;
    }

    public boolean isOpened() {
        return OPENED;
    }

    public void addWidgets() {
        this.tailoringScreen.addRenderableWidget(this.searchBox);
        this.tailoringScreen.addRenderableWidget(this.recipeList);
    }

    public void removeWidgets() {
        this.tailoringScreen.removeWidget(this.searchBox);
        this.tailoringScreen.removeWidget(this.recipeList);
    }

    private int getYOrigin() {
        return this.tailoringScreen.getGuiTop();
    }

    private int getXOrigin() {
        return this.tailoringScreen.getGuiLeft() - WIDTH - X_OFFSET;
    }

    public void setSelectedRecipe(TailoringRecipe recipe) {
        SELECTED_RECIPE = recipe;
        this.tailoringScreen.onRecipeSelected(recipe);
    }

    public TailoringRecipe getSelectedRecipe() {
        return SELECTED_RECIPE;
    }

    public ResourceKey<Recipe<?>> getSelectedRecipeKey() {
        return SELECTED_RECIPE != null ? ALL_RECIPES.inverse().get(SELECTED_RECIPE) : null;
    }

    public List<TailoringRecipe> getAvailableRecipes() {
        return AVAILABLE_RECIPES;
    }

    public void cycle(boolean forward) {
        int index = AVAILABLE_RECIPES.indexOf(SELECTED_RECIPE);

        if (forward) {
            index = index >= AVAILABLE_RECIPES.size() - 1 ? 0 : index + 1;
        } else {
            index = index <= 0 ? AVAILABLE_RECIPES.size() - 1 : index - 1;
        }

        this.setSelectedRecipe(AVAILABLE_RECIPES.get(index));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.isOpened()) {
            int leftPos = this.getXOrigin();
            int topPos = this.getYOrigin();
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, leftPos, topPos, 1, 1, WIDTH, HEIGHT, 256, 256);
        }
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return this.isOpened() ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        return this.isOpened() && this.recipeList.mouseDragged(event, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.isOpened() && this.recipeList.isMouseOver(mouseX, mouseY)) {
            return this.recipeList.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        } else {
            return false;
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if(!this.isOpened()) {
            return false;
        } else if (this.searchBox.keyPressed(event)) {
            return true;
        } else if (this.searchBox.isFocused() && this.searchBox.isVisible() && event.key() != 256) {
            return true;
        } else if (Minecraft.getInstance().options.keyChat.matches(event) && !this.searchBox.isFocused()) {
            this.searchBox.setFocused(true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        if (this.isOpened() && this.searchBox.charTyped(event)) {
            return true;
        }

        return GuiEventListener.super.charTyped(event);
    }

    public static void setAvailableRecipes(List<ResourceKey<Recipe<?>>> recipes) {
        AVAILABLE_RECIPES.clear();
        AVAILABLE_RECIPES.addAll(recipes
                .stream()
                .map(r -> ALL_RECIPES.get(r))
                .sorted((o1, o2) -> {
                    var category1 = TailoringRecipe.getCategoryTitle(o1.categoryId().orElse(null));
                    var category2 = TailoringRecipe.getCategoryTitle(o2.categoryId().orElse(null));

                    if (category1 != null && category2 == null) {
                        return -1;
                    } else if (category1 == null && category2 != null) {
                        return 1;
                    } else if (category1 != null) {
                        if (category1.getString().equals(category2.getString())) {
                            return o1.title().getString().compareToIgnoreCase(o2.title().getString());
                        } else {
                            return category1.getString().compareToIgnoreCase(category2.getString());
                        }
                    } else {
                        return o1.title().getString().compareToIgnoreCase(o2.title().getString());
                    }
                }).toList());

        if (SELECTED_RECIPE != null && !AVAILABLE_RECIPES.contains(SELECTED_RECIPE)) {
            SELECTED_RECIPE = null;
        }

        if (SELECTED_RECIPE == null && !AVAILABLE_RECIPES.isEmpty()) {
            SELECTED_RECIPE = AVAILABLE_RECIPES.getFirst();
        }
    }

    @SubscribeEvent
    static void recipesReceived(RecipesReceivedEvent e) {
        ALL_RECIPES.clear();
        for (RecipeHolder<TailoringRecipe> recipeHolder : e.getRecipeMap().byType(PalladiumRecipeTypes.TAILORING.get())) {
            ALL_RECIPES.put(recipeHolder.id(), recipeHolder.value());
        }
        SELECTED_RECIPE = null;
    }

    @SubscribeEvent
    static void clientLogOut(ClientPlayerNetworkEvent.LoggingOut e) {
        ALL_RECIPES.clear();
        AVAILABLE_RECIPES.clear();
        SELECTED_RECIPE = null;
    }

    private class RecipeList extends AbstractSelectionList<RecipeListEntry> {

        private final int listWidth;
        private final List<TailoringRecipe> recipes;
        private final Consumer<TailoringRecipe> onClick;

        public RecipeList(Minecraft minecraft, int width, int height, int x, int y, List<TailoringRecipe> recipes, Consumer<TailoringRecipe> onClick) {
            super(minecraft, width, height, y, 16);
            this.setX(x);
            this.listWidth = width;
            this.recipes = recipes;
            this.onClick = onClick;
        }

        @Override
        protected void renderListBackground(GuiGraphics guiGraphics) {

        }

        public void populate(String search) {
            this.clearEntries();
            Identifier lastCategoryId = null;

            for (TailoringRecipe recipe : this.recipes) {
                if (search == null || search.isEmpty() || recipe.title().getString().toLowerCase(Locale.ROOT).contains(search)) {
                    if (!Objects.equals(recipe.categoryId().orElse(null), lastCategoryId)) {
                        if (lastCategoryId != null && recipe.categoryId().isEmpty()) {
                            this.addEntry(new RecipeListRecipeCategory(Component.literal("-----")));
                        } else {
                            this.addEntry(new RecipeListRecipeCategory(TailoringRecipe.getCategoryTitle(recipe.categoryId().get())));
                        }
                        lastCategoryId = recipe.categoryId().orElse(null);
                    }

                    this.addEntry(new RecipeListRecipeEntry(recipe, this.onClick));
                }
            }

            var selected = this.getSelected();
            if (selected != null) {
                this.scrollToEntry(selected);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

        }

        @Override
        public int getRowWidth() {
            return this.listWidth - 15;
        }

        @Override
        protected int scrollBarX() {
            return this.getX() + this.listWidth - 6;
        }

        @Override
        public @Nullable RecipeListEntry getSelected() {
            return this.children().stream().filter(e -> e instanceof RecipeListRecipeEntry r && r.recipe == SELECTED_RECIPE).findFirst().orElse(null);
        }

        @Override
        public void setSelected(@Nullable ClientTailoringRecipeBook.RecipeListEntry selected) {
            if (selected instanceof RecipeListRecipeEntry e) {
                SELECTED_RECIPE = e.recipe;
            }
        }
    }

    private abstract static class RecipeListEntry extends AbstractSelectionList.Entry<RecipeListEntry> {

        public abstract boolean isSelected();

    }

    private static class RecipeListRecipeEntry extends RecipeListEntry {

        private final TailoringRecipe recipe;
        private final Consumer<TailoringRecipe> onClick;

        private RecipeListRecipeEntry(TailoringRecipe recipe, Consumer<TailoringRecipe> onClick) {
            this.recipe = recipe;
            this.onClick = onClick;
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            var text = this.recipe.title();
            var font = Minecraft.getInstance().font;
            int x = this.getContentX();
            int y = this.getContentY();

            guiGraphics.drawScrollingString(guiGraphics.textRenderer(), font, text, x + 17, this.getContentRight(), y + 2);

            for (ArmorType type : ArmorType.values()) {
                var stack = this.recipe.getResults().getOrDefault(type, ItemStack.EMPTY);

                if (!stack.isEmpty()) {
                    guiGraphics.renderItem(stack, x, y - 2);
                    break;
                }
            }
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
            this.onClick.accept(this.recipe);
            return true;
        }

        @Override
        public boolean isSelected() {
            return this.recipe == SELECTED_RECIPE;
        }

    }

    private static class RecipeListRecipeCategory extends RecipeListEntry {

        private final Component title;

        private RecipeListRecipeCategory(Component title) {
            this.title = title.copy().withStyle(ChatFormatting.GRAY, ChatFormatting.BOLD);
        }

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            var font = Minecraft.getInstance().font;
            int x = this.getContentX();
            int y = this.getContentY();

            guiGraphics.drawScrollingString(guiGraphics.textRenderer(), font, this.title, x - 6, this.getContentRight(), y + 2);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
            return false;
        }

        @Override
        public boolean isSelected() {
            return false;
        }
    }
}
