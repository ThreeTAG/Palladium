package net.threetag.palladium.compat.trinkets.fabric;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.emi.trinkets.TrinketsMain;
import net.threetag.palladium.power.holderfactory.PowerProviderFactory;

public class TrinketsCompat {

    public static final DeferredRegister<PowerProviderFactory> FACTORIES = DeferredRegister.create(TrinketsMain.MOD_ID, PowerProviderFactory.RESOURCE_KEY);
    public static final RegistrySupplier<PowerProviderFactory> TRINKETS = FACTORIES.register("trinkets", TrinketsPowerProviderFactory::new);

    public static void init() {
        FACTORIES.register();
    }

}
