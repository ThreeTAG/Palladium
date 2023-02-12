package net.threetag.palladium.mixin;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(Player.class)
public class PlayerMixin implements PalladiumPlayerExtension {

    @Unique
    private Vec3 prevMovementDelta = null;

    @Unique
    private int flightAnimation = 0;

    @Unique
    private int prevFlightAnimation = 0;

    @Unique
    private float speed = 0;

    @Unique
    private float prevSpeed = 0;

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        var player = (Player) (Object) this;
        this.prevMovementDelta = player.getDeltaMovement();
        this.prevSpeed = this.speed;
        this.speed = (float) Math.sqrt(this.prevMovementDelta.x * this.prevMovementDelta.x + this.prevMovementDelta.y * this.prevMovementDelta.y + this.prevMovementDelta.z * this.prevMovementDelta.z);

        var flightType = FlightHandler.getCurrentFlightType(player);
        this.prevFlightAnimation = this.flightAnimation;

        if (flightType.isNotNull()) {
            if (player.isInWater()) {
                if (this.flightAnimation > 0) {
                    this.flightAnimation--;
                }
            } else {
                if (player.zza > 0.0F) {
                    if (player.isSprinting()) {
                        if (this.flightAnimation < 30) {
                            this.flightAnimation++;
                        }
                    } else {
                        if (this.flightAnimation < 20) {
                            this.flightAnimation++;
                        } else if (this.flightAnimation > 20) {
                            this.flightAnimation--;
                        }
                    }
                } else if (this.flightAnimation > 0) {
                    this.flightAnimation--;
                }
            }
        } else if (this.flightAnimation > 0) {
            this.flightAnimation--;
        }
    }

    @Override
    public Vec3 palladium_getPrevMovementDelta() {
        return this.prevMovementDelta;
    }

    @Override
    public float palladium_getFlightAnimation(float partialTicks) {
        return this.prevFlightAnimation + (this.flightAnimation - this.prevFlightAnimation) * partialTicks;
    }

    @Override
    public float palladium_getSpeed(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevSpeed, this.speed);
    }

}
