package net.threetag.palladium.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.threetag.palladium.client.util.GuiUtil;
import net.threetag.palladium.client.util.RenderUtil;

public class CloseButton extends Button {

    public CloseButton(int x, int y, OnPress onPress) {
        super(x, y, 7, 7, Component.literal("x"), onPress, Button.DEFAULT_NARRATION);
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.isHoveredOrFocused()) {
            GuiUtil.drawStringWithOutline(guiGraphics, this.getMessage(), this.getX(), this.getY(), RenderUtil.DEFAULT_GRAY, RenderUtil.FULL_WHITE);
        } else {
            guiGraphics.drawString(Minecraft.getInstance().font, this.getMessage(), this.getX() + 1, this.getY() - 1, RenderUtil.DEFAULT_GRAY, false);
        }
    }
}
