package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class InvisibilityAbility extends Ability {

    // TODO add first person support

    public static final MapCodec<InvisibilityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("mob_visibility_modifier", 0.0F).forGetter(a -> a.mobVisibilityModifier),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, InvisibilityAbility::new));

    public final float mobVisibilityModifier;

    public InvisibilityAbility(float mobVisibilityModifier, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.mobVisibilityModifier = mobVisibilityModifier;
    }

    @Override
    public AbilitySerializer<InvisibilityAbility> getSerializer() {
        return AbilitySerializers.INVISIBILITY.get();
    }

    public static class Serializer extends AbilitySerializer<InvisibilityAbility> {

        @Override
        public MapCodec<InvisibilityAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, InvisibilityAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Makes the player invisible. Also makes mobs not see the player anymore.")
                    .addOptional("mob_visibility_modifier", TYPE_FLOAT, "A multiplier for how visible the player is to mobs. 0.0 means completely invisible, 1.0 means normal visibility.", 0)
                    .setExampleObject(new InvisibilityAbility(0.5F, AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
