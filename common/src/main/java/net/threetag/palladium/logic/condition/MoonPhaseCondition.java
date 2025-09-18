package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;

public record MoonPhaseCondition(int min, int max) implements Condition {

    public static final MapCodec<MoonPhaseCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.intRange(0, 7).optionalFieldOf("min", 0).forGetter(MoonPhaseCondition::min),
                    Codec.intRange(0, 7).optionalFieldOf("max", 7).forGetter(MoonPhaseCondition::max)
            ).apply(instance, MoonPhaseCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, MoonPhaseCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MoonPhaseCondition::min,
            ByteBufCodecs.VAR_INT, MoonPhaseCondition::max,
            MoonPhaseCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var level = context.getLevel();

        if (level == null) {
            return false;
        }

        return level.getMoonPhase() >= this.min && level.getMoonPhase() <= this.max;
    }

    @Override
    public ConditionSerializer<MoonPhaseCondition> getSerializer() {
        return ConditionSerializers.MOON_PHASE.get();
    }

    public static class Serializer extends ConditionSerializer<MoonPhaseCondition> {

        @Override
        public MapCodec<MoonPhaseCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, MoonPhaseCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the moon phase is between the given values.";
        }
    }

}
