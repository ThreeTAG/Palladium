package com.threetag.threecore.karma.client;

import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;

public class KarmaToast implements IToast {

    private long firstDrawTime;
    private int prevKarma;
    public int karma;

    public KarmaToast(int karma) {
        this.karma = karma;
    }

    @Override
    public Visibility draw(GuiToast guiToast, long l) {
        if (this.firstDrawTime == 0 || this.karma != this.prevKarma)
            this.firstDrawTime = l;

        this.prevKarma = this.karma;
        guiToast.getMinecraft().getTextureManager().bindTexture(KarmaBarRenderer.TEXTURE);
        boolean good = this.karma >= 0;
        GlStateManager.color3f(1.0F, 1.0F, 1.0F);
        guiToast.drawTexturedModalRect(0, 0, 0, good ? 46 : 15, 160, 32);

        guiToast.getMinecraft().fontRenderer.drawString(I18n.format("karma.toast.title"), 25.0F, 7.0F, good ? 0xff7b0000 : 0xffffffff);
        guiToast.getMinecraft().fontRenderer.drawString(I18n.format(this.karma > 0 ? "+" + this.karma : this.karma + ""), 30.0F, 18.0F, -16777216);

        return l - this.firstDrawTime < 5000L ? Visibility.SHOW : Visibility.HIDE;
    }

    public void addKarma(int karma) {
        this.karma += karma;
    }

    public static void addOrUpdate(GuiToast toastGui, int karma) {
        KarmaToast karmaToast = toastGui.getToast(KarmaToast.class, NO_TOKEN);
        if (karmaToast == null) {
            toastGui.add(new KarmaToast(karma));
        } else {
            karmaToast.addKarma(karma);
        }

    }

}
