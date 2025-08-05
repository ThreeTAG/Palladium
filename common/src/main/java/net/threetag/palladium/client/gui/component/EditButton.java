package net.threetag.palladium.client.gui.component;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.threetag.palladium.util.RenderUtil;

public class EditButton extends Button {

    public EditButton(int x, int y, OnPress onPress) {
        super(x, y, 12, 12, Component.empty(), onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int u = this.active ? (this.isHoveredOrFocused() ? 24 : 12) : 0;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, RenderUtil.WIDGETS_LOCATION, this.getX(), this.getY(), u, 136, this.width, this.height, 256, 256, ARGB.white(this.alpha));
    }
}
