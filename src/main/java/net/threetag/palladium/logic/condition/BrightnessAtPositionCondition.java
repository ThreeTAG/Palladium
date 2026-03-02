package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public record BrightnessAtPositionCondition(int min, int max) implements Condition {

    public static final MapCodec<BrightnessAtPositionCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.intRange(0, 16).optionalFieldOf("min", 0).forGetter(BrightnessAtPositionCondition::min),
                    Codec.intRange(0, 16).optionalFieldOf("max", 16).forGetter(BrightnessAtPositionCondition::max)
            ).apply(instance, BrightnessAtPositionCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, BrightnessAtPositionCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, BrightnessAtPositionCondition::min,
            ByteBufCodecs.VAR_INT, BrightnessAtPositionCondition::max,
            BrightnessAtPositionCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var level = context.get(DataContextKeys.LEVEL);
        var pos = context.get(DataContextKeys.BLOCK_POS);

        if (level == null || pos == null) {
            return false;
        }

        var brightness = level.getMaxLocalRawBrightness(pos);
        return brightness >= this.min && brightness <= this.max;
    }

    @Override
    public ConditionSerializer<BrightnessAtPositionCondition> getSerializer() {
        return ConditionSerializers.BRIGHTNESS_AT_POSITION.get();
    }

    public static class Serializer extends ConditionSerializer<BrightnessAtPositionCondition> {

        @Override
        public MapCodec<BrightnessAtPositionCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, BrightnessAtPositionCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Brightness at Position")
                    .setDescription("Checks if the brightness at the given position is within the given range.")
                    .addOptional("min", SettingType.intRange(0, 16), "The minimum required value of the brightness value (Value between 0-16).", 0)
                    .addOptional("max", SettingType.intRange(0, 16), "The maximum required value of the brightness value.", 16)
                    .addExampleObject(new BrightnessAtPositionCondition(6, 7));
        }
    }
}
