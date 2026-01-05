package net.threetag.palladium.menu.forge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.NetworkHooks;
import net.threetag.palladium.menu.ExtendedMenuProvider;
import net.threetag.palladium.menu.PalladiumMenuTypes;

public class PalladiumMenuTypesImpl {

    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        NetworkHooks.openScreen(player, provider, provider::addAdditionalData);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(PalladiumMenuTypes.ExtendedMenuTypeFactory<T> factory) {
        return IForgeMenuType.create(factory::create);
    }

}
