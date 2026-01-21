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
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.power.PowerHolder;
import net.threetag.palladium.power.PowerUtil;
import net.threetag.palladium.power.ability.AbilityInstance;

import java.util.*;

public class PalladiumAnimationManager extends SimpleJsonResourceReloadListener<PalladiumAnimation> {

    public static final Identifier ID = Palladium.id("animations");
    public static final PalladiumAnimationManager INSTANCE = new PalladiumAnimationManager();

    public static final Identifier COSMETIC_ANIMATION = Palladium.id("cosmetic_animation");
    public static final Identifier IDLE_ANIMATION = Palladium.id("idle_animation");
    public static final Identifier ACTIVE_ANIMATION = Palladium.id("active_animation");

    private final Map<Identifier, PalladiumAnimation> byName = new HashMap<>();

    public PalladiumAnimationManager() {
        super(PalladiumAnimation.CODEC, FileToIdConverter.json("palladium/animations"));
    }

    public static void registerLayers(FMLClientSetupEvent e) {
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(COSMETIC_ANIMATION, 500,
                player -> new PlayerAnimationController(player, new PalladiumAnimationHandler(0))
        ));
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(IDLE_ANIMATION, 1000,
                player -> new PlayerAnimationController(player, new PalladiumAnimationHandler(1))
        ));
        e.enqueueWork(() -> PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ACTIVE_ANIMATION, 1500,
                player -> new PlayerAnimationController(player, new PalladiumAnimationHandler(2))
        ));
    }

    @Override
    protected void apply(Map<Identifier, PalladiumAnimation> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " animations");
    }

    public PalladiumAnimation get(Identifier id) {
        return this.byName.get(id);
    }

    public static class PalladiumAnimationHandler implements AnimationController.AnimationStateHandler {

        private final int animationLayer;

        public PalladiumAnimationHandler(int animationLayer) {
            this.animationLayer = animationLayer;
        }

        @Override
        public PlayState handle(AnimationController animationController, AnimationData animationData, AnimationController.AnimationSetter animationSetter) {
            if (animationController instanceof PlayerAnimationController) {
                Avatar a = ((PlayerAnimationController) animationController).getAvatar();
                Optional<Identifier> animation = getFirstEnabledAnimation(a, animationLayer);
                if (animation.isEmpty()){
                    if(!animationController.hasAnimationFinished())
                        animationController.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(10, EasingType.LINEAR), new Animation(new ExtraAnimationData("name", AnimationStage.WAIT.name()), 10, Animation.LoopType.PLAY_ONCE, Collections.emptyMap(), UniversalAnimLoader.NO_KEYFRAMES, new HashMap<>(), new HashMap<>()));
                    return PlayState.STOP;
                }
                return animationSetter.setAnimation(PlayerRawAnimationBuilder.begin().thenPlay(animation.get()).build());
            }
            return PlayState.STOP;
        }

        private static Optional<Identifier> getFirstEnabledAnimation(LivingEntity entity, int animationLayer) {
            for (PowerHolder holder : PowerUtil.getPowerHandler(entity).getPowerHolders().values()) {
                for (AbilityInstance<?> value : holder.getAbilities().values()) {
                    Optional<Identifier> animation = value.getAbility().getProperties().getAnimation();
                    if (value.isEnabled() && animation.isPresent() && value.getAbility().getProperties().getAnimationLayer() == animationLayer) {
                        return animation;
                    }
                }
            }
            return Optional.empty();
        }
    }
}
