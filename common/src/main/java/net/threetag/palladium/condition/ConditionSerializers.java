package net.threetag.palladium.condition;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class ConditionSerializers {

    public static final DeferredRegister<ConditionSerializer<?>> CONDITION_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.CONDITION_SERIALIZER);

    // TODO
    public static final RegistryHolder<TrueCondition.Serializer> TRUE = CONDITION_SERIALIZERS.register("true", TrueCondition.Serializer::new);
    public static final RegistryHolder<FalseCondition.Serializer> FALSE = CONDITION_SERIALIZERS.register("false", FalseCondition.Serializer::new);
    public static final RegistryHolder<NotCondition.Serializer> NOT = CONDITION_SERIALIZERS.register("not", NotCondition.Serializer::new);
    public static final RegistryHolder<OrCondition.Serializer> OR = CONDITION_SERIALIZERS.register("or", OrCondition.Serializer::new);
    public static final RegistryHolder<AndCondition.Serializer> AND = CONDITION_SERIALIZERS.register("and", AndCondition.Serializer::new);
    public static final RegistryHolder<HasPowerCondition.Serializer> HAS_POWER = CONDITION_SERIALIZERS.register("has_power", HasPowerCondition.Serializer::new);
//    public static final RegistryHolder<WearsSuitSetCondition.Serializer> WEARS_SUIT_SET = CONDITION_SERIALIZERS.register("wears_suit_set", WearsSuitSetCondition.Serializer::new);
    public static final RegistryHolder<AbilityUnlockedCondition.Serializer> ABILITY_UNLOCKED = CONDITION_SERIALIZERS.register("ability_unlocked", AbilityUnlockedCondition.Serializer::new);
    public static final RegistryHolder<AbilityTypeUnlockedCondition.Serializer> ABILITY_TYPE_UNLOCKED = CONDITION_SERIALIZERS.register("ability_type_unlocked", AbilityTypeUnlockedCondition.Serializer::new);
    public static final RegistryHolder<AbilityEnabledCondition.Serializer> ABILITY_ENABLED = CONDITION_SERIALIZERS.register("ability_enabled", AbilityEnabledCondition.Serializer::new);
    public static final RegistryHolder<AbilityTypeEnabledCondition.Serializer> ABILITY_TYPE_ENABLED = CONDITION_SERIALIZERS.register("ability_type_enabled", AbilityTypeEnabledCondition.Serializer::new);
    public static final RegistryHolder<AbilityOnCooldownCondition.Serializer> ABILITY_ON_COOLDOWN = CONDITION_SERIALIZERS.register("ability_on_cooldown", AbilityOnCooldownCondition.Serializer::new);
    public static final RegistryHolder<HealthCondition.Serializer> HEALTH = CONDITION_SERIALIZERS.register("health", HealthCondition.Serializer::new);
    public static final RegistryHolder<ObjectiveScoreCondition.Serializer> OBJECTIVE_SCORE = CONDITION_SERIALIZERS.register("objective_score", ObjectiveScoreCondition.Serializer::new);
    public static final RegistryHolder<HasTagCondition.Serializer> HAS_TAG = CONDITION_SERIALIZERS.register("has_tag", HasTagCondition.Serializer::new);
    public static final RegistryHolder<AnimationTimerAbilityCondition.Serializer> ANIMATION_TIMER_ABILITY = CONDITION_SERIALIZERS.register("animation_timer_ability", AnimationTimerAbilityCondition.Serializer::new);
    public static final RegistryHolder<AbilityTicksCondition.Serializer> ABILITY_TICKS = CONDITION_SERIALIZERS.register("ability_ticks", AbilityTicksCondition.Serializer::new);
    public static final RegistryHolder<AbilityFirstTickCondition.Serializer> ABILITY_FIRST_TICK = CONDITION_SERIALIZERS.register("ability_first_tick", AbilityFirstTickCondition.Serializer::new);
    public static final RegistryHolder<AbilityLastTickCondition.Serializer> ABILITY_LAST_TICK = CONDITION_SERIALIZERS.register("ability_last_tick", AbilityLastTickCondition.Serializer::new);
    public static final RegistryHolder<CrouchingCondition.Serializer> CROUCHING = CONDITION_SERIALIZERS.register("crouching", CrouchingCondition.Serializer::new);
    public static final RegistryHolder<PoseCondition.Serializer> POSE = CONDITION_SERIALIZERS.register("pose", PoseCondition.Serializer::new);
    public static final RegistryHolder<MoonPhaseCondition.Serializer> MOON_PHASE = CONDITION_SERIALIZERS.register("moon_phase", MoonPhaseCondition.Serializer::new);
    public static final RegistryHolder<ModLoadedCondition.Serializer> MOD_LOADED = CONDITION_SERIALIZERS.register("mod_loaded", ModLoadedCondition.Serializer::new);
    public static final RegistryHolder<ItemInSlotCondition.Serializer> ITEM_IN_SLOT = CONDITION_SERIALIZERS.register("item_in_slot", ItemInSlotCondition.Serializer::new);
