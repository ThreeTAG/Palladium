package net.threetag.palladium.power.provider;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.DeferredRegistry;
import net.threetag.palladium.registry.RegistrySupplier;

public class PowerProviders {

    public static final DeferredRegistry<PowerProvider> PROVIDERS = DeferredRegistry.create(Palladium.MOD_ID, PowerProvider.RESOURCE_KEY);

    public static final RegistrySupplier<PowerProvider> SUPERPOWER = PROVIDERS.register("superpower", SuperpowerProvider::new);
    public static final RegistrySupplier<PowerProvider> EQUIPMENT_SLOTS = PROVIDERS.register("equipment_slots", EquipmentSlotPowerProvider::new);
    public static final RegistrySupplier<PowerProvider> SUIT_SETS = PROVIDERS.register("suit_sets", SuitSetPowerProvider::new);

}
