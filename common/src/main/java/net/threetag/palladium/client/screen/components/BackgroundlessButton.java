package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class BackgroundlessButton extends Button {

    public BackgroundlessButton(int i, int j, int k, int l, Component component, OnPress onPress) {
        super(i, j, k, l, component, onPress);
    }

    public BackgroundlessButton(int i, int j, int k, int l, Component component, OnPress onPress, OnTooltip onTooltip) {
        super(i, j, k, l, component, onPress, onTooltip);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft.getInstance().font.draw(poseStack, this.getMessage(), this.x + (this.width - Minecraft.getInstance().font.width(this.getMessage())) / 2F, this.y - 1, 4210752);
        if (this.isHovered) {
            this.renderToolTip(poseStack, x, y);
        }
    }
}
