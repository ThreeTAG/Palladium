package net.threetag.palladium.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.threetag.palladium.PalladiumClient;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.compat.geckolib.fabric.GeckoLibCompatImpl;
import net.threetag.palladium.compat.trinkets.fabric.TrinketsCompat;
import net.threetag.palladiumcore.util.Platform;

public class PalladiumFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AddonPackManager.startLoading();
        AddonPackManager.waitForLoading();

        PalladiumClient.init();

        if (Platform.isModLoaded("trinkets")) {
            TrinketsCompat.initClient();
        }
    }

}
