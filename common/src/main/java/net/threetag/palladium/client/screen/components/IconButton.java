package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.util.icon.IIcon;
import org.jetbrains.annotations.Nullable;

public class IconButton extends Button {

    private final IIcon icon;
    private boolean renderBackground = true;

    protected IconButton(int x, int y, IIcon icon, Button.OnPress onPress, CreateNarration createNarration) {
        super(x, y, 20, 20, Component.empty(), onPress, createNarration);
        this.icon = icon;
    }

    public static Builder builder(IIcon icon, OnPress onPress) {
        return new Builder(icon, onPress);
    }

    public IconButton disableBackgroundRendering() {
        this.renderBackground = false;
        return this;
    }

    public IIcon getIcon() {
        return this.icon;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (this.renderBackground) {
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();

            int i = 60;
            if (!this.active) {
                i += 40;
            } else if (this.isHovered) {
                i += 20;
            }

            guiGraphics.blit(FlatIconButton.WIDGETS_LOCATION, this.getX(), this.getY(), (float) 0, (float) i, this.width, this.height, 256, 256);
            guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        this.getIcon().draw(minecraft, guiGraphics, this.getX() + 2, this.getY() + 2);
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {

        private final IIcon icon;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private CreateNarration createNarration;
        private boolean disableBackground = false;

        public Builder(IIcon icon, OnPress onPress) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.icon = icon;
            this.onPress = onPress;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder createNarration(CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public Builder disableBackgroundRendering() {
            this.disableBackground = true;
            return this;
        }

        public Button build() {
            IconButton button = new IconButton(this.x, this.y, this.icon, this.onPress, this.createNarration);
            button.setTooltip(this.tooltip);
            return this.disableBackground ? button.disableBackgroundRendering() : button;
        }
    }

}
