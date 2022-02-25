package net.threetag.palladium.power.holderfactory;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;

public class PowerProviderFactories {

    public static final DeferredRegister<PowerProviderFactory> FACTORIES = DeferredRegister.create(Palladium.MOD_ID, PowerProviderFactory.RESOURCE_KEY);

    public static final RegistrySupplier<PowerProviderFactory> SUPERPOWER = FACTORIES.register("superpower", SuperpowerPowerProviderFactory::new);
    public static final RegistrySupplier<PowerProviderFactory> EQUIPMENT_SLOTS = FACTORIES.register("equipment_slots", EquipmentSlotPowerFactory::new);


}
