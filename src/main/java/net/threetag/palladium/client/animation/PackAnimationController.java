package net.threetag.palladium.client.animation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record PackAnimationController(String initialState, int blendTransition, Map<String, State> states) {

    public static final Codec<PackAnimationController> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("initial_state").forGetter(PackAnimationController::initialState),
            PalladiumCodecs.TIME.optionalFieldOf("blend_transition", 5).forGetter(PackAnimationController::blendTransition),
            Codec.unboundedMap(Codec.STRING, State.CODEC).fieldOf("states").forGetter(PackAnimationController::states)
    ).apply(instance, PackAnimationController::new));

    public record State(Identifier animation, List<Transition> transitions) {

        public static final Codec<State> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.optionalFieldOf("animation", PalladiumAnimationManager.EMPTY_ANIMATION).forGetter(State::animation),
                Transition.LIST_CODEC.optionalFieldOf("transitions", Collections.emptyList()).forGetter(State::transitions)
        ).apply(instance, State::new));

    }

    public record Transition(String state, Condition condition) {

        public static final Codec<List<Transition>> LIST_CODEC = Codec.unboundedMap(Codec.STRING, Condition.CODEC)
                .xmap(map -> map.entrySet().stream().map(e -> new Transition(e.getKey(), e.getValue())).toList(),
                        transitions -> transitions.stream().collect(Collectors.toMap(Transition::state, Transition::condition)));

    }
}
