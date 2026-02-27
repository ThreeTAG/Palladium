package net.threetag.palladium.power.ability;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class AbilitySerializers {

    public static final DeferredRegister<AbilitySerializer<?>> ABILITIES = DeferredRegister.create(PalladiumRegistryKeys.ABILITY_SERIALIZER, Palladium.MOD_ID);

    public static final DeferredHolder<AbilitySerializer<?>, DummyAbility.Serializer> DUMMY = ABILITIES.register("dummy", DummyAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, CommandAbility.Serializer> COMMAND = ABILITIES.register("command", CommandAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, ShrinkPlayerOverlayAbility.Serializer> SHRINK_PLAYER_OVERLAY = ABILITIES.register("shrink_player_overlay", ShrinkPlayerOverlayAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, AttributeModifierAbility.Serializer> ATTRIBUTE_MODIFIER = ABILITIES.register("attribute_modifier", AttributeModifierAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, HealingAbility.Serializer> HEALING = ABILITIES.register("healing", HealingAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, SlowfallAbility.Serializer> SLOWFALL = ABILITIES.register("slowfall", SlowfallAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, DamageImmunityAbility.Serializer> DAMAGE_IMMUNITY = ABILITIES.register("damage_immunity", DamageImmunityAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, InvisibilityAbility.Serializer> INVISIBILITY = ABILITIES.register("invisibility", InvisibilityAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, BeamAbility.Serializer> BEAM = ABILITIES.register("beam", BeamAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, SizeAbility.Serializer> SIZE = ABILITIES.register("size", SizeAbility.Serializer::new);
//    public static final DeferredHolder<AbilitySerializer<?>, ProjectileAbility.Serializer> PROJECTILE = ABILITIES.register("projectile", ProjectileAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, SkinChangeAbility.Serializer> SKIN_CHANGE = ABILITIES.register("skin_change", SkinChangeAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, AimAbility.Serializer> AIM = ABILITIES.register("aim", AimAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, HideModelPartAbility.Serializer> HIDE_MODEL_PART = ABILITIES.register("hide_model_part", HideModelPartAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, ShaderEffectAbility.Serializer> SHADER_EFFECT = ABILITIES.register("shader_effect", ShaderEffectAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, GuiOverlayAbility.Serializer> GUI_OVERLAY = ABILITIES.register("gui_overlay", GuiOverlayAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, ShowBothArmsAbility.Serializer> SHOW_BOTH_ARMS = ABILITIES.register("show_both_arms", ShowBothArmsAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, FluidWalkingAbility.Serializer> FLUID_WALKING = ABILITIES.register("fluid_walking", FluidWalkingAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, RestrictSlotsAbility.Serializer> RESTRICT_SLOTS = ABILITIES.register("restrict_slots", RestrictSlotsAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, PlaySoundAbility.Serializer> PLAY_SOUND = ABILITIES.register("play_sound", PlaySoundAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, IntangibilityAbility.Serializer> INTANGIBILITY = ABILITIES.register("intangibility", IntangibilityAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, NameChangeAbility.Serializer> NAME_CHANGE = ABILITIES.register("name_change", NameChangeAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, SculkImmunityAbility.Serializer> SCULK_IMMUNITY = ABILITIES.register("sculk_immunity", SculkImmunityAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, TrailAbility.Serializer> TRAIL = ABILITIES.register("trail", TrailAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, FireAspectAbility.Serializer> FIRE_ASPECT = ABILITIES.register("fire_aspect", FireAspectAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, ParticleAbility.Serializer> PARTICLES = ABILITIES.register("particles", ParticleAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, FlightAbility.Serializer> FLIGHT = ABILITIES.register("flight", FlightAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, GlidingAbility.Serializer> GLIDING = ABILITIES.register("gliding", GlidingAbility.Serializer::new);
    public static final DeferredHolder<AbilitySerializer<?>, WallClimbingAbility.Serializer> WALL_CLIMBING = ABILITIES.register("wall_climbing", WallClimbingAbility.Serializer::new);

}
