package net.threetag.palladium.menu;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.screen.MultiversalIteratorScreen;
import net.threetag.palladium.client.screen.MultiversalIteratorSuitStandScreen;
import net.threetag.palladium.client.screen.TailoringScreen;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

public class PalladiumMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Palladium.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<TailoringMenu>> TAILORING = MENU_TYPES.register("tailoring", () -> new MenuType<>(TailoringMenu::new, FeatureFlags.VANILLA_SET));
    public static final RegistrySupplier<MenuType<MultiversalIteratorMenu>> MULTIVERSAL_ITERATOR = MENU_TYPES.register("multiversal_iterator", () -> new MenuType<>(MultiversalIteratorMenu::new, FeatureFlags.VANILLA_SET));
    public static final RegistrySupplier<MenuType<MultiversalIteratorSuitStandMenu>> MULTIVERSAL_ITERATOR_SUIT_STAND = MENU_TYPES.register("multiversal_iterator_suit_stand", () -> ofExtended(MultiversalIteratorSuitStandMenu::new));

    @Environment(EnvType.CLIENT)
    public static void registerScreens() {
        MenuScreens.register(TAILORING.get(), TailoringScreen::new);
        MenuScreens.register(MULTIVERSAL_ITERATOR.get(), MultiversalIteratorScreen::new);
        MenuScreens.register(MULTIVERSAL_ITERATOR_SUIT_STAND.get(), MultiversalIteratorSuitStandScreen::new);
    }

    @ExpectPlatform
    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(ExtendedMenuTypeFactory<T> factory) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface ExtendedMenuTypeFactory<T extends AbstractContainerMenu> {

        T create(int id, Inventory inventory, FriendlyByteBuf buf);
    }

}
