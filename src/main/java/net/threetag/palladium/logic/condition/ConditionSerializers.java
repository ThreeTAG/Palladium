package net.threetag.palladium.logic.condition;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class ConditionSerializers {

    public static final DeferredRegister<ConditionSerializer<?>> CONDITION_SERIALIZERS = DeferredRegister.create(PalladiumRegistryKeys.CONDITION_SERIALIZER, Palladium.MOD_ID);

    // TODO
    public static final DeferredHolder<ConditionSerializer<?>, TrueCondition.Serializer> TRUE = CONDITION_SERIALIZERS.register("true", TrueCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, FalseCondition.Serializer> FALSE = CONDITION_SERIALIZERS.register("false", FalseCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, NotCondition.Serializer> NOT = CONDITION_SERIALIZERS.register("not", NotCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, OrCondition.Serializer> OR = CONDITION_SERIALIZERS.register("or", OrCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AndCondition.Serializer> AND = CONDITION_SERIALIZERS.register("and", AndCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, HasPowerCondition.Serializer> HAS_POWER = CONDITION_SERIALIZERS.register("has_power", HasPowerCondition.Serializer::new);
//    public static final DeferredHolder<ConditionSerializer<?>, WearsSuitSetCondition.Serializer> WEARS_SUIT_SET = CONDITION_SERIALIZERS.register("wears_suit_set", WearsSuitSetCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityUnlockedCondition.Serializer> ABILITY_UNLOCKED = CONDITION_SERIALIZERS.register("ability_unlocked", AbilityUnlockedCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityTypeUnlockedCondition.Serializer> ABILITY_TYPE_UNLOCKED = CONDITION_SERIALIZERS.register("ability_type_unlocked", AbilityTypeUnlockedCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityEnabledCondition.Serializer> ABILITY_ENABLED = CONDITION_SERIALIZERS.register("ability_enabled", AbilityEnabledCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityTypeEnabledCondition.Serializer> ABILITY_TYPE_ENABLED = CONDITION_SERIALIZERS.register("ability_type_enabled", AbilityTypeEnabledCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityOnCooldownCondition.Serializer> ABILITY_ON_COOLDOWN = CONDITION_SERIALIZERS.register("ability_on_cooldown", AbilityOnCooldownCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, HealthCondition.Serializer> HEALTH = CONDITION_SERIALIZERS.register("health", HealthCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, ObjectiveScoreCondition.Serializer> OBJECTIVE_SCORE = CONDITION_SERIALIZERS.register("objective_score", ObjectiveScoreCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, HasTagCondition.Serializer> HAS_TAG = CONDITION_SERIALIZERS.register("has_tag", HasTagCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AnimationTimerAbilityCondition.Serializer> ANIMATION_TIMER_ABILITY = CONDITION_SERIALIZERS.register("animation_timer_ability", AnimationTimerAbilityCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityTicksCondition.Serializer> ABILITY_TICKS = CONDITION_SERIALIZERS.register("ability_ticks", AbilityTicksCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityFirstTickCondition.Serializer> ABILITY_FIRST_TICK = CONDITION_SERIALIZERS.register("ability_first_tick", AbilityFirstTickCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, AbilityLastTickCondition.Serializer> ABILITY_LAST_TICK = CONDITION_SERIALIZERS.register("ability_last_tick", AbilityLastTickCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, CrouchingCondition.Serializer> CROUCHING = CONDITION_SERIALIZERS.register("crouching", CrouchingCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, PoseCondition.Serializer> POSE = CONDITION_SERIALIZERS.register("pose", PoseCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, MoonPhaseCondition.Serializer> MOON_PHASE = CONDITION_SERIALIZERS.register("moon_phase", MoonPhaseCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, ModLoadedCondition.Serializer> MOD_LOADED = CONDITION_SERIALIZERS.register("mod_loaded", ModLoadedCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, ItemInSlotCondition.Serializer> ITEM_IN_SLOT = CONDITION_SERIALIZERS.register("item_in_slot", ItemInSlotCondition.Serializer::new);
//    public static final DeferredHolder<ConditionSerializer<?>, ItemInSlotOpenCondition.Serializer> ITEM_IN_SLOT_OPEN = CONDITION_SERIALIZERS.register("item_in_slot_open", ItemInSlotOpenCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, EmptySlotCondition.Serializer> EMPTY_SLOT = CONDITION_SERIALIZERS.register("empty_slot", EmptySlotCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, EntityTypeCondition.Serializer> ENTITY_TYPE = CONDITION_SERIALIZERS.register("entity_type", EntityTypeCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, BrightnessAtPositionCondition.Serializer> BRIGHTNESS_AT_POSITION = CONDITION_SERIALIZERS.register("brightness_at_position", BrightnessAtPositionCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, OnGroundCondition.Serializer> ON_GROUND = CONDITION_SERIALIZERS.register("on_ground", OnGroundCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsMovingCondition.Serializer> IS_MOVING = CONDITION_SERIALIZERS.register("is_moving", IsMovingCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, HasMovementInputCondition.Serializer> HAS_MOVEMENT_INPUT = CONDITION_SERIALIZERS.register("has_movement_input", HasMovementInputCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, CommandResultCondition.Serializer> COMMAND_RESULT = CONDITION_SERIALIZERS.register("command_result", CommandResultCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, SprintingCondition.Serializer> SPRINTING = CONDITION_SERIALIZERS.register("sprinting", SprintingCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, DimensionCondition.Serializer> DIMENSION = CONDITION_SERIALIZERS.register("dimension", DimensionCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsOnFireCondition.Serializer> IS_ON_FIRE = CONDITION_SERIALIZERS.register("is_on_fire", IsOnFireCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsSwimmingCondition.Serializer> IS_SWIMMING = CONDITION_SERIALIZERS.register("is_swimming", IsSwimmingCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsElytraFlyingCondition.Serializer> IS_ELYTRA_FLYING = CONDITION_SERIALIZERS.register("is_elytra_flying", IsElytraFlyingCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, InWaterCondition.Serializer> IN_WATER = CONDITION_SERIALIZERS.register("in_water", InWaterCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, InLavaCondition.Serializer> IN_LAVA = CONDITION_SERIALIZERS.register("in_lava", InLavaCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsUnderWaterCondition.Serializer> IS_UNDER_WATER = CONDITION_SERIALIZERS.register("is_under_water", IsUnderWaterCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsInRainCondition.Serializer> IS_IN_RAIN = CONDITION_SERIALIZERS.register("is_in_rain", IsInRainCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsInWaterOrRainCondition.Serializer> IS_IN_WATER_OR_RAIN = CONDITION_SERIALIZERS.register("is_in_water_or_rain", IsInWaterOrRainCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, DayCondition.Serializer> DAY = CONDITION_SERIALIZERS.register("day", DayCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, NightCondition.Serializer> NIGHT = CONDITION_SERIALIZERS.register("night", NightCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, HasEffectCondition.Serializer> HAS_EFFECT = CONDITION_SERIALIZERS.register("has_effect", HasEffectCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, EnergyBarCondition.Serializer> ENERGY_BAR = CONDITION_SERIALIZERS.register("energy_bar", EnergyBarCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, SlimArmorModelCondition.Serializer> SLIM_ARMOR_MODEL = CONDITION_SERIALIZERS.register("slim_armor_model", SlimArmorModelCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, WideArmorModelCondition.Serializer> WIDE_ARMOR_MODEL = CONDITION_SERIALIZERS.register("wide_armor_model", WideArmorModelCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, EntityScaleCondition.Serializer> ENTITY_SCALE = CONDITION_SERIALIZERS.register("entity_scale", EntityScaleCondition.Serializer::new);
    public static final DeferredHolder<ConditionSerializer<?>, IsUsingFlightTypeCondition.Serializer> IS_USING_FLIGHT_TYPE = CONDITION_SERIALIZERS.register("is_using_flight_type", IsUsingFlightTypeCondition.Serializer::new);

}
