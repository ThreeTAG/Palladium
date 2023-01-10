package net.threetag.palladium.compat.kubejs;

import dev.latvian.mods.kubejs.event.EventJS;
import net.threetag.palladiumcore.registry.client.OverlayRegistry;

public class RegisterGuiOverlaysEventJS extends EventJS {

    public void register(String id, OverlayRegistry.IIngameOverlay overlay) {
        OverlayRegistry.registerOverlay(id, overlay);
    }

}
