package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.threetag.threecore.util.icon.IIcon;

public class IconButton extends Button {

    private final IIcon icon;

    public IconButton(int xIn, int yIn, IIcon icon, Button.IPressable onPressIn) {
        this(xIn, yIn, icon, onPressIn, field_238486_s_);
    }

    public IconButton(int x, int y, IIcon icon, Button.IPressable onPress, Button.ITooltip tooltip) {
        super(x, y, 20, 20, StringTextComponent.EMPTY, onPress, tooltip);
        this.icon = icon;
    }

    public void setPosition(int xIn, int yIn) {
        this.x = xIn;
        this.y = yIn;
    }

    public IIcon getIcon() {
        return this.icon;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(FlatIconButton.WIDGETS_LOCATION);

        int i = 60;
        if (!this.active) {
            i += 40;
        } else if (this.isHovered()) {
            i += 20;
        }

        RenderSystem.enableDepthTest();
        blit(matrixStack, this.x, this.y, (float) 0, (float) i, this.width, this.height, 256, 256);
        this.getIcon().draw(minecraft, matrixStack, this.x + 2, this.y + 2);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }

    }
}
