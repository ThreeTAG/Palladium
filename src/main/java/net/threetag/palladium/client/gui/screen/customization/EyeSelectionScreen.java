package net.threetag.palladium.client.gui.screen.customization;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.threetag.palladium.client.gui.screen.ModalScreen;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.customization.EyeSelectionUtil;
import net.threetag.palladium.network.SetPlayerEyeSelection;

public class EyeSelectionScreen extends ModalScreen {

    public static final String TRANS_TITLE = "gui.palladium.player_customizations.eye_selection.title";
    public static final String TRANS_RESET = "gui.palladium.player_customizations.eye_selection.reset";
    public static final String TRANS_SAVE = "gui.palladium.player_customizations.eye_selection.save";
    private final AbstractClientPlayer player;
    private long eyeSelection;
    private final Button saveButton;

    public EyeSelectionScreen(AbstractClientPlayer player, long eyeSelection) {
        super(150, 150, Component.empty());
        this.player = player;
        this.eyeSelection = eyeSelection;
        this.setHeader(Component.translatable(TRANS_TITLE));

        this.addButton(Button.builder(Component.translatable(TRANS_RESET), button -> this.setEyeSelection(EyeSelectionUtil.DEFAULT_EYES)).build());

        this.addButton(this.saveButton = Button.builder(Component.translatable(TRANS_SAVE), button -> {
            ClientPacketDistributor.sendToServer(new SetPlayerEyeSelection(this.eyeSelection));
            this.onClose();
        }).build());
    }

    @Override
    protected void init() {
        super.init();

        int startX = (int) (this.getLeftPos() + this.getImageWidth() / 2F - 32);
        int startY = (int) (this.getTopPos() + this.getImageHeight() / 2F - 32) - 5;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                this.addRenderableWidget(new EyePixel(startX + x * 8, startY + y * 8, this.eyeSelection, x, y));
            }
        }
    }

    private void setEyeSelection(long eyeSelection) {
        this.eyeSelection = eyeSelection;

        for (GuiEventListener child : this.children()) {
            if (child instanceof EyePixel pixel) {
                pixel.update(eyeSelection);
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.saveButton.active = this.eyeSelection != 0L;
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(this.getLeftPos() + this.getImageWidth() / 2F - 32, this.getTopPos() + this.getImageHeight() / 2F - 32 - 5);
        guiGraphics.pose().scale(8, 8);
        guiGraphics.fill(0, 0, 8, 8, RenderUtil.FULL_BLACK);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.player.getSkin().body().texturePath(), 0, 0, 8, 8, 8, 8, 64, 64, ARGB.white(0.75F));
        guiGraphics.pose().popMatrix();
    }

    private class EyePixel extends AbstractWidget {

        private final int skinX, skinY;
        private boolean enabled;

        public EyePixel(int x, int y, long eyeSelection, int skinX, int skinY) {
            super(x, y, 8, 8, Component.empty());
            this.enabled = EyeSelectionUtil.isPixelSelected(eyeSelection, skinX, skinY);
            this.skinX = skinX;
            this.skinY = skinY;
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent p_447133_, boolean p_434606_) {
            return super.mouseClicked(p_447133_, p_434606_);
        }

        @Override
        public void onClick(MouseButtonEvent event, boolean isDoubleClick) {
            this.enabled = !this.enabled;
            EyeSelectionScreen.this.eyeSelection = EyeSelectionUtil.setPixelSelected(EyeSelectionScreen.this.eyeSelection, this.skinX, this.skinY, this.enabled);
        }

        @Override
        protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (this.enabled) {
                guiGraphics.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), RenderUtil.FULL_WHITE);
            } else if (this.isHovered()) {
                guiGraphics.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), ARGB.white(0.3F));
            }

            guiGraphics.hLine(this.getX(), this.getRight() - 1, this.getBottom() - 1, ARGB.black(0.1F));
            guiGraphics.vLine(this.getRight() - 1, this.getY() - 1, this.getBottom(), ARGB.black(0.1F));
        }

        public void update(long eyeSelection) {
            this.enabled = EyeSelectionUtil.isPixelSelected(eyeSelection, this.skinX, this.skinY);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

        }
    }
}
