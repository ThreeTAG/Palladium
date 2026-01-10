package net.threetag.palladium.menu;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;

public class PalladiumMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, Palladium.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<TailoringMenu>> TAILORING = MENU_TYPES.register("tailoring", () -> new MenuType<>(TailoringMenu::new, FeatureFlags.VANILLA_SET));

}