//    public static final RegistryHolder<ItemInSlotOpenCondition.Serializer> ITEM_IN_SLOT_OPEN = CONDITION_SERIALIZERS.register("item_in_slot_open", ItemInSlotOpenCondition.Serializer::new);
    public static final RegistryHolder<EmptySlotCondition.Serializer> EMPTY_SLOT = CONDITION_SERIALIZERS.register("empty_slot", EmptySlotCondition.Serializer::new);
    public static final RegistryHolder<EntityTypeCondition.Serializer> ENTITY_TYPE = CONDITION_SERIALIZERS.register("entity_type", EntityTypeCondition.Serializer::new);
    public static final RegistryHolder<EntityTypeTagCondition.Serializer> ENTITY_TYPE_TAG = CONDITION_SERIALIZERS.register("entity_type_tag", EntityTypeTagCondition.Serializer::new);
    public static final RegistryHolder<BrightnessAtPositionCondition.Serializer> BRIGHTNESS_AT_POSITION = CONDITION_SERIALIZERS.register("brightness_at_position", BrightnessAtPositionCondition.Serializer::new);
    public static final RegistryHolder<OnGroundCondition.Serializer> ON_GROUND = CONDITION_SERIALIZERS.register("on_ground", OnGroundCondition.Serializer::new);
    public static final RegistryHolder<IsMovingCondition.Serializer> IS_MOVING = CONDITION_SERIALIZERS.register("is_moving", IsMovingCondition.Serializer::new);
    public static final RegistryHolder<HasMovementInputCondition.Serializer> HAS_MOVEMENT_INPUT = CONDITION_SERIALIZERS.register("has_movement_input", HasMovementInputCondition.Serializer::new);
    public static final RegistryHolder<CommandResultCondition.Serializer> COMMAND_RESULT = CONDITION_SERIALIZERS.register("command_result", CommandResultCondition.Serializer::new);
    public static final RegistryHolder<IntervalCondition.Serializer> INTERVAL = CONDITION_SERIALIZERS.register("interval", IntervalCondition.Serializer::new);
    public static final RegistryHolder<SprintingCondition.Serializer> SPRINTING = CONDITION_SERIALIZERS.register("sprinting", SprintingCondition.Serializer::new);
    public static final RegistryHolder<DimensionCondition.Serializer> DIMENSION = CONDITION_SERIALIZERS.register("dimension", DimensionCondition.Serializer::new);
    public static final RegistryHolder<IsOnFireCondition.Serializer> IS_ON_FIRE = CONDITION_SERIALIZERS.register("is_on_fire", IsOnFireCondition.Serializer::new);
    public static final RegistryHolder<IsSwimmingCondition.Serializer> IS_SWIMMING = CONDITION_SERIALIZERS.register("is_swimming", IsSwimmingCondition.Serializer::new);
    public static final RegistryHolder<IsElytraFlyingCondition.Serializer> IS_ELYTRA_FLYING = CONDITION_SERIALIZERS.register("is_elytra_flying", IsElytraFlyingCondition.Serializer::new);
