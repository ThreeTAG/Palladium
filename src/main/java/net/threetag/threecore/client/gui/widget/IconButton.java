package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.icon.IIcon;

public class IconButton extends Button {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(ThreeCore.MODID, "textures/gui/widgets.png");

    private final IIcon icon;

    public IconButton(int xPos, int yPos, IIcon icon, IPressable handler) {
        super(xPos, yPos, 20, 20, new StringTextComponent(""), handler);
        this.icon = icon;
    }

    @Override
    public void func_230431_b_(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.func_230989_a_(this.func_230449_g_() /* isHovered() */) * 20;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.func_238474_b_(stack, this.field_230690_l_, this.field_230691_m_, 0, i, 20, this.field_230689_k_);
        this.icon.draw(minecraft, stack, this.field_230690_l_ + 2, this.field_230691_m_ + 2);
        this.func_230441_a_(stack, minecraft, mouseX, mouseY);
        RenderSystem.disableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
    }
}
