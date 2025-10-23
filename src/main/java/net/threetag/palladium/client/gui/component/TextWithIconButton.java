package net.threetag.palladium.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;
import org.jetbrains.annotations.Nullable;

public class TextWithIconButton extends Button {

    private static final WidgetSprites SPRITES = new WidgetSprites(ResourceLocation.withDefaultNamespace("widget/button"), ResourceLocation.withDefaultNamespace("widget/button_disabled"), ResourceLocation.withDefaultNamespace("widget/button_highlighted"));
    private final Component suffix;
    private final Icon icon;

    public TextWithIconButton(int i, int j, int k, int l, @Nullable Component prefix, @Nullable Component suffix, Icon icon, OnPress onPress, CreateNarration narration) {
        super(i, j, k, l, prefix == null ? Component.empty() : prefix, onPress, narration);
        this.suffix = suffix == null ? Component.empty() : suffix;
        this.icon = icon;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blitSprite(
                RenderPipelines.GUI_TEXTURED,
                SPRITES.get(this.active, this.isHoveredOrFocused()),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight(),
                ARGB.white(this.alpha)
        );
        int i = ARGB.color(this.alpha, this.active ? -1 : -6250336);
        int prefixWidth = minecraft.font.width(this.getMessage());
        int fullTextWidth = prefixWidth + 16 + minecraft.font.width(this.suffix);

        guiGraphics.drawString(minecraft.font, this.getMessage(), (int) (this.getX() + this.width / 2F - fullTextWidth / 2F), (int) (this.getY() + (this.getHeight() - 8) / 2F), i);
        guiGraphics.drawString(minecraft.font, this.suffix, (int) (this.getX() + this.width / 2F - fullTextWidth / 2F + prefixWidth + 16), (int) (this.getY() + (this.height - 8F) / 2F), i);
        IconRenderer.drawIcon(this.icon, minecraft, guiGraphics, DataContext.forEntity(minecraft.player), this.getX() + this.width / 2 - fullTextWidth / 2 + prefixWidth, this.getY() + (this.height - 8) / 2 - 4);
    }

    public static Builder textWithIconBuilder(Component prefix, Icon icon, OnPress onPress) {
        return new Builder(prefix, icon, onPress);
    }

    public static Builder textWithIconBuilder(Icon icon, Component suffix, OnPress onPress) {
        return new Builder(icon, suffix, onPress);
    }

    public static Builder textWithIconBuilder(Component prefix, Icon icon, Component suffix, OnPress onPress) {
        return new Builder(prefix, icon, suffix, onPress);
    }

    public static class Builder {
        private final Component prefix;
        private final Component suffix;
        private final Icon icon;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private CreateNarration createNarration;

        public Builder(Component prefix, Icon icon, OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.prefix = prefix;
            this.suffix = null;
            this.icon = icon;
            this.onPress = onPress;
        }

        public Builder(Icon icon, Component suffix, OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.prefix = null;
            this.suffix = suffix;
            this.icon = icon;
            this.onPress = onPress;
        }

        public Builder(Component prefix, Icon icon, Component suffix, OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.prefix = prefix;
            this.suffix = suffix;
            this.icon = icon;
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

        public Builder createNarration(CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public Button build() {
            TextWithIconButton button = new TextWithIconButton(this.x, this.y, this.width, this.height, this.prefix, this.suffix, this.icon, this.onPress, this.createNarration);
            button.setTooltip(this.tooltip);
            return button;
        }
    }
}