//    public static final RegistryHolder<IsHoveringCondition.Serializer> IS_HOVERING = CONDITION_SERIALIZERS.register("is_hovering", IsHoveringCondition.Serializer::new);
//    public static final RegistryHolder<IsFlyingCondition.Serializer> IS_FLYING = CONDITION_SERIALIZERS.register("is_flying", IsFlyingCondition.Serializer::new);
//    public static final RegistryHolder<IsLevitatingCondition.Serializer> IS_LEVITATING = CONDITION_SERIALIZERS.register("is_levitating", IsLevitatingCondition.Serializer::new);
//    public static final RegistryHolder<IsFastFlyingCondition.Serializer> IS_FAST_FLYING = CONDITION_SERIALIZERS.register("is_fast_flying", IsFastFlyingCondition.Serializer::new);
//    public static final RegistryHolder<IsHoveringOrFlyingCondition.Serializer> IS_HOVERING_OR_FLYING = CONDITION_SERIALIZERS.register("is_hovering_or_flying", IsHoveringOrFlyingCondition.Serializer::new);
//    public static final RegistryHolder<IsHoveringOrLevitatingCondition.Serializer> IS_HOVERING_OR_LEVITATING = CONDITION_SERIALIZERS.register("is_hovering_or_levitating", IsHoveringOrLevitatingCondition.Serializer::new);
    public static final RegistryHolder<InWaterCondition.Serializer> IN_WATER = CONDITION_SERIALIZERS.register("in_water", InWaterCondition.Serializer::new);
    public static final RegistryHolder<InLavaCondition.Serializer> IN_LAVA = CONDITION_SERIALIZERS.register("in_lava", InLavaCondition.Serializer::new);
    public static final RegistryHolder<IsUnderWaterCondition.Serializer> IS_UNDER_WATER = CONDITION_SERIALIZERS.register("is_under_water", IsUnderWaterCondition.Serializer::new);
    public static final RegistryHolder<IsInRainCondition.Serializer> IS_IN_RAIN = CONDITION_SERIALIZERS.register("is_in_rain", IsInRainCondition.Serializer::new);
    public static final RegistryHolder<IsInWaterOrRainCondition.Serializer> IS_IN_WATER_OR_RAIN = CONDITION_SERIALIZERS.register("is_in_water_or_rain", IsInWaterOrRainCondition.Serializer::new);
    public static final RegistryHolder<IsInWaterRainOrBubbleCondition.Serializer> IS_IN_WATER_RAIN_OR_BUBBLE = CONDITION_SERIALIZERS.register("is_in_water_rain_or_bubble", IsInWaterRainOrBubbleCondition.Serializer::new);
    public static final RegistryHolder<IsInWaterOrBubbleCondition.Serializer> IS_IN_WATER_OR_BUBBLE = CONDITION_SERIALIZERS.register("is_in_water_or_bubble", IsInWaterOrBubbleCondition.Serializer::new);
    public static final RegistryHolder<DayCondition.Serializer> DAY = CONDITION_SERIALIZERS.register("day", DayCondition.Serializer::new);
    public static final RegistryHolder<NightCondition.Serializer> NIGHT = CONDITION_SERIALIZERS.register("night", NightCondition.Serializer::new);
//    public static final RegistryHolder<InAccessorySlotMenuCondition.Serializer> IN_ACCESSORY_SLOT_MENU = CONDITION_SERIALIZERS.register("in_accessory_slot_menu", InAccessorySlotMenuCondition.Serializer::new);
    public static final RegistryHolder<HasEffectCondition.Serializer> HAS_EFFECT = CONDITION_SERIALIZERS.register("has_effect", HasEffectCondition.Serializer::new);
    public static final RegistryHolder<EnergyBarCondition.Serializer> ENERGY_BAR = CONDITION_SERIALIZERS.register("energy_bar", EnergyBarCondition.Serializer::new);
//    public static final RegistryHolder<AccessorySelectedCondition.Serializer> ACCESSORY_SELECTED = CONDITION_SERIALIZERS.register("accessory_selected", AccessorySelectedCondition.Serializer::new);
    public static final RegistryHolder<SmallArmsCondition.Serializer> SMALL_ARMS = CONDITION_SERIALIZERS.register("small_arms", SmallArmsCondition.Serializer::new);
    public static final RegistryHolder<EntityScaleCondition.Serializer> ENTITY_SCALE = CONDITION_SERIALIZERS.register("entity_scale", EntityScaleCondition.Serializer::new);

}
