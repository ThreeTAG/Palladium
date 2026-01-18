package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class SculkImmunityAbility extends Ability {

    public static final MapCodec<SculkImmunityAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, SculkImmunityAbility::new));

    public SculkImmunityAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<SculkImmunityAbility> getSerializer() {
        return AbilitySerializers.SCULK_IMMUNITY.get();
    }

    public static class Serializer extends AbilitySerializer<SculkImmunityAbility> {

        @Override
        public MapCodec<SculkImmunityAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, SculkImmunityAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("When enabled, the player will not cause any walk-related sculk vibrations anymore.")
                    .addExampleObject(new SculkImmunityAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
