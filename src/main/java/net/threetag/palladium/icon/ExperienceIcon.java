package net.threetag.palladium.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record ExperienceIcon(int amount, boolean level) implements Icon {

    public static final MapCodec<ExperienceIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    ExtraCodecs.NON_NEGATIVE_INT.fieldOf("amount").forGetter(ExperienceIcon::amount),
                    Codec.BOOL.optionalFieldOf("level", true).forGetter(ExperienceIcon::level)
            )
            .apply(instance, ExperienceIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ExperienceIcon> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ExperienceIcon::amount,
            ByteBufCodecs.BOOL, ExperienceIcon::level,
            ExperienceIcon::new
    );

    @Override
    public IconSerializer<ExperienceIcon> getSerializer() {
        return IconSerializers.EXPERIENCE.get();
    }

    @Override
    public String toString() {
        return "ExperienceIcon{" +
                "amount=" + amount +
                ", level=" + level +
                '}';
    }

    public static class Serializer extends IconSerializer<ExperienceIcon> {

        @Override
        public MapCodec<ExperienceIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExperienceIcon> streamCodec() {
            return STREAM_CODEC;
        }

    }
}
