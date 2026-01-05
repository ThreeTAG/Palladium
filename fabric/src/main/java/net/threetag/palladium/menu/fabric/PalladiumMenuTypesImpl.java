package net.threetag.palladium.menu.fabric;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.threetag.palladium.menu.ExtendedMenuProvider;
import net.threetag.palladium.menu.PalladiumMenuTypes;
import org.jetbrains.annotations.Nullable;

public class PalladiumMenuTypesImpl {

    public static void openExtendedMenu(ServerPlayer player, ExtendedMenuProvider provider) {
        player.openMenu(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayer player, FriendlyByteBuf buf) {
                provider.addAdditionalData(buf);
            }

            @Override
            public Component getDisplayName() {
                return provider.getDisplayName();
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return provider.createMenu(i, inventory, player);
            }
        });
    }

    public static <T extends AbstractContainerMenu> MenuType<T> ofExtended(PalladiumMenuTypes.ExtendedMenuTypeFactory<T> factory) {
        return new ExtendedScreenHandlerType<>(factory::create);
    }
}
