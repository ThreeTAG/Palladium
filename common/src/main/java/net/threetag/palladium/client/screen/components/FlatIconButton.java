package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.icon.IIcon;

public class FlatIconButton extends Button {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(Palladium.MOD_ID, "textures/gui/widgets.png");

    private final IIcon icon;

    public FlatIconButton(int xPos, int yPos, IIcon icon, Button.OnPress handler) {
        super(xPos, yPos, 20, 20, Component.literal(""), handler);
        this.icon = icon;
    }

    @Override
    public void renderButton(PoseStack matrixStack, int x, int y, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered) * 20;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(matrixStack, this.x, this.y, 0, i, 20, this.height);
        this.renderBg(matrixStack, minecraft, x, y);
        this.icon.draw(minecraft, matrixStack, this.x + 2, this.y + 2);

        if (this.isHovered) {
            this.renderToolTip(matrixStack, x, y);
        }
    }

}
