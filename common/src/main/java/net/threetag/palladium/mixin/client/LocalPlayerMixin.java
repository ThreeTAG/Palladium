package net.threetag.palladium.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.network.SetFlyingStateMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Unique
    private boolean cachedMayFly = false;

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onUpdateAbilities()V", ordinal = 1, shift = At.Shift.AFTER))
    private void onFlyChange(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (player instanceof PalladiumPlayerExtension extension) {
            var flight = extension.palladium$getFlightHandler();
            if (player.getAbilities().flying) {
                if (flight.getFlightType().isNotNull()) {
                    new SetFlyingStateMessage(false).send();
                    flight.setFlightType(FlightHandler.FlightType.NONE);
                } else {
                    var flightType = FlightHandler.getAvailableFlightType(player);

                    if (flightType.isNull()) {
                        return;
                    }

                    new SetFlyingStateMessage(true).send();
                    flight.setFlightType(flightType);
                }
            } else {
                new SetFlyingStateMessage(false).send();
                flight.setFlightType(FlightHandler.FlightType.NONE);
            }

            player.getAbilities().flying = false;
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void aiStepTop(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        this.cachedMayFly = player.getAbilities().mayfly;
        player.getAbilities().mayfly |= player.getAttributeValue(PalladiumAttributes.LEVITATION_SPEED.get()) > 0D || player.getAttributeValue(PalladiumAttributes.FLIGHT_SPEED.get()) > 0D;
    }

    @Inject(method = "aiStep", at = @At("RETURN"))
    private void aiStepBottom(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        player.getAbilities().mayfly = this.cachedMayFly;
    }

}
