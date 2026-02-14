package net.threetag.palladium.power.ability;

import com.mojang.serialization.Codec;
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

public class InvisibilityAbility extends Ability implements OpacityChanging<InvisibilityAbility>{

    public static final MapCodec<InvisibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("opacity", 0.0F).forGetter(a -> a.opacity),
                    Codec.FLOAT.optionalFieldOf("opacity_self", 0.2F).forGetter(a -> a.opacitySelf),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("mob_visibility_modifier", 0F).forGetter(a -> a.mobVisibilityModifier),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, InvisibilityAbility::new));

    public final float opacity;
    public final float opacitySelf;
    public final float mobVisibilityModifier;

    public InvisibilityAbility(float opacity, float opacitySelf, float mobVisibilityModifier, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.opacity = opacity;
        this.opacitySelf = opacitySelf;
        this.mobVisibilityModifier = mobVisibilityModifier;
    }

    @Override
    public AbilitySerializer<InvisibilityAbility> getSerializer() {
        return AbilitySerializers.INVISIBILITY.get();
    }

    @Override
    public float getOpacity(LivingEntity entity, AbilityInstance<InvisibilityAbility> instance, boolean isLocalPlayer, float partialTick) {
        return Mth.lerp(instance.getAnimationTimerProgressEased(partialTick), 1F, isLocalPlayer && this.opacitySelf >= 0F ? this.opacitySelf : this.opacity);
    }

    @Override
    public float getMobVisibilityMultiplier(LivingEntity entity, AbilityInstance<InvisibilityAbility> instance, float partialTick) {
        return Mth.lerp(instance.getAnimationTimerProgressEased(partialTick), 1F, this.mobVisibilityModifier);
    }

    public static class Serializer extends AbilitySerializer<InvisibilityAbility> {

        @Override
        public MapCodec<InvisibilityAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, InvisibilityAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Makes the player invisible. Also makes mobs not see the player anymore.")
                    .addOptional("opacity", TYPE_NON_NEGATIVE_FLOAT, "The opacity the player will adapt", 0F)
                    .addOptional("opacity_self", TYPE_FLOAT, "The opacity at which the player will see themself. If this is being set to something less than 0 it will use the default opacity setting.", 0.2F)
                    .addOptional("mob_visibility_modifier", TYPE_NON_NEGATIVE_FLOAT, "A multiplier for how visible the player is to mobs. 0.0 means completely invisible, 1.0 means normal visibility.", 0F)
                    .addExampleObject(new InvisibilityAbility(0, 0.2F, 0, AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()))
                    .addExampleObject(new InvisibilityAbility(0.1F, 0.5F, 0.5F, AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
