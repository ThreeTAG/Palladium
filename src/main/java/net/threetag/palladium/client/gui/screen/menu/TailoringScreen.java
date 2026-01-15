package net.threetag.palladium.client.gui.screen.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.item.equipment.ArmorType;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.component.FlatButton;
import net.threetag.palladium.client.renderer.entity.state.SuitStandRenderState;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.entity.SuitStand;
import net.threetag.palladium.item.recipe.TailoringRecipe;
import net.threetag.palladium.menu.TailoringMenu;
import net.threetag.palladium.util.RecipeUtil;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class TailoringScreen extends AbstractContainerScreen<TailoringMenu> {

    public static final Identifier TEXTURE = Palladium.id("textures/gui/container/tailoring.png");
    private static final Vector3f SUIT_STAND_TRANSLATION = new Vector3f(0.0F, 0.9F, 0.0F);
    private static final Quaternionf SUIT_STAND_ANGLE = new Quaternionf().rotationXYZ(0.43633232F, 0.0F, (float) Math.PI);
    private static final WidgetSprites PAGE_FORWARD_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/page_forward"), Identifier.withDefaultNamespace("recipe_book/page_forward_highlighted")
    );
    private static final WidgetSprites PAGE_BACKWARD_SPRITES = new WidgetSprites(
            Identifier.withDefaultNamespace("recipe_book/page_backward"), Identifier.withDefaultNamespace("recipe_book/page_backward_highlighted")
    );
    private static final Identifier SCROLLER_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller");
    private static final Identifier SCROLLER_DISABLED_SPRITE = Identifier.withDefaultNamespace("container/stonecutter/scroller_disabled");
    private static final Identifier INGREDIENT_AVAILABLE_SPRITE = Palladium.id("container/tailoring/ingredient_available");
    private static final Identifier INGREDIENT_UNAVAILABLE_SPRITE = Palladium.id("container/tailoring/ingredient_unavailable");
    private static final int SCROLLER_X = 152;
    private static final int SCROLLER_Y = 18;
    private static final int SCROLLER_WIDTH = 12;
    private static final int SCROLLER_HEIGHT = 15;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int SCROLLER_FULL_HEIGHT = 54;
    private static final int RECIPES_X = 85;
    private static final int RECIPES_Y = 17;

    private ImageButton forwardButton;
    private ImageButton backButton;
    private ImageButton craftButton;
    private ClientTailoringRecipeBook recipeBook;
    private ImageButton recipeBookButton;
    @Nullable
    private SuitStandRenderState suitStandPreview;
    private float scrollOffs;
    private boolean scrolling;
    private int startIndex;

    public TailoringScreen(TailoringMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 232;
        this.imageHeight = 189;
        this.inventoryLabelX = 36;
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        this.craftButton.active = this.recipeBook.getSelectedRecipe() != null && this.menu.canCraft(this.recipeBook.getSelectedRecipe());
        this.menu.getToolSlot().icon = this.recipeBook.getSelectedRecipe() != null ? this.recipeBook.getSelectedRecipe().toolIcon().orElse(null) : null;
    }

    @Override
    protected void init() {
        super.init();

        this.suitStandPreview = new SuitStandRenderState();
        this.suitStandPreview.entityType = PalladiumEntityTypes.SUIT_STAND.get();
        this.suitStandPreview.showBasePlate = false;
        this.suitStandPreview.showArms = true;
        this.suitStandPreview.color = DyeColor.WHITE;
        this.suitStandPreview.xRot = 25.0F;
        this.suitStandPreview.bodyRot = 210.0F;
        this.suitStandPreview.headPose = SuitStand.DEFAULT_HEAD_POSE;
        this.suitStandPreview.bodyPose = SuitStand.DEFAULT_BODY_POSE;
        this.suitStandPreview.rightArmPose = SuitStand.DEFAULT_RIGHT_ARM_POSE;
        this.suitStandPreview.leftArmPose = SuitStand.DEFAULT_LEFT_ARM_POSE;
        this.suitStandPreview.rightLegPose = SuitStand.DEFAULT_RIGHT_LEG_POSE;
        this.suitStandPreview.leftLegPose = SuitStand.DEFAULT_LEFT_LEG_POSE;

        this.addRenderableWidget(this.recipeBook = new ClientTailoringRecipeBook(this));
        this.leftPos = this.recipeBook.updateScreenPosition(this.width, this.imageWidth, this.recipeBook.isOpened());
        this.recipeBook.init(this.width, this.height, this.imageWidth);

        this.addRenderableWidget(this.backButton = new ImageButton(this.leftPos + 7, this.topPos + 45, 12, 17, PAGE_BACKWARD_SPRITES, button -> this.recipeBook.cycle(false)));
        this.addRenderableWidget(this.forwardButton = new ImageButton(this.leftPos + 70, this.topPos + 45, 12, 17, PAGE_FORWARD_SPRITES, button -> this.recipeBook.cycle(true)));
        this.addRenderableWidget(this.craftButton = new FlatButton(this.leftPos + 105, this.topPos + 74, 60, 18, button -> this.menu.craft(this.recipeBook.getSelectedRecipeKey()), Component.translatable("container.palladium.tailoring.craft")));

        this.addRenderableWidget(this.recipeBookButton = new ImageButton(this.leftPos + 176, this.topPos + 74, 20, 18, RecipeBookComponent.RECIPE_BUTTON_SPRITES, b -> {
            this.leftPos = this.recipeBook.updateScreenPosition(this.width, this.imageWidth, !this.recipeBook.isOpened());
            this.recipeBook.toggleVisibility();
            this.setWidgetsPositions();
        }));

        this.setWidgetsPositions();

        if (this.recipeBook.isOpened()) {
            this.recipeBook.addWidgets();
        }
    }

    protected void setWidgetsPositions() {
        this.recipeBookButton.setX(this.leftPos + 176);
        this.backButton.setX(this.leftPos + 7);
        this.forwardButton.setX(this.leftPos + 70);
        this.craftButton.setX(this.leftPos + 105);
    }

    public void onRecipeSelected(TailoringRecipe recipe) {
        this.scrollOffs = 0.0F;
        this.startIndex = 0;

        if (this.suitStandPreview != null) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                RenderUtil.setItemInHumanoidRenderStateSlot(this.suitStandPreview, slot, ItemStack.EMPTY);
            }

            if (recipe != null) {
                for (Map.Entry<ArmorType, ItemStack> e : recipe.getResults().entrySet()) {
                    RenderUtil.setItemInHumanoidRenderStateSlot(this.suitStandPreview, e.getKey().getSlot(), e.getValue());
                }
            }
        }
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

        // Ingredients
        int k = (int) ((SCROLLER_FULL_HEIGHT - SCROLLER_HEIGHT) * this.scrollOffs);
        Identifier Identifier = this.isScrollBarActive() ? SCROLLER_SPRITE : SCROLLER_DISABLED_SPRITE;
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier, this.leftPos + SCROLLER_X, this.topPos + SCROLLER_Y + k, SCROLLER_WIDTH, SCROLLER_HEIGHT);
        int l = this.leftPos + RECIPES_X;
        int i1 = this.topPos + RECIPES_Y;
        int j1 = this.startIndex + 12;
        this.renderButtons(guiGraphics, l, i1, j1);
        this.renderRecipes(guiGraphics, l, i1, j1);

        // Suit Stand Preview
        if (this.suitStandPreview != null) {
            guiGraphics.submitEntityRenderState(this.suitStandPreview, 30F, SUIT_STAND_TRANSLATION,
                    SUIT_STAND_ANGLE, null, this.leftPos + 8, this.topPos + 18,
                    this.leftPos + 81, this.topPos + 91);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);
        if (this.recipeBook.getSelectedRecipe() != null) {
            int i = this.leftPos + RECIPES_X;
            int j = this.topPos + RECIPES_Y;
            int k = this.startIndex + 12;
            List<SizedIngredient> ingredients = this.recipeBook.getSelectedRecipe().getIngredients();

            for (int l = this.startIndex; l < k && l < ingredients.size(); l++) {
                int i1 = l - this.startIndex;
                int j1 = i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
                int k1 = j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 2;
                if (mouseX >= j1 && mouseX < j1 + RECIPES_IMAGE_SIZE_WIDTH && mouseY >= k1 && mouseY < k1 + RECIPES_IMAGE_SIZE_HEIGHT) {
                    ContextMap contextmap = SlotDisplayContext.fromLevel(this.minecraft.level);
                    SlotDisplay slotdisplay = ingredients.get(l).ingredient().display();
                    guiGraphics.setTooltipForNextFrame(this.font, slotdisplay.resolveForFirstStack(contextmap), mouseX, mouseY);
                }
            }
        }
    }

    private void renderButtons(GuiGraphics guiGraphics, int x, int y, int lastVisibleElementIndex) {
        for (int i = this.startIndex; i < lastVisibleElementIndex && i < this.recipeBook.getSelectedRecipe().getIngredients().size(); i++) {
            int j = i - this.startIndex;
            int k = x + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int l = j / RECIPES_COLUMNS;
            int i1 = y + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            Identifier Identifier;
            var ingredient = this.recipeBook.getSelectedRecipe().getIngredients().get(i);

            if (RecipeUtil.checkSizedIngredientInContainer(ingredient, this.menu.playerInventory)) {
                Identifier = INGREDIENT_AVAILABLE_SPRITE;
            } else {
                Identifier = INGREDIENT_UNAVAILABLE_SPRITE;
            }

            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier, k, i1 - 1, RECIPES_IMAGE_SIZE_WIDTH, RECIPES_IMAGE_SIZE_HEIGHT);
        }
    }

    private void renderRecipes(GuiGraphics guiGraphics, int x, int y, int startIndex) {
        List<SizedIngredient> ingredients = this.recipeBook.getSelectedRecipe().getIngredients();
        ContextMap contextmap = SlotDisplayContext.fromLevel(this.minecraft.level);

        for (int i = this.startIndex; i < startIndex && i < ingredients.size(); i++) {
            int j = i - this.startIndex;
            int k = x + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int l = j / RECIPES_COLUMNS;
            int i1 = y + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            var ingredient = ingredients.get(i);
            var display = ingredient.ingredient().display();
            var stack = display.resolveForFirstStack(contextmap);
            stack.setCount(ingredient.count());
            guiGraphics.renderItem(stack, k, i1);
            guiGraphics.renderItemDecorations(this.minecraft.font, stack, k, i1);
        }
    }

    private boolean isScrollBarActive() {
        return this.recipeBook.getSelectedRecipe() != null && this.recipeBook.getSelectedRecipe().getIngredients().size() > 12;
    }

    protected int getOffscreenRows() {
        return (this.recipeBook.getSelectedRecipe().getIngredients().size() + RECIPES_COLUMNS - 1) / RECIPES_COLUMNS - RECIPES_ROWS;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
        this.scrolling = false;
        if (this.recipeBook.getSelectedRecipe() != null) {
            int i = this.leftPos + SCROLLER_X;
            int j = this.topPos + SCROLLER_Y;

            if (event.x() >= i && event.x() < i + SCROLLER_WIDTH && event.y() >= j && event.y() < j + SCROLLER_FULL_HEIGHT) {
                this.scrolling = true;
            }
        }

        return super.mouseClicked(event, isDoubleClick);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double mouseX, double mouseY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + SCROLLER_Y;
            int j = i + SCROLLER_FULL_HEIGHT;
            this.scrollOffs = ((float) event.y() - i - 7.5F) / (j - i - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int) (this.scrollOffs * this.getOffscreenRows() + 0.5) * 4;
            return true;
        }

        return this.recipeBook.mouseDragged(event, mouseX, mouseY) || super.mouseDragged(event, mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (!this.recipeBook.mouseScrolled(mouseX, mouseY, scrollX, scrollY) && !super.mouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
            if (this.isScrollBarActive()) {
                int i = this.getOffscreenRows();
                float f = (float) scrollY / i;
                this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
                this.startIndex = (int) (this.scrollOffs * i + 0.5) * 4;
            }
        }
        return true;
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return this.recipeBook.keyPressed(event) || super.keyPressed(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        return this.recipeBook.charTyped(event) || super.charTyped(event);
    }
}
