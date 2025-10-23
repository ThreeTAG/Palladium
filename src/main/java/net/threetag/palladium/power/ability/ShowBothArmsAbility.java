package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class ShowBothArmsAbility extends Ability {

    public static final MapCodec<ShowBothArmsAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, ShowBothArmsAbility::new));

    public ShowBothArmsAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<ShowBothArmsAbility> getSerializer() {
        return AbilitySerializers.SHOW_BOTH_ARMS.get();
    }

    public static class Serializer extends AbilitySerializer<ShowBothArmsAbility> {

        @Override
        public MapCodec<ShowBothArmsAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, ShowBothArmsAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("Enables the rendering of your off-hand in first-person.")
                    .setExampleObject(new ShowBothArmsAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
