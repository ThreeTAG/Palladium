package net.threetag.palladium.compat.trinkets.fabric;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.emi.trinkets.TrinketsMain;
import net.threetag.palladium.power.provider.PowerProvider;

public class TrinketsCompat {

    public static final DeferredRegister<PowerProvider> FACTORIES = DeferredRegister.create(TrinketsMain.MOD_ID, PowerProvider.RESOURCE_KEY);
    public static final RegistrySupplier<PowerProvider> TRINKETS = FACTORIES.register("trinkets", TrinketsPowerProvider::new);

    public static void init() {
        FACTORIES.register();
    }

}
