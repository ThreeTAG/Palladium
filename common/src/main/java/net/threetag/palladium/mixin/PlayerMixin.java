package net.threetag.palladium.mixin;

import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(Player.class)
public abstract class PlayerMixin implements PalladiumPlayerExtension {

    @Unique
    private final FlightHandler palladium$flightHandler = new FlightHandler((Player) (Object) this);

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        this.palladium$flightHandler.tick();
    }

    @ModifyVariable(method = "getDimensions", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Pose getDimensions(Pose pose) {
        var hover = this.palladium$flightHandler.getHoveringAnimation(0);
        var levitation = this.palladium$flightHandler.getLevitationAnimation(0);
        var flight = this.palladium$flightHandler.getFlightAnimation(0);

        if (hover > 0F || levitation > 0F || flight > 0F) {
            if (this.palladium$flightHandler.flightBoost > 1F) {
                return Pose.FALL_FLYING;
            } else {
                return Pose.STANDING;
            }
        }
        return pose;
    }

    @ModifyVariable(method = "getStandingEyeHeight", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private Pose getStandingEyeHeight(Pose pose) {
        var hover = this.palladium$flightHandler.getHoveringAnimation(0);
        var levitation = this.palladium$flightHandler.getLevitationAnimation(0);
        var flight = this.palladium$flightHandler.getFlightAnimation(0);

        if (hover > 0F || levitation > 0F || flight > 0F) {
            if (this.palladium$flightHandler.flightBoost > 1F) {
                return Pose.FALL_FLYING;
            } else {
                return Pose.STANDING;
            }
        }
        return pose;
    }



}
