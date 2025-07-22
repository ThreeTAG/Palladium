package net.threetag.palladium.client.gui.screen.customization;

import dev.architectury.networking.NetworkManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.Customization;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.EntityCustomizationHandler;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.client.gui.component.grid.AbstractSelectionGrid;
import net.threetag.palladium.client.renderer.entity.layer.ClientEntityRenderLayers;
import net.threetag.palladium.entity.SuitStand;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.network.SelectCustomizationPacket;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class CustomizationsGrid extends AbstractSelectionGrid<CustomizationsGrid.Entry> {

    private final CustomizationCategory slot;
    private final PoseStackTransformation preview;

    public CustomizationsGrid(ScreenRectangle rectangle, CustomizationCategory slot, Minecraft minecraft) {
        super(minecraft, rectangle.left(), rectangle.top(), rectangle.width(), rectangle.height(), 50, 50, 4);
        this.slot = slot;
        this.preview = slot.preview().invertYRot();

        var slotRegistry = minecraft.level.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY);
        var slotId = slotRegistry.getKey(slot);
        var registry = minecraft.level.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION);

        for (Customization customization : registry) {
            if (customization.getSlot().location().equals(slotId)) {
                var entry = new Entry(registry.wrapAsHolder(customization));
                this.addEntry(entry);

                var selectedHolder = EntityCustomizationHandler.get(minecraft.player).get(slotRegistry.wrapAsHolder(slot));
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

    public class Entry extends AbstractSelectionGrid.Entry<Entry> {

        private static final WidgetSprites SPRITES = new WidgetSprites(
                Palladium.id("widget/customization"),
                Palladium.id("widget/customization_disabled"),
                Palladium.id("widget/customization_highlighted")
        );

        private final Holder<Customization> customization;
        private final SuitStand suitStandPreview;

        public Entry(Holder<Customization> customization) {
            this.customization = customization;
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
                    RenderType::guiTextured,
                    SPRITES.get(true, hovering),
                    left,
                    top,
                    width,
                    height,
                    0xFFFFFFFF
            );

            guiGraphics.enableScissor(left, top, left + width, top + height);
            PlayerCustomizationScreen.renderEntityInInventory(guiGraphics, left + width / 2F, top + height / 2F, 20, CustomizationsGrid.this.preview, this.suitStandPreview);
            guiGraphics.disableScissor();

            if (hovering) {
                guiGraphics.renderTooltip(
                        CustomizationsGrid.this.minecraft.font,
                        this.customization.value().getTitle(CustomizationsGrid.this.minecraft.level.registryAccess()),
                        mouseX,
                        mouseY);
            }
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (CommonInputs.selected(keyCode)) {
                this.select();
                return true;
            } else {
                return super.keyPressed(keyCode, scanCode, modifiers);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            this.select();
            return super.mouseClicked(mouseX, mouseY, button);
        }

        private void select() {
            CustomizationsGrid.this.setSelected(this);
            CustomizationsGrid.this.minecraft.getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F)
            );
            NetworkManager.sendToServer(new SelectCustomizationPacket(this.customization.unwrapKey().orElseThrow()));
        }
    }

}
