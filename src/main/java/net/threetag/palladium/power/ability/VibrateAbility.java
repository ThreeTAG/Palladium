package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class VibrateAbility extends Ability {

    // TODO

    public static final MapCodec<VibrateAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, VibrateAbility::new));

    public VibrateAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<VibrateAbility> getSerializer() {
        return AbilitySerializers.VIBRATE.get();
    }

    public static class Serializer extends AbilitySerializer<VibrateAbility> {

        @Override
        public MapCodec<VibrateAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, VibrateAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Adds a vibration effect to the player.")
                    .setExampleObject(new VibrateAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
