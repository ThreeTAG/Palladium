package net.threetag.palladium.power.provider;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class PowerProviders {

    public static final DeferredRegister<PowerProvider> PROVIDERS = DeferredRegister.create(PalladiumRegistryKeys.POWER_PROVIDER, Palladium.MOD_ID);

    public static final int PRIORITY_SUPERPOWERS = 100;
    public static final int PRIORITY_SUITS = 200;
    public static final int PRIORITY_ITEMS = 300;

    // TODO
    public static final DeferredHolder<PowerProvider, SuperpowerProvider> SUPERPOWER = PROVIDERS.register("superpower", SuperpowerProvider::new);
    public static final DeferredHolder<PowerProvider, ItemPowerProvider> ITEM = PROVIDERS.register("item", ItemPowerProvider::new);
//    public static final Holder<PowerProvider> SUIT_SETS = PROVIDERS.register("suit_sets", SuitSetPowerProvider::new);

}
