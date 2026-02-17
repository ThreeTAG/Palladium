package net.threetag.palladium.client.animation;

import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.world.entity.Avatar;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerUtil;

public class PackControllerAnimationContainer extends AnimationContainer {

    private final PalladiumAnimationLayer layer;
    private PackAnimationController currentAnimationController;
    private PackAnimationController.State currentState;

    public PackControllerAnimationContainer(PalladiumAnimationLayer layer, Avatar player) {
        super(player);
        this.layer = layer;
    }

    private int getBlendTransition() {
        return this.currentAnimationController != null ? this.currentAnimationController.blendTransition() : 5;
    }

    public PackAnimationController getAvailableAnimationController(Avatar player) {
        for (Power power : PowerUtil.getPowers(player)) {
            var id = power.getAnimationController(this.layer);
            if (id != null) {
                var controller = PackAnimationControllerManager.INSTANCE.get(id);

                if (controller != null) {
                    return controller;
                }
            }
        }

        return null;
    }

    @Override
    public PlayState handle(AnimationController animationController, AnimationData animationData, AnimationController.AnimationSetter animationSetter) {
        var current = getAvailableAnimationController(this.getPlayer());

        if (current != this.currentAnimationController) {
            this.currentAnimationController = current;
            this.currentState = null;
        }

        if (this.currentAnimationController != null) {
            if (this.currentState == null) {
                this.currentState = this.currentAnimationController.states().get(this.currentAnimationController.initialState());
            }

            if (this.currentState != null) {
                var context = DataContext.forEntity(this.getPlayer()).with(DataContextKeys.ANY_ANIMATION_FINISHED, animationController.hasAnimationFinished());

                for (PackAnimationController.Transition transition : this.currentState.transitions()) {
                    if (transition.condition().test(context)) {
                        this.currentState = this.currentAnimationController.states().get(transition.state());

                        if (this.currentState != null) {
                            return this.setAnimation(this.currentState.animation(), this.getBlendTransition(), animationController, animationSetter);
                        } else {
                            return this.setAnimation(null, this.getBlendTransition(), animationController, animationSetter);
                        }
                    }
                }
                return this.setAnimation(this.currentState.animation(), this.getBlendTransition(), animationController, animationSetter);
            } else {
                return this.setAnimation(null, this.getBlendTransition(), animationController, animationSetter);
            }
        } else {
            return this.setAnimation(null, this.getBlendTransition(), animationController, animationSetter);
        }
    }
}
