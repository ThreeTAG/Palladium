package net.threetag.palladium.client.gui.component;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.threetag.palladium.client.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.RenderUtil;
import org.jetbrains.annotations.Nullable;

public class FlatIconButton extends Button {

    private final Icon icon;

    protected FlatIconButton(int x, int y, Icon icon, OnPress onPress, CreateNarration createNarration) {
        super(x, y, 20, 20, Component.empty(), onPress, createNarration);
        this.icon = icon;
    }

    public static Builder flatIcon(Icon icon, OnPress onPress) {
        return new Builder(icon, onPress);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RenderUtil.WIDGETS_LOCATION, this.getX(), this.getY(), 0, this.getTextureY(), 20, this.height, 256, 256, ARGB.white(this.alpha));
        this.icon.draw(minecraft, guiGraphics, minecraft.player != null ? DataContext.forEntity(minecraft.player) : DataContext.create(), this.getX() + 2, this.getY() + 2);
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return i * 20;
    }

    @Environment(EnvType.CLIENT)
    public static class Builder {

        private final Icon icon;
        private final OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private CreateNarration createNarration;

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

        public FlatIconButton build() {
            FlatIconButton button = new FlatIconButton(this.x, this.y, this.icon, this.onPress, this.createNarration);
            button.setTooltip(this.tooltip);
            return button;
        }
    }

}
