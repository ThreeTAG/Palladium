package net.threetag.palladium.fabric;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;
import net.threetag.palladium.core.registry.SimpleRegister;

public class PalladiumFabricServer implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        ServerLifecycleEvents.SERVER_STARTING.register(server -> PalladiumLifecycleEvents.SETUP.invoker().run());

        AddonPackManager.initiateBasicLoaders();
        AddonPackManager.initiateAllLoaders(SimpleRegister::register);
    }
}
