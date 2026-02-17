package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimResources;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.RawAnimation;
import com.zigythebird.playeranimcore.animation.layered.AnimationSnapshot;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.bones.AdvancedBoneSnapshot;
import com.zigythebird.playeranimcore.bones.PlayerAnimBone;
import com.zigythebird.playeranimcore.easing.EasingType;
import com.zigythebird.playeranimcore.enums.PlayState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AnimationContainer implements AnimationController.AnimationStateHandler {

    private final Avatar player;
    private Identifier currentAnimation;

    protected AnimationContainer(Avatar player) {
        this.player = player;
    }

    public Avatar getPlayer() {
        return player;
    }

    public PlayState setAnimation(Identifier animationId, int blendTransition, AnimationController animationController, AnimationController.AnimationSetter animationSetter) {
        if (animationId == null) {
            animationId = PalladiumAnimationManager.EMPTY_ANIMATION;
        }

        if (!Objects.equals(animationId, this.currentAnimation)) {
            if(!Objects.equals(PalladiumAnimationManager.EMPTY_ANIMATION, this.currentAnimation) && animationController instanceof RenderLayerAwareAnimationController controller){
                AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(blendTransition, EasingType.EASE_IN_OUT_SINE);
                Map<String, AdvancedBoneSnapshot> snapshots = new HashMap<>();

                for(PlayerAnimBone bone : controller.getActiveBones().values()) {
                    snapshots.put(bone.getName(), new AdvancedBoneSnapshot(bone));
                }

                fadeModifier.setTransitionAnimation(new AnimationSnapshot(snapshots));
                animationController.addModifierLast(fadeModifier);
            }
            this.currentAnimation = animationId;
        }

        return animationSetter.setAnimation(RawAnimation.begin().thenPlay(PlayerAnimResources.getAnimation(this.currentAnimation)));
    }
}
