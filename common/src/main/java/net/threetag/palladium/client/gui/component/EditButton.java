package net.threetag.palladium.client.gui.component;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.util.RenderUtil;

public class EditButton extends Button {

    public EditButton(int x, int y, OnPress onPress) {
        super(x, y, 12, 12, Component.empty(), onPress, DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableDepthTest();
        int u = this.active ? (this.isHoveredOrFocused() ? 24 : 12) : 0;
        guiGraphics.blit(RenderType::guiTextured, RenderUtil.WIDGETS_LOCATION, this.getX(), this.getY(), (float) u, (float) 136, this.width, this.height, 256, 256);
    }
}
