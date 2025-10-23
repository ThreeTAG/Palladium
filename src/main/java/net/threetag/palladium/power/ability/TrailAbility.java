package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;

import java.util.List;

public class TrailAbility extends Ability {

    // TODO

    public static final MapCodec<TrailAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ResourceLocation.CODEC.fieldOf("trail").forGetter(ab -> ab.trailRendererId),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, TrailAbility::new));

    public final ResourceLocation trailRendererId;

    public TrailAbility(ResourceLocation trailRendererId, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
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
                    .add("trail", TYPE_RESOURCE_LOCATION, "The id of the trail renderer to use.")
                    .setExampleObject(new TrailAbility(ResourceLocation.fromNamespaceAndPath("example", "trail_id"), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()));
        }
    }
}
