package net.threetag.palladium.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.threetag.palladium.addonpack.AddonPackManager;

public class PalladiumFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        AddonPackManager.startLoading();
        AddonPackManager.waitForLoading();
    }
}
