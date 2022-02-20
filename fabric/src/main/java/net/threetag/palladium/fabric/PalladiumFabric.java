package net.threetag.palladium.fabric;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.threetag.palladium.Palladium;

public class PalladiumFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        Palladium.init();
        AutoConfig.register(PalladiumConfigImpl.class, Toml4jConfigSerializer::new);
    }

}
