package net.threetag.palladium.power.ability;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class AbilitySerializers {

    public static final DeferredRegister<AbilitySerializer<?>> ABILITIES = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ABILITY_SERIALIZER);

    public static final RegistryHolder<DummyAbility.Serializer> DUMMY = ABILITIES.register("dummy", DummyAbility.Serializer::new);
    public static final RegistryHolder<CommandAbility.Serializer> COMMAND = ABILITIES.register("command", CommandAbility.Serializer::new);
//    public static final RegistryHolder<RenderLayerByAccessorySlotAbility.Serializer> RENDER_LAYER_BY_ACCESSORY_SLOT = ABILITIES.register("render_layer_by_accessory_slot", RenderLayerByAccessorySlotAbility.Serializer::new);
    public static final RegistryHolder<ShrinkBodyOverlayAbility.Serializer> SHRINK_BODY_OVERLAY = ABILITIES.register("shrink_body_overlay", ShrinkBodyOverlayAbility.Serializer::new);
    public static final RegistryHolder<AttributeModifierAbility.Serializer> ATTRIBUTE_MODIFIER = ABILITIES.register("attribute_modifier", AttributeModifierAbility.Serializer::new);
    public static final RegistryHolder<HealingAbility.Serializer> HEALING = ABILITIES.register("healing", HealingAbility.Serializer::new);
    public static final RegistryHolder<SlowfallAbility.Serializer> SLOWFALL = ABILITIES.register("slowfall", SlowfallAbility.Serializer::new);
    public static final RegistryHolder<DamageImmunityAbility.Serializer> DAMAGE_IMMUNITY = ABILITIES.register("damage_immunity", DamageImmunityAbility.Serializer::new);
    public static final RegistryHolder<InvisibilityAbility.Serializer> INVISIBILITY = ABILITIES.register("invisibility", InvisibilityAbility.Serializer::new);
    public static final RegistryHolder<EnergyBeamAbility.Serializer> ENERGY_BEAM = ABILITIES.register("energy_beam", EnergyBeamAbility.Serializer::new);
    public static final RegistryHolder<SizeAbility.Serializer> SIZE = ABILITIES.register("size", SizeAbility.Serializer::new);
//    public static final RegistryHolder<ProjectileAbility.Serializer> PROJECTILE = ABILITIES.register("projectile", ProjectileAbility.Serializer::new);
    public static final RegistryHolder<SkinChangeAbility.Serializer> SKIN_CHANGE = ABILITIES.register("skin_change", SkinChangeAbility.Serializer::new);
    public static final RegistryHolder<AimAbility.Serializer> AIM = ABILITIES.register("aim", AimAbility.Serializer::new);
    public static final RegistryHolder<HideBodyPartAbility.Serializer> HIDE_BODY_PART = ABILITIES.register("hide_body_part", HideBodyPartAbility.Serializer::new);
    public static final RegistryHolder<RemoveBodyPartAbility.Serializer> REMOVE_BODY_PART = ABILITIES.register("remove_body_part", RemoveBodyPartAbility.Serializer::new);
    public static final RegistryHolder<ShaderEffectAbility.Serializer> SHADER_EFFECT = ABILITIES.register("shader_effect", ShaderEffectAbility.Serializer::new);
    public static final RegistryHolder<GuiOverlayAbility.Serializer> GUI_OVERLAY = ABILITIES.register("gui_overlay", GuiOverlayAbility.Serializer::new);
    public static final RegistryHolder<ShowBothArmsAbility.Serializer> SHOW_BOTH_ARMS = ABILITIES.register("show_both_arms", ShowBothArmsAbility.Serializer::new);
    //    public static final RegistryHolder<PlayerAnimationAbility.Serializer> PLAYER_ANIMATION = ABILITIES.register("player_animation", PlayerAnimationAbility::new);
    public static final RegistryHolder<FluidWalkingAbility.Serializer> FLUID_WALKING = ABILITIES.register("fluid_walking", FluidWalkingAbility.Serializer::new);
    public static final RegistryHolder<RestrictSlotsAbility.Serializer> RESTRICT_SLOTS = ABILITIES.register("restrict_slots", RestrictSlotsAbility.Serializer::new);
    public static final RegistryHolder<PlaySoundAbility.Serializer> PLAY_SOUND = ABILITIES.register("play_sound", PlaySoundAbility.Serializer::new);
    public static final RegistryHolder<VibrateAbility.Serializer> VIBRATE = ABILITIES.register("vibrate", VibrateAbility.Serializer::new);
    public static final RegistryHolder<IntangibilityAbility.Serializer> INTANGIBILITY = ABILITIES.register("intangibility", IntangibilityAbility.Serializer::new);
    public static final RegistryHolder<NameChangeAbility.Serializer> NAME_CHANGE = ABILITIES.register("name_change", NameChangeAbility.Serializer::new);
    public static final RegistryHolder<SculkImmunityAbility.Serializer> SCULK_IMMUNITY = ABILITIES.register("sculk_immunity", SculkImmunityAbility.Serializer::new);
    public static final RegistryHolder<TrailAbility.Serializer> TRAIL = ABILITIES.register("trail", TrailAbility.Serializer::new);
    public static final RegistryHolder<FireAspectAbility.Serializer> FIRE_ASPECT = ABILITIES.register("fire_aspect", FireAspectAbility.Serializer::new);
    public static final RegistryHolder<ParticleAbility.Serializer> PARTICLES = ABILITIES.register("particles", ParticleAbility.Serializer::new);
    public static final RegistryHolder<AnimationAbility.Serializer> ANIMATION = ABILITIES.register("animation", AnimationAbility.Serializer::new);

    public static void init() {

    }

}
