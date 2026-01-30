package net.threetag.palladium.client.animation;

import com.zigythebird.playeranim.animation.PlayerAnimationController;
import com.zigythebird.playeranim.animation.PlayerRawAnimationBuilder;
import com.zigythebird.playeranim.api.PlayerAnimationFactory;
import com.zigythebird.playeranimcore.animation.Animation;
import com.zigythebird.playeranimcore.animation.AnimationController;
import com.zigythebird.playeranimcore.animation.AnimationData;
import com.zigythebird.playeranimcore.animation.ExtraAnimationData;
import com.zigythebird.playeranimcore.animation.layered.modifier.AbstractFadeModifier;
import com.zigythebird.playeranimcore.easing.EasingType;
import com.zigythebird.playeranimcore.enums.AnimationStage;
import com.zigythebird.playeranimcore.enums.PlayState;
import com.zigythebird.playeranimcore.loading.UniversalAnimLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityProperties;
import net.threetag.palladium.power.ability.AbilityUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;

public class PalladiumAnimationManager {

    public static final Identifier FLIGHT_ANIMATION_LAYER = Palladium.id("flight_animation");

    public static void registerLayers(FMLClientSetupEvent e) {
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(AbilityProperties.COSMETIC_ANIMATION_LAYER, 500,
                player -> new PlayerAnimationController(player, new AbilityAnimationHandler(AbilityProperties.COSMETIC_ANIMATION_LAYER))
        ));
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(AbilityProperties.IDLE_ANIMATION_LAYER, 1000,
                player -> new PlayerAnimationController(player, new AbilityAnimationHandler(AbilityProperties.IDLE_ANIMATION_LAYER))
        ));
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(FLIGHT_ANIMATION_LAYER, 1500,
                player -> new PlayerAnimationController(player, new FlightAnimationHandler())
        ));
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(AbilityProperties.ACTIVE_ANIMATION_LAYER, 2000,
                player -> {

                    PlayerAnimationController c = new PlayerAnimationController(player, new AbilityAnimationHandler(AbilityProperties.ACTIVE_ANIMATION_LAYER));
                    c.registerPlayerAnimBone("head.halo");
                    return c;
                }
        ));
    }

    private record AbilityAnimationHandler(Identifier id) implements AnimationController.AnimationStateHandler {

        @Override
        public PlayState handle(AnimationController animationController, AnimationData animationData, AnimationController.AnimationSetter animationSetter) {
            if (animationController instanceof PlayerAnimationController) {
                Avatar a = ((PlayerAnimationController) animationController).getAvatar();
                Optional<Identifier> animation = getFirstEnabledAnimation(a, this.id);
                if (animation.isEmpty()) {
                    if (!animationController.hasAnimationFinished()) {
                        animationController.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, EasingType.LINEAR), new Animation(new ExtraAnimationData("name", AnimationStage.WAIT.name()), 10, Animation.LoopType.PLAY_ONCE, Collections.emptyMap(), UniversalAnimLoader.NO_KEYFRAMES, new HashMap<>(), new HashMap<>()));
                    }
                    return PlayState.STOP;
                } else {
                    return animationSetter.setAnimation(PlayerRawAnimationBuilder.begin().thenPlay(animation.get()).build());
                }
            }
            return PlayState.STOP;
        }

        private static Optional<Identifier> getFirstEnabledAnimation(LivingEntity entity, Identifier id) {
            for (AbilityInstance<?> instance : AbilityUtil.getEnabledInstances(entity)) {
                Optional<Identifier> animation = instance.getAbility().getProperties().getAnimation();
                if (animation.isPresent() && instance.getAbility().getProperties().getAnimationLayer().equals(id)) {
                    return animation;
                }
            }
            return Optional.empty();
        }
    }

    private static class FlightAnimationHandler implements AnimationController.AnimationStateHandler {

        @Override
        public PlayState handle(AnimationController animationController, AnimationData animationData, AnimationController.AnimationSetter animationSetter) {
            if (animationController instanceof PlayerAnimationController) {
                Avatar a = ((PlayerAnimationController) animationController).getAvatar();
                var flight = EntityFlightHandler.get(a);
                var animationHandler = flight.getAnimationHandler();
                var animationId = animationHandler != null ? animationHandler.getAnimationAssetId() : null;

                if (animationId == null || !flight.isFlying()) {
                    if (!animationController.hasAnimationFinished()) {
                        animationController.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, EasingType.LINEAR), new Animation(new ExtraAnimationData("name", AnimationStage.WAIT.name()), 10, Animation.LoopType.PLAY_ONCE, Collections.emptyMap(), UniversalAnimLoader.NO_KEYFRAMES, new HashMap<>(), new HashMap<>()));
                    }
                    return PlayState.STOP;
                } else {
                    return animationSetter.setAnimation(PlayerRawAnimationBuilder.begin().thenPlay(animationId).build());
                }
            }
            return PlayState.STOP;
        }
    }
}
