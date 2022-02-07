package net.threetag.test.fabric;

import net.fabricmc.api.ModInitializer;
import net.threecore.test.TestMod;

public class TestModFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        TestMod.init();
    }
}
