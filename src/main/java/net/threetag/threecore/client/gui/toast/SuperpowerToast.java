package net.threetag.threecore.client.gui.toast;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.ToastGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.threetag.threecore.client.renderer.AbilityBarRenderer;
import net.threetag.threecore.util.icon.IIcon;

public class SuperpowerToast implements IToast {

    public ITextComponent name;
    public IIcon icon;

    public SuperpowerToast(ITextComponent name, IIcon icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public Visibility func_230444_a_(MatrixStack stack, ToastGui guiToast, long l) {
        guiToast.getMinecraft().getTextureManager().bindTexture(AbilityBarRenderer.TEXTURE);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        guiToast.func_238474_b_(stack, 0, 0, 0, 224, 160, 32);

        guiToast.getMinecraft().fontRenderer.func_238421_b_(stack, I18n.format("superpower.toast.title"), 30.0F, 7.0F, 0xff7b0000);
        guiToast.getMinecraft().fontRenderer.func_238407_a_(stack, this.name, 30.0F, 18.0F, -16777216);

        this.icon.draw(guiToast.getMinecraft(), stack, 8, 8);

        return l >= 5000L ? Visibility.HIDE : Visibility.SHOW;
    }
}
