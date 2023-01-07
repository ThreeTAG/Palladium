package net.threetag.palladium.client.screen.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.threetag.palladium.util.icon.IIcon;
import org.jetbrains.annotations.Nullable;

public class TextWithIconButton extends Button {

    private final Component suffix;
    private final IIcon icon;

    public TextWithIconButton(int i, int j, int k, int l, @Nullable Component prefix, @Nullable Component suffix, IIcon icon, OnPress onPress) {
        super(i, j, k, l, prefix == null ? Component.empty() : prefix, onPress);
        this.suffix = suffix == null ? Component.empty() : suffix;
        this.icon = icon;
    }

    public TextWithIconButton(int i, int j, int k, int l, @Nullable Component prefix, @Nullable Component suffix, IIcon icon, OnPress onPress, OnTooltip onTooltip) {
        super(i, j, k, l, prefix == null ? Component.empty() : prefix, onPress, onTooltip);
        this.suffix = suffix == null ? Component.empty() : suffix;
        this.icon = icon;
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHoveredOrFocused());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(poseStack, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.blit(poseStack, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBg(poseStack, minecraft, mouseX, mouseY);
        int j = this.active ? 16777215 : 10526880;

        int prefixWidth = font.width(this.getMessage());
        int fullTextWidth = prefixWidth + 16 + font.width(this.suffix);
        int x = 0;
        int color = j | Mth.ceil(this.alpha * 255.0F) << 24;

        font.drawShadow(poseStack, this.getMessage(), this.x + this.width / 2F - fullTextWidth / 2F, this.y + (this.height - 8F) / 2F, color);
        font.drawShadow(poseStack, this.suffix, this.x + this.width / 2F - fullTextWidth / 2F + prefixWidth + 16, this.y + (this.height - 8F) / 2F, color);
        this.icon.draw(minecraft, poseStack, this.x + this.width / 2 - fullTextWidth / 2 + prefixWidth, this.y + (this.height - 8) / 2 - 4);

        if (this.isHoveredOrFocused()) {
            this.renderToolTip(poseStack, mouseX, mouseY);
        }
    }
}
