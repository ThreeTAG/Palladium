package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;

import java.util.Objects;

public abstract class AnimationContainer {

    private final Avatar player;
    protected final AnimationController animationController;
    private Identifier currentAnimation;

    protected AnimationContainer(Avatar player, AnimationController animationController) {
        this.player = player;
        this.animationController = animationController;
    }

    abstract void tick();

    public Avatar getPlayer() {
        return player;
    }

    public void setAnimation(Identifier animationId, int blendTransition) {
        if (animationId == null) {
            animationId = PalladiumAnimationManager.EMPTY_ANIMATION;
        }

        if (!Objects.equals(animationId, this.currentAnimation)) {
            this.currentAnimation = animationId;
            this.animationController.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(blendTransition, EasingType.EASE_IN_OUT_SINE), RawAnimation.begin().thenPlay(PlayerAnimResources.getAnimation(this.currentAnimation)), false);
        }
    }

}
