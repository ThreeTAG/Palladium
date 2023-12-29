package net.threetag.palladium.mixin.forge;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.network.SyncPropertyMessage;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.forge.NetworkManagerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManagerImpl.class)
public class NetworkManagerMixin {

    // Class purely exists for analysing purposes
    private static final boolean DEBUG = false;

    @Inject(method = "sendToServer", at = @At("RETURN"), remap = false)
    public void sendToServer(MessageC2S message, CallbackInfo ci) {
        if (DEBUG) {
            Palladium.LOGGER.info("TO SERVER: " + message.getType().getId());
        }
    }

    @Inject(method = "sendToPlayer", at = @At("RETURN"), remap = false)
    public void sendToPlayer(ServerPlayer player, MessageS2C message, CallbackInfo ci) {
        if (DEBUG) {
            Palladium.LOGGER.info("TO PLAYER " + player.getName() + ": " + message.getType().getId());
            if (message instanceof SyncPropertyMessage sync) {
                for (String key : sync.tag.getAllKeys()) {
                    Palladium.LOGGER.info("  - " + key + ": " + sync.tag.get(key).getAsString());
                }
            }
        }
    }

    @Inject(method = "sendToTracking", at = @At("RETURN"), remap = false)
    public void sendToTracking(Entity entity, MessageS2C message, CallbackInfo ci) {
        if (DEBUG) {
            Palladium.LOGGER.info("TO TRACKING " + entity.getName() + ": " + message.getType().getId());
        }
    }

    @Inject(method = "sendToTrackingAndSelf", at = @At("RETURN"), remap = false)
    public void sendToTrackingAndSelf(ServerPlayer player, MessageS2C message, CallbackInfo ci) {
        if (DEBUG) {
            Palladium.LOGGER.info("TO TRACKING & S " + player.getName() + ": " + message.getType().getId());
        }
    }

}
