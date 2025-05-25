package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.screen.power.PowersScreen;

public class RenderPowerScreenEventJS extends EventJS {

    public final PowersScreen screen;
    public final GuiGraphics guiGraphics;
    public final int mouseX;
    public final int mouseY;
    public final float partialTick;
    public final ResourceLocation tab;

    public RenderPowerScreenEventJS(PowersScreen screen, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick, ResourceLocation tab) {
        this.screen = screen;
        this.guiGraphics = guiGraphics;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.partialTick = partialTick;
        this.tab = tab;
    }

    public static void register() {
        PowersScreen.POST_RENDER_CALLBACK = (screen, gui, mX, mY, pTick, tab) -> {
            gui.pose().pushPose();
            PalladiumJSEvents.RENDER_POWER_SCREEN.post(new RenderPowerScreenEventJS(screen, gui, mX, mY, pTick, tab));
            gui.pose().popPose();
        };
    }
}
