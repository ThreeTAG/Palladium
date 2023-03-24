package net.threetag.palladium.compat.geckolib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.util.json.GsonUtil;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

import java.util.ArrayList;
import java.util.List;

public class ParsedAnimationController<T extends IAnimatable> {

    public final String name;
    private final String initialAnimation;
    private final int transitionTicks;

    public ParsedAnimationController(String name, String initialAnimation, int transitionTicks) {
        this.name = name;
        this.initialAnimation = initialAnimation;
        this.transitionTicks = transitionTicks;
    }

    public ParsedAnimationController(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            this.name = jsonElement.getAsString();
            this.initialAnimation = null;
            this.transitionTicks = 1;
        } else if (jsonElement.isJsonObject()) {
            this.name = GsonHelper.getAsString(jsonElement.getAsJsonObject(), "name");
            this.initialAnimation = GsonHelper.getAsString(jsonElement.getAsJsonObject(), "initial_animation", null);
            this.transitionTicks = GsonUtil.getAsIntMin(jsonElement.getAsJsonObject(), "transition_ticks", 1, 1);
        } else {
            throw new JsonParseException("Animation controller must be primitive for name or object with 'name', 'initial_animation' and 'transition_ticks'");
        }
    }

    public AnimationController<T> createController(T animatable) {
        AnimationController<T> controller = new AnimationController<>(animatable, this.name, this.transitionTicks, ParsedAnimationController::predicate);

        if (this.initialAnimation != null) {
            controller.setAnimation(new AnimationBuilder().addAnimation(this.initialAnimation));
        } else {
            controller.setAnimation(new AnimationBuilder());
        }

        return controller;
    }

    public static PlayState predicate(AnimationEvent<?> e) {
        return PlayState.CONTINUE;
    }

    public static <T extends IAnimatable> List<ParsedAnimationController<T>> getAsList(JsonObject json, String memberName) {
        List<ParsedAnimationController<T>> controllers = new ArrayList<>();

        if (json.has(memberName)) {
            GsonUtil.forEachInListOrPrimitive(json.get(memberName), el -> controllers.add(new ParsedAnimationController<>(el)));
        } else {
            controllers.add(new ParsedAnimationController<>("main", null, 1));
        }

        return controllers;
    }
}
