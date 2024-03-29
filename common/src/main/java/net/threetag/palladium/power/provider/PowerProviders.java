package net.threetag.palladium.power.provider;

import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PowerProviders {

    public static final DeferredRegister<PowerProvider> PROVIDERS = DeferredRegister.create(Palladium.MOD_ID, PowerProvider.REGISTRY);

    public static final RegistrySupplier<PowerProvider> SUPERPOWER = PROVIDERS.register("superpower", SuperpowerProvider::new);
    public static final RegistrySupplier<PowerProvider> EQUIPMENT_SLOTS = PROVIDERS.register("equipment_slots", EquipmentSlotPowerProvider::new);
    public static final RegistrySupplier<PowerProvider> SUIT_SETS = PROVIDERS.register("suit_sets", SuitSetPowerProvider::new);

}
