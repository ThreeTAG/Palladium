package net.threetag.palladium.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.entity.TrailSegmentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Shadow
    private ClientLevel level;

    @Inject(method = "handleSetEntityData", at = @At("HEAD"), cancellable = true)
    public void handleSetEntityData(ClientboundSetEntityDataPacket packet, CallbackInfo ci) {
        Entity entity = this.level.getEntity(packet.id());

        if (entity instanceof TrailSegmentEntity<?>) {
            ci.cancel();
        }
    }

}
