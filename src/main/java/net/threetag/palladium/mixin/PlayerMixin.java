package net.threetag.palladium.mixin;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {

    @Inject(method = "getDesiredPose", at = @At("RETURN"), cancellable = true)
    private void getDesiredPose(CallbackInfoReturnable<Pose> cir) {
        var player = (Player) (Object) this;

        if (cir.getReturnValue() == Pose.CROUCHING && EntityFlightHandler.get(player).isFlying()) {
            cir.setReturnValue(Pose.STANDING);
        }
    }

}
