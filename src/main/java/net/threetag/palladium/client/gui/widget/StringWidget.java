package net.threetag.palladium.client.gui.widget;

import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.TextAlignment;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.util.RenderUtil;

public class StringWidget extends net.minecraft.client.gui.components.StringWidget {

    private int color = RenderUtil.DEFAULT_GRAY;
    private boolean shadow = false;
    private TextAlignment alignment = TextAlignment.LEFT;

    public StringWidget(Component message, Font font) {
        super(message, font);
    }

    public StringWidget(int width, int height, Component message, Font font) {
        super(width, height, message, font);
        this.setMaxWidth(width);
    }

    public StringWidget(int x, int y, int width, int height, Component message, Font font) {
        super(x, y, width, height, message, font);
        this.setMaxWidth(width);
    }

    public StringWidget setColor(int color) {
        this.color = color;
        return this;
    }

    public StringWidget setShadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public StringWidget setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderUtil.flashStringColorOverride(this.color);
        RenderUtil.flashStringShadowOverride(this.shadow);
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void visitLines(ActiveTextCollector textCollector) {
        Component component = this.getMessage();
        Font font = this.getFont();
        int i = this.maxWidth > 0 ? this.maxWidth : this.getWidth();
        int j = font.width(component);
        int k = this.getX();
        int l = this.getY() + (this.getHeight() - 9) / 2;
        boolean flag = j > i;
        if (flag) {
            switch (this.textOverflow.ordinal()) {
                case 0 -> textCollector.accept(this.alignment, k, l, clipText(component, font, i));
                case 1 -> this.renderScrollingStringOverContents(textCollector, component, 2);
            }
        } else {
            textCollector.accept(this.alignment, k, l, component.getVisualOrderText());
        }
    }
}
