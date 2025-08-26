package net.threetag.palladium.power.provider;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class PowerProviders {

    public static final DeferredRegister<PowerProvider> PROVIDERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.POWER_PROVIDER);

    public static final int PRIORITY_SUPERPOWERS = 100;
    public static final int PRIORITY_SUITS = 200;
    public static final int PRIORITY_ITEMS = 300;

    // TODO
    public static final RegistryHolder<PowerProvider> SUPERPOWER = PROVIDERS.register("superpower", SuperpowerProvider::new);
    public static final RegistryHolder<PowerProvider> ITEM = PROVIDERS.register("item", ItemPowerProvider::new);
//    public static final Holder<PowerProvider> EQUIPMENT_SLOTS = PROVIDERS.register("equipment_slots", EquipmentSlotPowerProvider::new);
//    public static final Holder<PowerProvider> SUIT_SETS = PROVIDERS.register("suit_sets", SuitSetPowerProvider::new);

}
