package net.threetag.palladium.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.threetag.palladium.entity.data.PalladiumEntityDataTypes;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.network.ToggleEntityFlightPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("DataFlowIssue")
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Unique
    private boolean palladium$cachedMayFly = false;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onUpdateAbilities()V", ordinal = 1, shift = At.Shift.AFTER))
    private void onFlyChange(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        var flight = EntityFlightHandler.get(player, PalladiumEntityDataTypes.FLIGHT.get());

        if (player.getAbilities().flying) {
            if (flight.getFlightType() != null) {
                ClientPacketDistributor.sendToServer(new ToggleEntityFlightPacket(false));
                flight.stopFlight();
            } else if (flight.startFlight()) {
                ClientPacketDistributor.sendToServer(new ToggleEntityFlightPacket(true));
            } else {
                return;
            }
        } else {
            ClientPacketDistributor.sendToServer(new ToggleEntityFlightPacket(false));
            flight.stopFlight();
        }

        player.getAbilities().flying = false;
    }

    @Inject(method = "isCrouching", at = @At("HEAD"), cancellable = true)
    private void isCrouching(CallbackInfoReturnable<Boolean> cir) {
        var player = (Player) (Object) this;

        if (EntityFlightHandler.get(player).isFlying()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStepTop(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        this.palladium$cachedMayFly = player.getAbilities().mayfly;
        player.getAbilities().mayfly |= EntityFlightHandler.getAvailableFlightType(player) != null;
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    private void aiStepBottom(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        player.getAbilities().mayfly = this.palladium$cachedMayFly;
    }

}
