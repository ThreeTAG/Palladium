package net.threetag.palladium.menu;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Palladium.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<TailoringMenu>> TAILORING = MENU_TYPES.register("tailoring", () -> new MenuType<>(TailoringMenu::new, FeatureFlags.VANILLA_SET));

    @Environment(EnvType.CLIENT)
    public static void registerScreens() {
        MenuScreens.register(TAILORING.get(), TailoringScreen::new);
    }

}
