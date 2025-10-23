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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.component.grid.AbstractSelectionGrid;
import net.threetag.palladium.client.gui.pip.GuiMultiEntityRenderState;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.entity.SuitStand;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.network.SelectCustomizationPacket;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.ArrayList;
import java.util.List;

public class CustomizationsGrid extends AbstractSelectionGrid<CustomizationsGrid.Entry> {

    private final CustomizationCategory slot;
    private final CustomizationPreview preview;
    private final List<GuiEntityRenderState> drawnEntities = new ArrayList<>();

    public CustomizationsGrid(ScreenRectangle rectangle, CustomizationCategory slot, Minecraft minecraft) {
        super(minecraft, rectangle.left(), rectangle.top(), rectangle.width(), rectangle.height(), 50, 50, 4);
        this.slot = slot;
        this.preview = slot.preview();

        var slotRegistry = minecraft.level.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY);
        var slotId = slotRegistry.getKey(slot);
        var registry = minecraft.level.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION);

        for (Customization customization : registry) {
            if (customization.getCategoryKey().location().equals(slotId)) {
                var handler = EntityCustomizationHandler.get(minecraft.player);
                var entry = new Entry(registry.wrapAsHolder(customization), handler.isUnlocked(customization));
                this.addEntry(entry);

                var selectedHolder = handler.get(slotRegistry.wrapAsHolder(slot));
                if (selectedHolder != null && selectedHolder.value() == customization) {
                    this.setSelected(entry);
                }
            }
        }
    }

    public void tick(boolean gamePaused) {
        if (gamePaused) {
            ClientEntityRenderLayers.get(this.minecraft.player, PalladiumEntityDataTypes.RENDER_LAYERS.get()).tick();
        }

        for (Entry child : this.children()) {
            ClientEntityRenderLayers.get(child.suitStandPreview, PalladiumEntityDataTypes.RENDER_LAYERS.get()).tick();
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
    }

    public class Entry extends AbstractSelectionGrid.Entry<Entry> {

        private static final WidgetSprites SPRITES = new WidgetSprites(
                Palladium.id("widget/customization"),
                Palladium.id("widget/customization_disabled"),
                Palladium.id("widget/customization_highlighted")
        );
        private static final ResourceLocation LOCK_TEXTURE = Palladium.id("textures/icon/lock.png");

        private final Holder<Customization> customization;
        private final boolean unlocked;
        private final SuitStand suitStandPreview;

        public Entry(Holder<Customization> customization, boolean unlocked) {
            this.customization = customization;
            this.unlocked = unlocked;
            this.suitStandPreview = new SuitStand(CustomizationsGrid.this.minecraft.level, 0.0, 0.0, 0.0);
            this.suitStandPreview.setNoBasePlate(true);
            this.suitStandPreview.setShowArms(true);
            this.suitStandPreview.yBodyRot = 0F;
            this.suitStandPreview.setXRot(0F);
            this.suitStandPreview.yHeadRot = this.suitStandPreview.getYRot();
            this.suitStandPreview.yHeadRotO = this.suitStandPreview.getYRot();
            EntityCustomizationHandler.get(this.suitStandPreview).select(this.customization);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            guiGraphics.blitSprite(
                    RenderPipelines.GUI_TEXTURED,
                    SPRITES.get(this.unlocked, hovering),
                    left,
                    top,
                    width,
                    height,
                    ARGB.white(1F)
            );

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
        public boolean mouseClicked(MouseButtonEvent event, boolean isDoubleClick) {
            if (this.unlocked) {
                this.select();
                return true;
            }

            return super.mouseClicked(event, isDoubleClick);
        }

        @Override
        public boolean keyPressed(KeyEvent event) {
            if (this.unlocked) {
                this.select();
                return true;
            }

            return super.keyPressed(event);
        }

        private void select() {
            CustomizationsGrid.this.minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
            );
            ClientPacketDistributor.sendToServer(new SelectCustomizationPacket(this.customization));

            if (CustomizationsGrid.this.getSelected() == this) {
                CustomizationsGrid.this.setSelected(null);
            } else {
                CustomizationsGrid.this.setSelected(this);
            }
        }
    }

}
