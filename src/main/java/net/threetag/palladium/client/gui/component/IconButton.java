package net.threetag.palladium.client.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.renderer.icon.IconRenderer;
import net.threetag.palladium.client.util.RenderUtil;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;
import org.jetbrains.annotations.Nullable;

public class IconButton extends Button {

    private final Icon icon;
    private boolean renderBackground = true;

    public IconButton(int x, int y, Icon icon, OnPress onPress, CreateNarration createNarration) {
        super(x, y, 20, 20, Component.empty(), onPress, createNarration);
        this.icon = icon;
    }

    public static Builder builder(Icon icon, OnPress onPress) {
        return new Builder(icon, onPress);
    }

    public IconButton disableBackgroundRendering() {
        this.renderBackground = false;
        return this;
    }

    public Icon getIcon() {
        return this.icon;
    }

    @Override
    public void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (this.renderBackground) {
            int i = 60;
            if (!this.active) {
                i += 40;
            } else if (this.isHovered) {
                i += 20;
            }

            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RenderUtil.WIDGETS_LOCATION, this.getX(), this.getY(), 0, i, this.width, this.height, 256, 256);
        }

        IconRenderer.drawIcon(this.getIcon(), minecraft, guiGraphics, minecraft.player != null ? DataContext.forEntity(minecraft.player) : DataContext.create(), this.getX() + 2, this.getY() + 2);
    }

    public static class Builder {

        private final Icon icon;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private CreateNarration createNarration;
        private boolean disableBackground = false;

        public Builder(Icon icon, OnPress onPress) {
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
