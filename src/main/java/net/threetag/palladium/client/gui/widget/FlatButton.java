package net.threetag.palladium.client.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.Palladium;
import org.jspecify.annotations.Nullable;

import java.util.function.Function;

public class FlatButton extends ImageButton {

    public static final WidgetSprites SPRITES = new WidgetSprites(
            Palladium.id("widget/flat_button"), Palladium.id("widget/flat_button_disabled"), Palladium.id("widget/flat_button_highlighted")
    );

    public FlatButton(int x, int y, int width, int height, OnPress onPress) {
        super(x, y, width, height, SPRITES, onPress);
    }

    public FlatButton(int x, int y, int width, int height, OnPress onPress, Component message) {
        super(x, y, width, height, SPRITES, onPress, message);
    }

    public FlatButton(int width, int height, OnPress onPress, Component message) {
        super(width, height, SPRITES, onPress, message);
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderContents(guiGraphics, mouseX, mouseY, partialTick);
        this.renderDefaultLabel(guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
    }

    public static Builder flatBuilder(Component message, Button.OnPress onPress) {
        return new Builder(message, onPress);
    }

    public static class Builder {
        private final Component message;
        private final Button.OnPress onPress;
        private @Nullable Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;

        public Builder(Component message, Button.OnPress onPress) {
            this.message = message;
            this.onPress = onPress;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public FlatButton build() {
            FlatButton button = new FlatButton(this.x, this.y, this.width, this.height, this.onPress, this.message);
            button.setTooltip(this.tooltip);
            return button;
        }

        public FlatButton build(Function<Builder, FlatButton> builder) {
            return builder.apply(this);
        }
    }
}
