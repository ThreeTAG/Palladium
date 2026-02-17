package net.threetag.palladium.client.animation;

import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.world.entity.Avatar;
import net.threetag.palladium.entity.flight.EntityFlightHandler;

public class FlightAnimationContainer extends AnimationContainer {

    public FlightAnimationContainer(Avatar player) {
        super(player);
    }
    
    @Override
    public PlayState handle(AnimationController animationController, AnimationData animationData, AnimationController.AnimationSetter animationSetter) {
        var flightHandler = EntityFlightHandler.get(this.getPlayer());
        var animationHandler = flightHandler.getAnimationHandler();

        if (animationHandler != null && animationHandler.getAnimationAssetId() != null && flightHandler.isFlying()) {
            var assetId = animationHandler.getAnimationAssetId();
            return this.setAnimation(assetId, 5, animationController, animationSetter);
        } else {
            return this.setAnimation(null, 5, animationController, animationSetter);
        }
    }
}
