package net.threetag.palladium.fabric.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.threetag.palladium.core.event.PalladiumLifecycleEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Inject(method = "reloadResources", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V"))
    private void reloadResources(CallbackInfo ci) {
        PalladiumLifecycleEvents.DATA_PACK_SYNC.invoker().onDataPackSync((PlayerList) (Object) this, null);
    }

    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;)V", ordinal = 5))
    private void placeNewPlayerSync(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        PalladiumLifecycleEvents.DATA_PACK_SYNC.invoker().onDataPackSync((PlayerList) (Object) this, player);
    }

}
