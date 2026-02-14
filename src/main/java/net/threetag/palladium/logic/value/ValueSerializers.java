package net.threetag.palladium.logic.value;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class ValueSerializers {

    public static final DeferredRegister<ValueSerializer<?>> VALUE_SERIALIZERS = DeferredRegister.create(PalladiumRegistryKeys.VALUE_SERIALIZER, Palladium.MOD_ID);

    public static final DeferredHolder<ValueSerializer<?>, StaticValue.Serializer> STATIC = VALUE_SERIALIZERS.register("static", StaticValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, MoLangIntegerValue.Serializer> MOLANG_INTEGER = VALUE_SERIALIZERS.register("molang_integer", MoLangIntegerValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, MoLangFloatValue.Serializer> MOLANG_FLOAT = VALUE_SERIALIZERS.register("molang_float", MoLangFloatValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, MoLangStringValue.Serializer> MOLANG_STRING = VALUE_SERIALIZERS.register("molang_string", MoLangStringValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, ConditionalValue.Serializer> CONDITIONAL = VALUE_SERIALIZERS.register("conditional", ConditionalValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, CrouchingValue.Serializer> CROUCHING = VALUE_SERIALIZERS.register("crouching", CrouchingValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, EntityTickCountValue.Serializer> ENTITY_TICK_COUNT = VALUE_SERIALIZERS.register("entity_tick_count", EntityTickCountValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, EntityHealthValue.Serializer> ENTITY_HEALTH = VALUE_SERIALIZERS.register("entity_health", EntityHealthValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, ScoreValue.Serializer> SCORE = VALUE_SERIALIZERS.register("score", ScoreValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, WidePlayerModelValue.Serializer> WIDE_PLAYER_MODEL = VALUE_SERIALIZERS.register("wide_player_model", WidePlayerModelValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, SlimPlayerModelValue.Serializer> SLIM_PLAYER_MODEL = VALUE_SERIALIZERS.register("slim_player_model", SlimPlayerModelValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, AbilityTickCountValue.Serializer> ABILITY_TICK_COUNT = VALUE_SERIALIZERS.register("ability_tick_count", AbilityTickCountValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, AbilityAnimationTimerValue.Serializer> ABILITY_ANIMATION_TIMER_VALUE = VALUE_SERIALIZERS.register("ability_animation_timer_value", AbilityAnimationTimerValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, AbilityKeyValue.Serializer> ABILITY_KEY = VALUE_SERIALIZERS.register("ability_key", AbilityKeyValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, EnergyBarValue.Serializer> ENERGY_BAR = VALUE_SERIALIZERS.register("energy_bar", EnergyBarValue.Serializer::new);
    public static final DeferredHolder<ValueSerializer<?>, MoonPhaseValue.Serializer> MOON_PHASE = VALUE_SERIALIZERS.register("moon_phase", MoonPhaseValue.Serializer::new);

}
