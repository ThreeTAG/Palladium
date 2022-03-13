package net.threetag.palladium.fabric;

import dev.architectury.registry.ReloadListenerRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.server.packs.PackType;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;

public class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PalladiumClient.init();
        ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, new PackRenderLayerManager());
    }

}
