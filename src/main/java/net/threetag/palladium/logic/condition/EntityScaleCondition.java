package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.Entity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.EntityScaleUtil;

public record EntityScaleCondition(float min, float max) implements Condition {

    public static final MapCodec<EntityScaleCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("min", 0F).forGetter(EntityScaleCondition::min),
                    ExtraCodecs.NON_NEGATIVE_FLOAT.optionalFieldOf("max", Float.MAX_VALUE).forGetter(EntityScaleCondition::max)
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
        public void addDocumentation(CodecDocumentationBuilder<Condition, EntityScaleCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Entity Scale")
                    .setDescription("Checks the current scale of the entity.")
                    .addOptional("min", TYPE_NON_NEGATIVE_FLOAT, "The minimum required scale of the entity.")
                    .addOptional("max", TYPE_NON_NEGATIVE_INT, "The maximum required scale of the entity.")
                    .addExampleObject(new EntityScaleCondition(1.0F, 2.0F));
        }
    }
}
