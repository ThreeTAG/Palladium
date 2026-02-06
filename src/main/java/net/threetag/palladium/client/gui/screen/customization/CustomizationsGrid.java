package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.state.pip.GuiEntityRenderState;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.DyeColor;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.widget.FlatButton;
import net.threetag.palladium.client.gui.widget.grid.AbstractSelectionGrid;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderState;
import net.threetag.palladium.client.renderer.entity.state.PalladiumRenderStateKeys;
import net.threetag.palladium.client.renderer.entity.state.SuitStandRenderState;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.entity.PalladiumEntityTypes;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.network.SelectCustomizationPacket;
import net.threetag.palladium.network.UnselectCustomizationPacket;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.ArrayList;
import java.util.List;

public class CustomizationsGrid extends AbstractSelectionGrid<CustomizationsGrid.Entry> {

    public static final String NO_CUSTOMIZATIONS_LABEL = "gui.palladium.player_customizations.empty";
    public static final String VERY_SAD_LABEL = "gui.palladium.player_customizations.sad_label";
    private final Holder<CustomizationCategory> category;
    private final CustomizationPreview preview;
    private final List<GuiEntityRenderState> drawnEntities = new ArrayList<>();

    public CustomizationsGrid(ScreenRectangle rectangle, Holder<CustomizationCategory> category, Minecraft minecraft) {
        super(minecraft, rectangle.left(), rectangle.top(), rectangle.width(), rectangle.height(), 50, 50, 4);
        this.category = category;
        this.preview = category.value().preview();

        var categoryRegistry = minecraft.level.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY);
        var categoryId = categoryRegistry.getKey(category.value());
        var registry = minecraft.level.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION);

        for (Customization customization : registry) {
            if (customization.getCategoryKey().identifier().equals(categoryId)) {
                var handler = EntityCustomizationHandler.get(minecraft.player);
                var entry = new SelectableEntry(registry.wrapAsHolder(customization), handler.isUnlocked(customization));
                this.addEntry(entry);

                var selectedHolder = handler.get(category);
                if (selectedHolder != null && selectedHolder.value() == customization) {
                    this.setSelected(entry);
                }
            }
        }

        if (!this.children().isEmpty() && !this.category.value().requiresSelection()) {
            var unselect = new UnselectEntry();
            this.addEntryToTop(unselect);
            if (this.getSelected() == null) {
                this.setSelected(unselect);
            }
        }
    }

    @Override
    public void updateSizeAndPosition(int width, int height, int x, int y) {
        super.updateSizeAndPosition(width / 3 * 2, height, x, y);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    protected void renderGridItems(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.drawnEntities.clear();
        super.renderGridItems(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.guiRenderState.submitPicturesInPictureState(new GuiMultiEntityRenderState(this.drawnEntities));

        if (this.children().isEmpty()) {
            guiGraphics.drawCenteredString(this.minecraft.font, Component.translatable(NO_CUSTOMIZATIONS_LABEL), this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() / 2) - 10, RenderUtil.FULL_WHITE);
            guiGraphics.drawCenteredString(this.minecraft.font, Component.translatable(VERY_SAD_LABEL), this.getX() + (this.getWidth() / 2), this.getY() + (this.getHeight() / 2) + 10, RenderUtil.FULL_WHITE);
        }
    }

    public abstract static class Entry extends AbstractSelectionGrid.Entry<Entry> {

        private static final WidgetSprites SPRITES = new WidgetSprites(
                Palladium.id("widget/customization"),
                Palladium.id("widget/customization_disabled"),
                Palladium.id("widget/customization_highlighted")
        );

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    FlatButton.SPRITES.get(this.isUnlocked(), hovering),
                    left,
                    top,
                    width,
                    height,
                    ARGB.white(1F)
            );
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
            if (this.isUnlocked()) {
                this.select();
                return true;
            }

            return super.mouseClicked(event, isDoubleClick);
        }

        @Override
        public boolean keyPressed(KeyEvent event) {
            if (this.isUnlocked()) {
                this.select();
                return true;
            }

            return super.keyPressed(event);
        }

        public abstract boolean isUnlocked();

        public abstract void select();
    }

    public class UnselectEntry extends Entry {

        private static final Identifier ICON_SPRITE = Palladium.id("widget/customization_none");

        @Override
        public boolean isUnlocked() {
            return true;
        }

        @Override
        public void select() {
            CustomizationsGrid.this.setSelected(this);
            CustomizationsGrid.this.minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
            );
            ClientPacketDistributor.sendToServer(new UnselectCustomizationPacket(CustomizationsGrid.this.category));
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            super.render(guiGraphics, index, top, left, width, height, mouseX, mouseY, hovering, partialTick);
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ICON_SPRITE, left, top, width, height, ARGB.white(1F));
        }
    }

    public class SelectableEntry extends Entry {

        private static final Identifier LOCK_TEXTURE = Palladium.id("textures/icon/lock.png");

        private final Holder<Customization> customization;
        private final boolean unlocked;
        private final SuitStandRenderState suitStandPreview;

        public SelectableEntry(Holder<Customization> customization, boolean unlocked) {
            this.customization = customization;
            this.unlocked = unlocked;
            this.suitStandPreview = new SuitStandRenderState();
            this.suitStandPreview.entityType = PalladiumEntityTypes.SUIT_STAND.get();
            this.suitStandPreview.showBasePlate = false;
            this.suitStandPreview.showArms = true;
            this.suitStandPreview.color = DyeColor.WHITE;
            this.suitStandPreview.xRot = 0F;
            this.suitStandPreview.bodyRot = 0F;
            this.suitStandPreview.boundingBoxHeight = 1.8F;
            this.suitStandPreview.setRenderData(PalladiumRenderStateKeys.RENDER_LAYERS, CustomizationPreviewComponent.getRenderLayersForCustomization(customization, DataContext.create()));
        }

        @Override
        public boolean isUnlocked() {
            return this.unlocked;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            super.render(guiGraphics, index, top, left, width, height, mouseX, mouseY, hovering, partialTick);
            PlayerCustomizationScreen.renderEntity(guiGraphics, left, top, left + width, top + height, 20, CustomizationsGrid.this.preview, this.suitStandPreview, CustomizationsGrid.this.drawnEntities::add);

//            if (!this.unlocked) {
//                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, LOCK_TEXTURE, left + width - 18, top + height - 18, 0, 0, 16, 16, 16, 16);
//            }

            if (hovering) {
                guiGraphics.setTooltipForNextFrame(
                        CustomizationsGrid.this.minecraft.font,
                        this.customization.value().getTitle(CustomizationsGrid.this.minecraft.level.registryAccess()),
                        mouseX,
                        mouseY);
            }
        }

        @Override
        public void select() {
            CustomizationsGrid.this.setSelected(this);
            CustomizationsGrid.this.minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
            );
            ClientPacketDistributor.sendToServer(new SelectCustomizationPacket(this.customization));
        }
    }

}
