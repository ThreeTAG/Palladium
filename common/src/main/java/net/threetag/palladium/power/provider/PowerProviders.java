package net.threetag.palladium.power.provider;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;

public class PowerProviders {

    public static final DeferredRegister<PowerProvider> PROVIDERS = DeferredRegister.create(Palladium.MOD_ID, PowerProvider.RESOURCE_KEY);

    public static final RegistrySupplier<PowerProvider> SUPERPOWER = PROVIDERS.register("superpower", SuperpowerProvider::new);
    public static final RegistrySupplier<PowerProvider> EQUIPMENT_SLOTS = PROVIDERS.register("equipment_slots", EquipmentSlotPowerProvider::new);
    public static final RegistrySupplier<PowerProvider> SUIT_SETS = PROVIDERS.register("suit_sets", SuitSetPowerProvider::new);

}
