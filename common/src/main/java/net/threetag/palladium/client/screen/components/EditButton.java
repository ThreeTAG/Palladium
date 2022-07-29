package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;

public class EditButton extends Button {

    public EditButton(int x, int y, OnPress onPress) {
        super(x, y, 12, 12, TextComponent.EMPTY, onPress);
    }

    public EditButton(int x, int y, OnPress onPress, OnTooltip onTooltip) {
        super(x, y, 12, 12, TextComponent.EMPTY, onPress, onTooltip);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, FlatIconButton.WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableDepthTest();
        int u = this.active ? (this.isHoveredOrFocused() ? 24 : 12) : 0;
        blit(poseStack, this.x, this.y, (float) u, (float) 136, this.width, this.height, 256, 256);

        if (this.isHoveredOrFocused()) {
            this.renderToolTip(poseStack, mouseX, mouseY);
        }
    }
}
