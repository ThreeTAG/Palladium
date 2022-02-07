package net.threetag.palladium.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.threetag.palladium.PalladiumClient;

public class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        PalladiumClient.init();
    }

}
