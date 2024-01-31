package net.threetag.palladium.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.CreativeModeTabRegistry;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Palladium.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> TECHNOLOGY = TABS.register("technology",
            () -> CreativeModeTabRegistry.create(Component.translatable("itemGroup.palladium.technology"),
                    () ->  new ItemStack(PalladiumItems.LEAD_CIRCUIT.get())));

    public static final RegistrySupplier<CreativeModeTab> PALLADIUM_MODS = TABS.register("palladium_mods",
            () -> CreativeModeTabRegistry.create(Component.translatable("itemGroup.palladium.mods"),
                    () ->  new ItemStack(PalladiumItems.SUIT_STAND.get())));

}
