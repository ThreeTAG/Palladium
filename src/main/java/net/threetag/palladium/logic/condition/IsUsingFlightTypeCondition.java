package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.entity.flight.PalladiumFlightTypes;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import net.threetag.palladium.util.MixedHolderSet;

import java.util.List;

public class IsUsingFlightTypeCondition implements Condition {

    public static final MapCodec<IsUsingFlightTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            MixedHolderSet.codec(PalladiumRegistryKeys.FLIGHT_TYPE).fieldOf("flight_type").forGetter(c -> c.flightType)
    ).apply(instance, IsUsingFlightTypeCondition::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, IsUsingFlightTypeCondition> STREAM_CODEC = StreamCodec.composite(
            MixedHolderSet.streamCodec(PalladiumRegistryKeys.FLIGHT_TYPE), c -> c.flightType,
            IsUsingFlightTypeCondition::new
    );

    private final MixedHolderSet<FlightType> flightType;

    public IsUsingFlightTypeCondition(MixedHolderSet<FlightType> flightType) {
        this.flightType = flightType;
    }

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity != null) {
            var handler = EntityFlightHandler.get(entity);

            if (handler.isFlying() && handler.getFlightType() != null) {
                var holder = entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.FLIGHT_TYPE).wrapAsHolder(handler.getFlightType());
                return this.flightType.contains(holder);
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer<?> getSerializer() {
        return ConditionSerializers.IS_USING_FLIGHT_TYPE.get();
    }

    public static class Serializer extends ConditionSerializer<IsUsingFlightTypeCondition> {

        @Override
        public MapCodec<IsUsingFlightTypeCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsUsingFlightTypeCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsUsingFlightTypeCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is using Flight Type")
                    .setDescription("Checks if the given entity is currently flying using the specified flight type.")
                    .add("flight_type", TYPE_FLIGHT_TYPE_HOLDER_SET, "ID(s) or tag(s) of the required flight type.")
                    .addExampleObject(new IsUsingFlightTypeCondition(new MixedHolderSet<>(HolderSet.direct(provider.holderOrThrow(PalladiumFlightTypes.PROPULSION)))))
                    .addExampleObject(new IsUsingFlightTypeCondition(new MixedHolderSet<>(List.of(
                            HolderSet.direct(provider.holderOrThrow(PalladiumFlightTypes.PROPULSION)),
                            HolderSet.direct(provider.holderOrThrow(PalladiumFlightTypes.WEB_SWINGING))
                    ))));

        }
    }
}
