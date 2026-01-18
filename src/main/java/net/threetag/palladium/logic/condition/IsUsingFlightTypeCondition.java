package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.threetag.palladium.entity.flight.EntityFlightHandler;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class IsUsingFlightTypeCondition implements Condition {

    public static final MapCodec<IsUsingFlightTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            RegistryFixedCodec.create(PalladiumRegistryKeys.FLIGHT_TYPE).fieldOf("flight_type").forGetter(c -> c.flightType)
    ).apply(instance, IsUsingFlightTypeCondition::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, IsUsingFlightTypeCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(PalladiumRegistryKeys.FLIGHT_TYPE), c -> c.flightType,
            IsUsingFlightTypeCondition::new
    );

    private final Holder<FlightType> flightType;

    public IsUsingFlightTypeCondition(Holder<FlightType> flightType) {
        this.flightType = flightType;
    }

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity != null) {
            var handler = EntityFlightHandler.get(entity);
            return handler.isFlying() && handler.getFlightType() == this.flightType.value();
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
        public String getDocumentationDescription() {
            return "Checks if the given entity is currently flying using the specified flight type.";
        }
    }
}
