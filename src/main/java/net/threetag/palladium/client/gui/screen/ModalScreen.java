package net.threetag.palladium.client.gui.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.FittingMultiLineTextWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.gui.widget.CloseButton;
import net.threetag.palladium.client.util.RenderUtil;

import java.util.ArrayList;
import java.util.List;

public class ModalScreen extends Screen {

    public static final Identifier BACKGROUND_MODAL_DEFAULT = Palladium.id("background/modal");
    public static final Identifier BACKGROUND_MODAL_HEADER = Palladium.id("background/modal_with_header");
    public static final Identifier BACKGROUND_MODAL_HEADER_AND_FOOTER = Palladium.id("background/modal_with_header_and_footer");
    public static final int DEFAULT_WIDTH = 170;
    public static final int DEFAULT_HEIGHT = 120;

    private int x;
    private int y;
    private int width;
    private int height;
    private Component headerText;
    private boolean renderBackground = true;
    private List<Button> buttons = new ArrayList<>();

    public ModalScreen(ScreenRectangle screenRectangle, Component message) {
        this(
                screenRectangle.left() + (screenRectangle.width() / 2) - (DEFAULT_WIDTH / 2),
                screenRectangle.top() + (screenRectangle.height() / 2) - (DEFAULT_HEIGHT / 2),
                DEFAULT_WIDTH, DEFAULT_HEIGHT,
                message
        );
    }

    public ModalScreen(int x, int y, int width, int height, Component message) {
        super(message);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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

        int paddingSides = 7;
        int paddingTop = this.headerText != null ? 20 : paddingSides;
        int paddingBottom = this.buttons.isEmpty() ? paddingSides : 28;

        this.addRenderableWidget(new CloseButton(this.getRight() - paddingSides - 7, this.getY() + paddingSides, button -> this.onClose()));

        this.addRenderableWidget(new FittingMultiLineTextWidget(
                this.getX() + paddingSides,
                this.getY() + paddingTop,
                this.getWidth() - paddingSides - paddingSides - 6,
                this.getHeight() - paddingTop - paddingBottom,
                this.getTitle(),
                this.minecraft.font
        ) {
            @Override
            protected void renderBackground(GuiGraphics guiGraphics) {

            }
        });

        if (!this.buttons.isEmpty()) {
            int gap = 4;
            int width = (this.getWidth() - paddingSides - paddingSides - (this.buttons.size() - 1) * gap) / this.buttons.size();

            for (int i = 0; i < this.buttons.size(); i++) {
                var button = this.buttons.get(i);
                button.setPosition(this.getX() + paddingSides + i * (width + gap), this.getBottom() - paddingSides + 2 - button.getHeight());
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

        if (this.headerText != null || !this.buttons.isEmpty()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.buttons.isEmpty() ? BACKGROUND_MODAL_HEADER : BACKGROUND_MODAL_HEADER_AND_FOOTER, this.getX(), this.getY(), this.getWidth(), this.getHeight());
            guiGraphics.drawString(Minecraft.getInstance().font, this.headerText, this.getX() + 7, this.getY() + 7, RenderUtil.DEFAULT_GRAY, false);
        } else {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, BACKGROUND_MODAL_DEFAULT, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRight() {
        return x + width;
    }

    public int getBottom() {
        return y + height;
    }
}
