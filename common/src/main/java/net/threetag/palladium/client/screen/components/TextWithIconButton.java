package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.icon.IIcon;
import org.jetbrains.annotations.Nullable;

public class TextWithIconButton extends Button {

    private final Component suffix;
    private final IIcon icon;

    public TextWithIconButton(int i, int j, int k, int l, @Nullable Component prefix, @Nullable Component suffix, IIcon icon, OnPress onPress, CreateNarration narration) {
        super(i, j, k, l, prefix == null ? Component.empty() : prefix, onPress, narration);
        this.suffix = suffix == null ? Component.empty() : suffix;
        this.icon = icon;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.active ? 16777215 : 10526880;

        int prefixWidth = minecraft.font.width(this.getMessage());
        int fullTextWidth = prefixWidth + 16 + minecraft.font.width(this.suffix);
        int color = i | Mth.ceil(this.alpha * 255.0F) << 24;

        guiGraphics.drawString(minecraft.font, this.getMessage(), (int) (this.getX() + this.width / 2F - fullTextWidth / 2F), (int) (this.getY() + (this.getHeight() - 8) / 2F), color);
        guiGraphics.drawString(minecraft.font, this.suffix, (int) (this.getX() + this.width / 2F - fullTextWidth / 2F + prefixWidth + 16), (int) (this.getY() + (this.height - 8F) / 2F), color);
        this.icon.draw(minecraft, guiGraphics, DataContext.forEntity(minecraft.player), this.getX() + this.width / 2 - fullTextWidth / 2 + prefixWidth, this.getY() + (this.height - 8) / 2 - 4);
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 + i * 20;
    }

    public static Builder textWithIconBuilder(Component prefix, IIcon icon, Button.OnPress onPress) {
        return new Builder(prefix, icon, onPress);
    }

    public static Builder textWithIconBuilder(IIcon icon, Component suffix, Button.OnPress onPress) {
        return new Builder(icon, suffix, onPress);
    }

    public static Builder textWithIconBuilder(Component prefix, IIcon icon, Component suffix, Button.OnPress onPress) {
        return new Builder(prefix, icon, suffix, onPress);
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {
        private final Component prefix;
        private final Component suffix;
        private final IIcon icon;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private CreateNarration createNarration;

        public Builder(Component prefix, IIcon icon, OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.prefix = prefix;
            this.suffix = null;
            this.icon = icon;
            this.onPress = onPress;
        }

        public Builder(IIcon icon, Component suffix, OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.prefix = null;
            this.suffix = suffix;
            this.icon = icon;
            this.onPress = onPress;
        }

        public Builder(Component prefix, IIcon icon, Component suffix, OnPress onPress) {
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
