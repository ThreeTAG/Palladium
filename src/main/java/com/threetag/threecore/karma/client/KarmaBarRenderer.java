package com.threetag.threecore.karma.client;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.karma.KarmaClass;
import com.threetag.threecore.karma.capability.CapabilityKarma;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class KarmaBarRenderer {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/karma.png");

    @SubscribeEvent
    public void renderHUD(RenderGameOverlayEvent.Post e) {
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            Minecraft mc = Minecraft.getInstance();
            if (!mc.ingameGUI.getChatGUI().getChatOpen())
                return;
            mc.player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
                MainWindow mainWindow = Minecraft.getInstance().mainWindow;
                float f = (float) (k.getKarma() + (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX) / 2) / (float) (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX);

                GlStateManager.pushMatrix();
                GlStateManager.color3f(1.0F, 1.0F, 1.0F);
                Minecraft.getInstance().textureManager.bindTexture(TEXTURE);
                mc.ingameGUI.drawTexturedModalRect(mainWindow.getScaledWidth() / 2 - 91, 10, 0, 0, 182, 5);
                mc.ingameGUI.drawTexturedModalRect(mainWindow.getScaledWidth() / 2 - 85 + (int) (f * 170) - 3, 8, 0, 5, 6, 9);
                for (KarmaClass classes : KarmaClass.VALUES) {
                    if (classes != KarmaClass.NEUTRAL) {
                        int value = classes.ordinal() < KarmaClass.NEUTRAL.ordinal() ? classes.getMaximum() : classes.getMinimum();
                        float f1 = (float) (value + (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX) / 2) / (float) (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX);
                        mc.ingameGUI.drawTexturedModalRect(mainWindow.getScaledWidth() / 2 - 85 + (int) (f1 * 170) - 1, 9, 6, 5, 2, 7);
                    }
                }
                GlStateManager.popMatrix();
            });
        }
    }

}
