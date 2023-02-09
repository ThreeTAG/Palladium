package net.threetag.palladium.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.network.SetFlyingStateMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onUpdateAbilities()V", ordinal = 1, shift = At.Shift.AFTER))
    private void onFlyChange(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;

        if (player instanceof PalladiumPlayerExtension extension) {
            if (player.getAbilities().flying) {
                var flightType = FlightHandler.getAvailableFlightType(player);

                if (flightType.isNull()) {
                    return;
                }

                new SetFlyingStateMessage(player.getAbilities().flying).send();
                player.getAbilities().flying = false;

                extension.palladium_setFlightType(flightType);
            } else {
                extension.palladium_setFlightType(FlightHandler.FlightType.NONE);
            }
        }
    }
}
