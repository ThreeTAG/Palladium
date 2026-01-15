package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        var brightness = entity.level().getMaxLocalRawBrightness(entity.blockPosition());
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
        public StreamCodec<RegistryFriendlyByteBuf, BrightnessAtPositionCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity's brightness at it's position is within the given range.";
        }
    }
}
