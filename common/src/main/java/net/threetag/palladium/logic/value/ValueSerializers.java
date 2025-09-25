package net.threetag.palladium.logic.value;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class ValueSerializers {

    public static final DeferredRegister<ValueSerializer<?>> VALUE_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.VALUE_SERIALIZER);

    public static final RegistryHolder<StaticValue.Serializer> STATIC = VALUE_SERIALIZERS.register("static", StaticValue.Serializer::new);
    public static final RegistryHolder<ConditionValue.Serializer> CONDITION = VALUE_SERIALIZERS.register("condition", ConditionValue.Serializer::new);
    public static final RegistryHolder<CrouchingValue.Serializer> CROUCHING = VALUE_SERIALIZERS.register("crouching", CrouchingValue.Serializer::new);
    public static final RegistryHolder<EntityTickCountValue.Serializer> ENTITY_TICK_COUNT = VALUE_SERIALIZERS.register("entity_tick_count", EntityTickCountValue.Serializer::new);
    public static final RegistryHolder<EntityHealthValue.Serializer> ENTITY_HEALTH = VALUE_SERIALIZERS.register("entity_health", EntityHealthValue.Serializer::new);
    public static final RegistryHolder<ScoreValue.Serializer> SCORE = VALUE_SERIALIZERS.register("score", ScoreValue.Serializer::new);
    public static final RegistryHolder<WidePlayerModelValue.Serializer> WIDE_PLAYER_MODEL = VALUE_SERIALIZERS.register("wide_player_model", WidePlayerModelValue.Serializer::new);
    public static final RegistryHolder<SlimPlayerModelValue.Serializer> SLIM_PLAYER_MODEL = VALUE_SERIALIZERS.register("slim_player_model", SlimPlayerModelValue.Serializer::new);
    public static final RegistryHolder<AbilityTickCountValue.Serializer> ABILITY_TICK_COUNT = VALUE_SERIALIZERS.register("ability_tick_count", AbilityTickCountValue.Serializer::new);
    public static final RegistryHolder<AnimationTimerValue.Serializer> ANIMATION_TIMER = VALUE_SERIALIZERS.register("animation_timer", AnimationTimerValue.Serializer::new);
    public static final RegistryHolder<AbilityKeyValue.Serializer> ABILITY_KEY = VALUE_SERIALIZERS.register("ability_key", AbilityKeyValue.Serializer::new);
    public static final RegistryHolder<EnergyBarValue.Serializer> ENERGY_BAR = VALUE_SERIALIZERS.register("energy_bar", EnergyBarValue.Serializer::new);
    public static final RegistryHolder<MoonPhaseValue.Serializer> MOON_PHASE = VALUE_SERIALIZERS.register("moon_phase", MoonPhaseValue.Serializer::new);

}
