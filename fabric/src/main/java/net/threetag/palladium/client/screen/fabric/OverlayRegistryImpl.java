package net.threetag.palladium.client.screen.fabric;

import com.mojang.blaze3d.platform.Window;
import dev.architectury.event.events.client.ClientGuiEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.IIngameOverlay;

public class OverlayRegistryImpl {

    public static void registerOverlay(String displayName, IIngameOverlay overlay) {
        ClientGuiEvent.RENDER_HUD.register((matrices, tickDelta) -> {
            try {
                Gui gui = Minecraft.getInstance().gui;
                Window window = Minecraft.getInstance().getWindow();
                overlay.render(gui, matrices, tickDelta, window.getGuiScaledWidth(), window.getGuiScaledHeight());
            } catch (Exception e) {
                Palladium.LOGGER.error("Error rendering overlay '{}'", displayName, e);
            }
        });
    }
}
