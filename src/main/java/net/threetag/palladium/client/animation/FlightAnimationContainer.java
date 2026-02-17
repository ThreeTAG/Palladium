package net.threetag.palladium.client.animation;

import com.zigythebird.playeranimcore.animation.AnimationController;
import net.minecraft.world.entity.Avatar;
import net.threetag.palladium.entity.flight.EntityFlightHandler;

public class FlightAnimationContainer extends AnimationContainer {

    public FlightAnimationContainer(Avatar player, AnimationController animationController) {
        super(player, animationController);
    }

    @Override
    public void tick() {
        var flightHandler = EntityFlightHandler.get(this.getPlayer());
        var animationHandler = flightHandler.getAnimationHandler();

        if (animationHandler != null && animationHandler.getAnimationAssetId() != null && flightHandler.isFlying()) {
            var assetId = animationHandler.getAnimationAssetId();
            this.setAnimation(assetId, 5);
        } else {
            this.setAnimation(null, 5);
        }
    }
}
