package net.threetag.palladium.icon;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

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

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Icon, ExperienceIcon> builder, HolderLookup.Provider provider) {
            builder.setName("Experience").setDescription("Displays an amount of experience points of levels")
                    .add("amount", TYPE_NON_NEGATIVE_INT, "Amount of xp points or levels")
                    .addOptional("level", TYPE_BOOLEAN, "Whether or not this will be displayed as xp points or levels", true);
        }
    }
}
