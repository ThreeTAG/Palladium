package net.threetag.palladium.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;

public final class PalladiumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Palladium.init();

        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            PalladiumLifecycleEvents.SETUP.invoker().run();
            PalladiumLifecycleEvents.CLIENT_SETUP.invoker().run();
        });
    }

}
