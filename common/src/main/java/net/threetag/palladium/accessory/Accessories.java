package net.threetag.palladium.accessory;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;

public class Accessories {

    public static final DeferredRegister<Accessory> ACCESSORIES = DeferredRegister.create(Palladium.MOD_ID, Accessory.RESOURCE_KEY);

    public static final RegistrySupplier<Accessory> LUCRAFT_ARC_REACTOR = ACCESSORIES.register("lucraft_arc_reactor", LucraftArcReactorAccessory::new);
    public static final RegistrySupplier<Accessory> WINTER_SOLDIER_ARM = ACCESSORIES.register("winter_soldier_arm", WinterSoldierArmAccessory::new);
    public static final RegistrySupplier<Accessory> HYPERION_ARM = ACCESSORIES.register("hyperion_arm", HyperionArmAccessory::new);
}
