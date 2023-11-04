package net.threetag.palladium.compat.geckolib.playeranimator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.HashMap;
import java.util.Map;

public class ParsedAnimationController<T extends GeoAnimatable> {

    private final String name;
    private final int transitionTickTime;
    private final RawAnimation rawAnimation;
    private final PresetType preset;
    private final Map<String, RawAnimation> triggers = new HashMap<>();

    public ParsedAnimationController(String name, int transitionTickTime, RawAnimation rawAnimation) {
        this.name = name;
        this.transitionTickTime = transitionTickTime;
        this.rawAnimation = rawAnimation;
        this.preset = null;
    }

    public ParsedAnimationController(PresetType preset) {
        this.name = null;
        this.transitionTickTime = 0;
        this.rawAnimation = null;
        this.preset = preset;
    }

    public ParsedAnimationController<T> triggerableAnim(String key, RawAnimation animation) {
        this.triggers.put(key, animation);
        return this;
    }

    public AnimationController<T> createController(T animatable) {
        AnimationController<T> controller;

        if (this.preset != null) {
            controller = this.preset.createController(animatable);
        } else {
            controller = new AnimationController<>(animatable, this.name, this.transitionTickTime, animationState -> animationState.setAndContinue(this.rawAnimation));
        }

        for (Map.Entry<String, RawAnimation> e : this.triggers.entrySet()) {
            controller.triggerableAnim(e.getKey(), e.getValue()).receiveTriggeredAnimations();
        }

        return controller;
    }

    @NotNull
    public static <T extends GeoAnimatable> ParsedAnimationController<T> controllerFromJson(JsonObject json) {
        ParsedAnimationController<T> controller;

        if (GsonHelper.isValidNode(json, "preset")) {
            var name = GsonHelper.getAsString(json, "preset");
            PresetType presetType = PresetType.fromName(name);

            if (presetType == null) {
                throw new JsonParseException("Unknown animation controller preset '" + name + "'");
            }

            // TODO custom names?
            controller = new ParsedAnimationController<>(presetType);
        } else {
            var name = GsonHelper.getAsString(json, "name");
            var transitionTicks = GsonUtil.getAsIntMin(json, "transition_tick_time", 0);
            var rawAnimation = animationFromJson(json.get("animation"));
            controller = new ParsedAnimationController<>(name, transitionTicks, rawAnimation);
        }

        if (GsonHelper.isValidNode(json, "triggers")) {
            var triggers = GsonHelper.getAsJsonObject(json, "triggers");
            for (Map.Entry<String, JsonElement> e : triggers.entrySet()) {
                controller.triggerableAnim(e.getKey(), animationFromJson(e.getValue()));
            }
        }

        return controller;
    }

    public static RawAnimation animationFromJson(JsonElement element) {
        var animation = RawAnimation.begin();

        if (element == null || element.isJsonNull()) {
            return animation;
        } else if (element.isJsonPrimitive()) {
            animation.thenPlay(element.getAsString());
        } else {
            var jsonArray = GsonHelper.convertToJsonArray(element, "animation");

            for (JsonElement el : jsonArray) {
                var step = GsonHelper.convertToJsonObject(el, "animation[].$");
                var type = GsonHelper.getAsString(step, "type");

                if (type.equalsIgnoreCase("then_play")) {
                    animation.thenPlay(GsonHelper.getAsString(step, "animation"));
                } else if (type.equalsIgnoreCase("then_loop")) {
                    animation.thenLoop(GsonHelper.getAsString(step, "animation"));
                } else if (type.equalsIgnoreCase("then_wait")) {
                    animation.thenWait(GsonHelper.getAsInt(step, "ticks"));
                } else if (type.equalsIgnoreCase("then_play_and_hold")) {
                    animation.thenPlayAndHold(GsonHelper.getAsString(step, "animation"));
                } else if (type.equalsIgnoreCase("then_play_x_times")) {
                    animation.thenPlayXTimes(GsonHelper.getAsString(step, "animation"), GsonHelper.getAsInt(step, "play_count"));
                }
            }
        }

        return animation;
    }

    public enum PresetType {

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

        private final String name;

        PresetType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public <T extends GeoAnimatable> AnimationController<T> createController(T animatable) {
            return switch (this) {
                case LIVING -> DefaultAnimations.genericLivingController(animatable);
                case IDLE -> DefaultAnimations.genericIdleController(animatable);
                case SNEAK ->
                        new AnimationController<T>(animatable, "Sneak", 10, state -> state.setAndContinue(DefaultAnimations.SNEAK));
                case CRAWL ->
                        new AnimationController<T>(animatable, "Crawl", 10, state -> state.setAndContinue(DefaultAnimations.CRAWL));
                case WALK -> DefaultAnimations.genericWalkController(animatable);
                case WALK_OR_ELSE_IDLE -> DefaultAnimations.genericWalkIdleController(animatable);
                case ATTACK -> new AnimationController<>(animatable, "Attack", 5, state -> {
                    if (state.getData(DataTickets.ENTITY) instanceof LivingEntity living && living.swinging)
                        return state.setAndContinue(DefaultAnimations.ATTACK_SWING);

                    state.getController().forceAnimationReset();

                    return PlayState.STOP;
                });
                case SWIM -> DefaultAnimations.genericSwimController(animatable);
                case SWIM_OR_ELSE_IDLE -> DefaultAnimations.genericSwimIdleController(animatable);
                case FLY -> DefaultAnimations.genericFlyController(animatable);
                case FLY_OR_ELSE_IDLE -> DefaultAnimations.genericFlyIdleController(animatable);
                case WALK_RUN_OR_ELSE_IDLE -> new AnimationController<T>(animatable, "Walk/Run/Idle", 0, state -> {
                    if (state.isMoving()) {
                        return state.setAndContinue(state.getData(DataTickets.ENTITY) instanceof LivingEntity en && en.isSprinting() ? DefaultAnimations.RUN : DefaultAnimations.WALK);
                    } else {
                        return state.setAndContinue(DefaultAnimations.IDLE);
                    }
                });
            };
        }

        @Nullable
        public static PresetType fromName(String name) {
            for (PresetType presetType : PresetType.values()) {
                if (presetType.getName().equalsIgnoreCase(name)) {
                    return presetType;
                }
            }
            return null;
        }
    }

}
