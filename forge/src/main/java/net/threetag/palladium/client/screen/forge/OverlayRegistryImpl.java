package net.threetag.palladium.client.screen.forge;

import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.threetag.palladium.client.screen.IIngameOverlay;

public class OverlayRegistryImpl {

    public static void registerOverlay(String displayName, IIngameOverlay overlay) {
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.SCOREBOARD_ELEMENT, displayName, overlay::render);
    }

}
