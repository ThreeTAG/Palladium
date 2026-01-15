package net.threetag.palladium.compat.geckolib.layer;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AnimationControllerFactory<T extends GeoAnimatable> {

    private static final Codec<AnimationControllerFactory<?>> CUSTOM_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(f -> f.name),
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("transition_tick_time").forGetter(f -> f.transitionTickTime),
            RawAnimationCodec.CODEC.optionalFieldOf("animation").forGetter(f -> Optional.ofNullable(f.rawAnimation)),
            Codec.unboundedMap(Codec.STRING, RawAnimationCodec.CODEC).optionalFieldOf("triggers", new HashMap<>()).forGetter(f -> f.triggers)
    ).apply(instance, (name, ticks, anim, triggers) -> new AnimationControllerFactory<>(name, ticks, anim.orElse(null), triggers)));

    private static final Codec<AnimationControllerFactory<?>> PRESET_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PresetType.CODEC.fieldOf("preset").forGetter(f -> f.preset)
    ).apply(instance, AnimationControllerFactory::new));

    public static final Codec<AnimationControllerFactory<?>> CODEC = Codec.either(CUSTOM_CODEC, PRESET_CODEC).xmap(
            either -> either.map(
                    factory -> factory,
                    factory -> factory
            ),
            factory -> factory.preset != null ? Either.right(factory) : Either.left(factory)
    );

    private final String name;
    private final int transitionTickTime;
    private final RawAnimation rawAnimation;
    private final PresetType preset;
    private Map<String, RawAnimation> triggers;

    public AnimationControllerFactory(String name, int transitionTickTime, RawAnimation rawAnimation, Map<String, RawAnimation> triggers) {
        this.name = name;
        this.transitionTickTime = transitionTickTime;
        this.rawAnimation = rawAnimation;
        this.preset = null;
        this.triggers = triggers;
    }

    public AnimationControllerFactory(PresetType preset) {
        this.name = null;
        this.transitionTickTime = 0;
        this.rawAnimation = null;
        this.preset = preset;
    }

    public AnimationController<T> create(T animatable) {
        AnimationController<T> controller;

        if (this.preset != null) {
            controller = this.preset.createController(animatable);
        } else {
            controller = new AnimationController<>(this.name, this.transitionTickTime, animationState -> animationState.setAndContinue(this.rawAnimation));
        }

        for (Map.Entry<String, RawAnimation> e : this.triggers.entrySet()) {
            controller.triggerableAnim(e.getKey(), e.getValue()).receiveTriggeredAnimations();
        }

        return controller;
    }

    public enum PresetType implements StringRepresentable {

        LIVING("living"),
        IDLE("idle"),
        SNEAK("sneak"),
        CRAWL("crawl"),
        WALK("walk"),
        WALK_OR_ELSE_IDLE("walk_or_else_idle"),
        ATTACK("attack"),
        SWIM("swim"),
        SWIM_OR_ELSE_IDLE("swim_or_else_idle"),
        FLY("fly"),
        FLY_OR_ELSE_IDLE("fly_or_else_idle"),
        WALK_RUN_OR_ELSE_IDLE("walk_run_or_else_idle");

        public static final Codec<PresetType> CODEC = StringRepresentable.fromEnum(PresetType::values);
        private final String name;

        PresetType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        // TODO overhaul
        public <T extends GeoAnimatable> AnimationController<T> createController(T animatable) {
            return switch (this) {
                case LIVING -> DefaultAnimations.genericLivingController();
                case IDLE -> DefaultAnimations.genericIdleController();
                case SNEAK ->
                        new AnimationController<T>("Sneak", 10, state -> state.setAndContinue(DefaultAnimations.SNEAK));
                case CRAWL ->
                        new AnimationController<T>("Crawl", 10, state -> state.setAndContinue(DefaultAnimations.CRAWL));
                case WALK -> DefaultAnimations.genericWalkController();
                case WALK_OR_ELSE_IDLE -> DefaultAnimations.genericWalkIdleController();
                case ATTACK -> new AnimationController<>("Attack", 5, state -> {
                    if (state.getDataOrDefault(DataTickets.SWINGING_ARM, false))
                        return state.setAndContinue(DefaultAnimations.ATTACK_PUNCH);

                    return PlayState.STOP;
                });
                case SWIM -> DefaultAnimations.genericSwimController();
                case SWIM_OR_ELSE_IDLE -> DefaultAnimations.genericSwimIdleController();
                case FLY -> DefaultAnimations.genericFlyController();
                case FLY_OR_ELSE_IDLE -> DefaultAnimations.genericFlyIdleController();
                case WALK_RUN_OR_ELSE_IDLE -> new AnimationController<T>("Walk/Run/Idle", 0, state -> {
                    if (state.isMoving())
                        return state.setAndContinue(Boolean.TRUE.equals(state.getDataOrDefault(DataTickets.SPRINTING, false)) ? DefaultAnimations.RUN : DefaultAnimations.WALK);
                    return state.setAndContinue(DefaultAnimations.IDLE);
                });
            };
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }

}
