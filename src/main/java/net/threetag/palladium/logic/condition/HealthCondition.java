package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;

public record HealthCondition(float minHealth, float maxHealth) implements Condition {

    public static final MapCodec<HealthCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.FLOAT.optionalFieldOf("min_health", Float.MIN_VALUE).forGetter(HealthCondition::minHealth),
                    Codec.FLOAT.optionalFieldOf("max_health", Float.MAX_VALUE).forGetter(HealthCondition::maxHealth)
            ).apply(instance, HealthCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HealthCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, HealthCondition::minHealth,
            ByteBufCodecs.FLOAT, HealthCondition::maxHealth,
            HealthCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        return entity.getHealth() >= this.minHealth && entity.getHealth() <= this.maxHealth;
    }

    @Override
    public ConditionSerializer<HealthCondition> getSerializer() {
        return ConditionSerializers.HEALTH.get();
    }

    public static class Serializer extends ConditionSerializer<HealthCondition> {

        @Override
        public MapCodec<HealthCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, HealthCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has a certain amount of health.";
        }
    }
}
