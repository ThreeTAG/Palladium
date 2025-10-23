package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class ShrinkBodyOverlayAbility extends Ability {

    // TODO

    public static final MapCodec<ShrinkBodyOverlayAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, ShrinkBodyOverlayAbility::new));

    public ShrinkBodyOverlayAbility(AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
    }

    @Override
    public AbilitySerializer<ShrinkBodyOverlayAbility> getSerializer() {
        return AbilitySerializers.SHRINK_BODY_OVERLAY.get();
    }

    public static class Serializer extends AbilitySerializer<ShrinkBodyOverlayAbility> {

        @Override
        public MapCodec<ShrinkBodyOverlayAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, ShrinkBodyOverlayAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("An ability that shrinks the body overlay of the entity.")
                    .setExampleObject(new ShrinkBodyOverlayAbility(AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
