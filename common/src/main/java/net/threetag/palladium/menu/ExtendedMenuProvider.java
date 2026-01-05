package net.threetag.palladium.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;

public interface ExtendedMenuProvider extends MenuProvider {

    void addAdditionalData(FriendlyByteBuf buf);

}
