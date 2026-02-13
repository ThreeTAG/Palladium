package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class ShrinkPlayerOverlayAbility extends Ability {

    public static final MapCodec<ShrinkPlayerOverlayAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, ShrinkPlayerOverlayAbility::new));

    public ShrinkPlayerOverlayAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<ShrinkPlayerOverlayAbility> getSerializer() {
        return AbilitySerializers.SHRINK_PLAYER_OVERLAY.get();
    }

    public static class Serializer extends AbilitySerializer<ShrinkPlayerOverlayAbility> {

        @Override
        public MapCodec<ShrinkPlayerOverlayAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, ShrinkPlayerOverlayAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("An ability that shrinks the body overlay of the entity.")
                    .addExampleObject(new ShrinkPlayerOverlayAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
