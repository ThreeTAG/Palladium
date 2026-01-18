package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public record HealthCondition(float minHealth, float maxHealth) implements Condition {

    public static final MapCodec<HealthCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("min", Float.MIN_VALUE).forGetter(HealthCondition::minHealth),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("max", Float.MAX_VALUE).forGetter(HealthCondition::maxHealth)
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
        public void addDocumentation(CodecDocumentationBuilder<Condition, HealthCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Health")
                    .setDescription("Checks if the entity has a certain amount of health.")
                    .addOptional("min", TYPE_NON_NEGATIVE_FLOAT, "The minimum required health of the entity.")
                    .addOptional("max", TYPE_NON_NEGATIVE_FLOAT, "The maximum required health of the entity.")
                    .addExampleObject(new HealthCondition(5, 10));
        }
    }
}
