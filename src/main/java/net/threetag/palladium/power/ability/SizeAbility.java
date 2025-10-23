package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.Collections;
import java.util.List;

public class SizeAbility extends Ability {

    // TODO

    public static final MapCodec<SizeAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("scale").forGetter(ab -> ab.size),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, SizeAbility::new));

    public final float size;

    public SizeAbility(float size, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.size = size;
    }

    @Override
    public AbilitySerializer<SizeAbility> getSerializer() {
        return AbilitySerializers.SIZE.get();
    }

    public static class Serializer extends AbilitySerializer<SizeAbility> {

        @Override
        public MapCodec<SizeAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, SizeAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("An ability that changes the size of the entity using Pehkui.")
                    .add("scale", TYPE_FLOAT, "The target scale of the entity.")
                    .setExampleObject(new SizeAbility(1.5F, AbilityProperties.BASIC, AbilityStateManager.EMPTY, Collections.emptyList()));
        }
    }
}
