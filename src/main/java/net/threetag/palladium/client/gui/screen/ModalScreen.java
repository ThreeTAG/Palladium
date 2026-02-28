package net.threetag.palladium.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.widget.BackgroundlessTextBoxWidget;
import net.threetag.palladium.client.gui.widget.CloseButton;
import net.threetag.palladium.client.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class ModalScreen extends Screen {

    public static final Identifier BACKGROUND_MODAL_DEFAULT = Palladium.id("background/modal");
    public static final Identifier BACKGROUND_MODAL_HEADER = Palladium.id("background/modal_with_header");
    public static final Identifier BACKGROUND_MODAL_FOOTER = Palladium.id("background/modal_with_footer");
    public static final Identifier BACKGROUND_MODAL_HEADER_AND_FOOTER = Palladium.id("background/modal_with_header_and_footer");
    public static final int DEFAULT_WIDTH = 170;
    public static final int DEFAULT_HEIGHT = 120;

    private int leftPos;
    private int topPos;
    private final int imageWidth;
    private final int imageHeight;
    private Component headerText;
    private boolean renderBackground = true;
    private final List<Button> buttons = new ArrayList<>();
    private CloseButton closeButton;

    public ModalScreen(Component message) {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, message);
    }

    public ModalScreen(int imageWidth, int imageHeight, Component message) {
        super(message);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public ModalScreen setHeader(Component text) {
        this.headerText = text;
        return this;
    }

    public ModalScreen addButton(Button button) {
        this.buttons.add(button);
        return this;
    }

    public ModalScreen disableBackgroundRendering() {
        this.renderBackground = false;
        return this;
    }

    @Override
    protected void init() {
        super.init();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        int paddingSides = 7;
        int paddingTop = this.headerText != null ? 20 : paddingSides;
        int paddingBottom = this.buttons.isEmpty() ? paddingSides : 28;
        int closeOffset = this.headerText != null ? 0 : 2;

        this.addRenderableWidget(this.closeButton = new CloseButton(this.getRight() - paddingSides - 7 - closeOffset, this.getTopPos() + paddingSides + closeOffset, button -> this.onClose()));

        if (!this.getTitle().getString().isBlank()) {
            this.addRenderableWidget(new BackgroundlessTextBoxWidget(
                    this.getLeftPos() + paddingSides,
                    this.getTopPos() + paddingTop,
                    this.getImageWidth() - paddingSides - paddingSides,
                    this.getImageHeight() - paddingTop - paddingBottom,
                    this.getTitle(),
                    this.minecraft.font
            ));
        }

        if (!this.buttons.isEmpty()) {
            int gap = 4;
            int width = (this.getImageWidth() - paddingSides - paddingSides - (this.buttons.size() - 1) * gap) / this.buttons.size();

            for (int i = 0; i < this.buttons.size(); i++) {
                var button = this.buttons.get(i);
                button.setPosition(this.getLeftPos() + paddingSides + i * (width + gap), this.getBottom() - paddingSides + 2 - button.getHeight());
                button.setWidth(width);
                this.addRenderableWidget(button);
            }
        }
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.renderBackground) {
            super.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        }

        if (this.headerText != null && !this.buttons.isEmpty()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_MODAL_HEADER_AND_FOOTER, this.getLeftPos(), this.getTopPos(), this.getImageWidth(), this.getImageHeight());
            guiGraphics.drawString(Minecraft.getInstance().font, this.headerText, this.getLeftPos() + 7, this.getTopPos() + 7, RenderUtil.DEFAULT_GRAY, false);
        } else if (this.headerText != null) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_MODAL_HEADER, this.getLeftPos(), this.getTopPos(), this.getImageWidth(), this.getImageHeight());
            guiGraphics.drawString(Minecraft.getInstance().font, this.headerText, this.getLeftPos() + 7, this.getTopPos() + 7, RenderUtil.DEFAULT_GRAY, false);
        } else if (!this.buttons.isEmpty()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_MODAL_FOOTER, this.getLeftPos(), this.getTopPos(), this.getImageWidth(), this.getImageHeight());
        } else {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_MODAL_DEFAULT, this.getLeftPos(), this.getTopPos(), this.getImageWidth(), this.getImageHeight());
        }
    }

    public int getLeftPos() {
        return leftPos;
    }

    public int getTopPos() {
        return topPos;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getRight() {
        return leftPos + imageWidth;
    }

    public int getBottom() {
        return topPos + imageHeight;
    }
}
