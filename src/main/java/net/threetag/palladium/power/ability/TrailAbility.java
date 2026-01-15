package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class TrailAbility extends Ability {

    public static final MapCodec<TrailAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Identifier.CODEC.fieldOf("trail").forGetter(ab -> ab.trailRendererId),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, TrailAbility::new));

    public final Identifier trailRendererId;

    public TrailAbility(Identifier trailRendererId, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.trailRendererId = trailRendererId;
    }

    @Override
    public AbilitySerializer<TrailAbility> getSerializer() {
        return AbilitySerializers.TRAIL.get();
    }

    public static class Serializer extends AbilitySerializer<TrailAbility> {

        @Override
        public MapCodec<TrailAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, TrailAbility> builder, HolderLookup.Provider provider) {
            builder.setDescription("An ability that renders a trail behind the entity.")
                    .add("trail", TYPE_IDENTIFIER, "The id of the trail renderer to use. Trail files are located in \"assets/namespace/palladium/trails\".")
                    .setExampleObject(new TrailAbility(Identifier.fromNamespaceAndPath("example", "trail_id"), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
