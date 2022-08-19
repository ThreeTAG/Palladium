package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.threetag.palladium.util.icon.IIcon;

public class IconButton extends Button {

    private final IIcon icon;
    private boolean renderBackground = true;

    public IconButton(int xIn, int yIn, IIcon icon, Button.OnPress onPressIn) {
        this(xIn, yIn, icon, onPressIn, NO_TOOLTIP);
    }

    public IconButton(int x, int y, IIcon icon, Button.OnPress onPress, Button.OnTooltip tooltip) {
        super(x, y, 20, 20, TextComponent.EMPTY, onPress, tooltip);
        this.icon = icon;
    }

    public IconButton disableBackgroundRendering() {
        this.renderBackground = false;
        return this;
    }

    public void setPosition(int xIn, int yIn) {
        this.x = xIn;
        this.y = yIn;
    }

    public IIcon getIcon() {
        return this.icon;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (this.renderBackground) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, FlatIconButton.WIDGETS_LOCATION);

            int i = 60;
            if (!this.active) {
                i += 40;
            } else if (this.isHovered) {
                i += 20;
            }

            RenderSystem.enableDepthTest();
            blit(matrixStack, this.x, this.y, (float) 0, (float) i, this.width, this.height, 256, 256);
        }

        this.getIcon().draw(minecraft, matrixStack, this.x + 2, this.y + 2);

        if (this.isHovered) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

}
