package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.util.EntityScaleUtil;
import net.threetag.palladium.logic.context.DataContext;

public record EntityScaleCondition(float min, float max) implements Condition {

    public static final MapCodec<EntityScaleCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("min", 0F).forGetter(EntityScaleCondition::min),
                    Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("max", Float.MAX_VALUE).forGetter(EntityScaleCondition::max)
            ).apply(instance, EntityScaleCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityScaleCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, EntityScaleCondition::min,
            ByteBufCodecs.FLOAT, EntityScaleCondition::max,
            EntityScaleCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        Entity entity = context.getEntity();

        if (entity == null) {
            return false;
        }

        var averageScale = (EntityScaleUtil.getInstance().getWidthScale(entity) + EntityScaleUtil.getInstance().getHeightScale(entity)) / 2F;
        return averageScale >= this.min && averageScale <= this.max;
    }

    @Override
    public ConditionSerializer<EntityScaleCondition> getSerializer() {
        return ConditionSerializers.ENTITY_SCALE.get();
    }

    public static class Serializer extends ConditionSerializer<EntityScaleCondition> {

        @Override
        public MapCodec<EntityScaleCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EntityScaleCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if an entity is within a certain scale (requires Pehkui for real effect). It checks for the \"average\" scale, which is the average of the width and height scale. Usually they are the same.";
        }
    }
}
