package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class InvisibilityAbility extends Ability {

    public static final MapCodec<InvisibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("mob_visibility_modifier", 0.0F).forGetter(a -> a.mobVisibilityModifier),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("opacity", 0.0F).forGetter(a -> a.opacity),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, InvisibilityAbility::new));

    public final float opacity;
    public final float mobVisibilityModifier;

    public InvisibilityAbility(float opacity, float mobVisibilityModifier, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.opacity = opacity;
        this.mobVisibilityModifier = mobVisibilityModifier;
    }

    @Override
    public AbilitySerializer<InvisibilityAbility> getSerializer() {
        return AbilitySerializers.INVISIBILITY.get();
    }

    public static float getVisibilityMultiplier(LivingEntity entity, float partialTick) {
        float f = 1F;

        for (AbilityInstance<InvisibilityAbility> ability : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.INVISIBILITY.get())) {
            f = Math.min(f, Mth.lerp(ability.getAnimationTimerProgressEased(partialTick), 1F, ability.getAbility().mobVisibilityModifier));
        }

        return f;
    }

    public static float getOpacity(LivingEntity entity, float partialTick) {
        float f = 1F;

        for (AbilityInstance<InvisibilityAbility> ability : AbilityUtil.getEnabledInstances(entity, AbilitySerializers.INVISIBILITY.get())) {
            f = Math.min(f, Mth.lerp(ability.getAnimationTimerProgressEased(partialTick), 1F, ability.getAbility().opacity));
        }

        return f;
    }

    public static class Serializer extends AbilitySerializer<InvisibilityAbility> {

        @Override
        public MapCodec<InvisibilityAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, InvisibilityAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Makes the player invisible. Also makes mobs not see the player anymore.")
                    .addOptional("opacity", TYPE_FLOAT, "The opacity the player will adapt", 0)
                    .addOptional("mob_visibility_modifier", TYPE_FLOAT, "A multiplier for how visible the player is to mobs. 0.0 means completely invisible, 1.0 means normal visibility.", 0)
                    .addExampleObject(new InvisibilityAbility(0, 0, AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()))
                    .addExampleObject(new InvisibilityAbility(0.5F, 0.5F, AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
