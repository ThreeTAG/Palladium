package net.threetag.test.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.threecore.test.TestMod;

@Mod(TestMod.MOD_ID)
public class TestModForge {

    public TestModForge() {
        EventBuses.registerModEventBus(TestMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        TestMod.init();
    }

}
