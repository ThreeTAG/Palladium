package net.threetag.threecore.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.capability.CapabilityKarma;
import net.threetag.threecore.karma.KarmaClass;

@OnlyIn(Dist.CLIENT)
public class KarmaBarRenderer {

    public static final ResourceLocation TEXTURE = new ResourceLocation(ThreeCore.MODID, "textures/gui/karma.png");

    @SubscribeEvent
    public void renderHUD(GuiScreenEvent.DrawScreenEvent e) {
        if (e.getGui() instanceof InventoryScreen) {
            Minecraft mc = Minecraft.getInstance();
            mc.player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
                MainWindow mainWindow = mc.getMainWindow();
                float f = (float) (k.getKarma() + (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX) / 2) / (float) (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX);

                RenderSystem.pushMatrix();
                RenderSystem.color3f(1.0F, 1.0F, 1.0F);
                mc.textureManager.bindTexture(TEXTURE);
                mc.ingameGUI.blit(e.getMatrixStack(), mainWindow.getScaledWidth() / 2 - 91, (mainWindow.getScaledHeight() / 2) - (((InventoryScreen) e.getGui()).getYSize() / 2) - 10, 0, 0, 182, 5);
                mc.ingameGUI.blit(e.getMatrixStack(), mainWindow.getScaledWidth() / 2 - 85 + (int) (f * 170) - 3, (mainWindow.getScaledHeight() / 2) - (((InventoryScreen) e.getGui()).getYSize() / 2) - 12, 0, 5, 6, 9);
                for (KarmaClass classes : KarmaClass.VALUES) {
                    if (classes != KarmaClass.NEUTRAL) {
                        int value = classes.ordinal() < KarmaClass.NEUTRAL.ordinal() ? classes.getMaximum() : classes.getMinimum();
                        float f1 = (float) (value + (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX) / 2) / (float) (Math.abs(CapabilityKarma.MIN) + CapabilityKarma.MAX);
                        mc.ingameGUI.blit(e.getMatrixStack(), mainWindow.getScaledWidth() / 2 - 85 + (int) (f1 * 170) - 1, (mainWindow.getScaledHeight() / 2) - (((InventoryScreen) e.getGui()).getYSize() / 2) - 11, 6, 5, 2, 7);
                    }
                }
                RenderSystem.color4f(1, 1, 1, 1F);
                RenderSystem.popMatrix();
            });
        }
    }

}
