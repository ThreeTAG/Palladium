package net.threetag.palladium.client.variable;

import net.threetag.palladium.Palladium;

public class PathVariableSerializers {

    public static final PathVariableSerializer<ConditionVariable> CONDITION = register("condition", new ConditionVariable.Serializer());
    public static final PathVariableSerializer<CrouchingVariable> CROUCHING = register("crouching", new CrouchingVariable.Serializer());
    public static final PathVariableSerializer<EntityTickCountVariable> ENTITY_TICK_COUNT = register("entity_tick_count", new EntityTickCountVariable.Serializer());
    public static final PathVariableSerializer<EntityHealthVariable> ENTITY_HEALTH = register("entity_health", new EntityHealthVariable.Serializer());
    public static final PathVariableSerializer<ScoreVariable> SCORE = register("score", new ScoreVariable.Serializer());
    public static final PathVariableSerializer<WidePlayerModelVariable> WIDE_PLAYER_MODEL = register("wide_player_model", new WidePlayerModelVariable.Serializer());
    public static final PathVariableSerializer<SlimPlayerModelVariable> SLIM_PLAYER_MODEL = register("slim_player_model", new SlimPlayerModelVariable.Serializer());
    public static final PathVariableSerializer<AbilityTickCountVariable> ABILITY_TICK_COUNT = register("ability_tick_count", new AbilityTickCountVariable.Serializer());
    public static final PathVariableSerializer<AnimationTimerVariable> ANIMATION_TIMER = register("animation_timer", new AnimationTimerVariable.Serializer());
    public static final PathVariableSerializer<AbilityKeyVariable> ABILITY_KEY = register("ability_key", new AbilityKeyVariable.Serializer());
    public static final PathVariableSerializer<EnergyBarVariable> ENERGY_BAR = register("energy_bar", new EnergyBarVariable.Serializer());
    public static final PathVariableSerializer<MoonPhaseVariable> MOON_PHASE = register("moon_phase", new MoonPhaseVariable.Serializer());

    private static <T extends PathVariable> PathVariableSerializer<T> register(String id, PathVariableSerializer<T> serializer) {
        return PathVariableSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
