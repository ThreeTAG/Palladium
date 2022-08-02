package net.threetag.palladium.client.screen.forge;

import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.IIngameOverlay;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Palladium.MOD_ID)
public class OverlayRegistryImpl {

    private static final Map<String, IIngameOverlay> OVERLAYS = new HashMap<>();

    public static void registerOverlay(String displayName, IIngameOverlay overlay) {
        OVERLAYS.put(displayName, overlay);
    }

    @SubscribeEvent
    public static void onRegisterGuiOverlays(RegisterGuiOverlaysEvent e) {
        OVERLAYS.forEach((id, overlay) -> e.registerAboveAll(id, overlay::render));
    }

}
