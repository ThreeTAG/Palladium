package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EntityType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public record EntityTypeCondition(EntityType<?> entityType) implements Condition {

    public static final MapCodec<EntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityTypeCondition::entityType)
            ).apply(instance, EntityTypeCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.registry(Registries.ENTITY_TYPE), EntityTypeCondition::entityType, EntityTypeCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.getType() == this.entityType;
    }

    @Override
    public ConditionSerializer<EntityTypeCondition> getSerializer() {
        return ConditionSerializers.ENTITY_TYPE.get();
    }

    public static class Serializer extends ConditionSerializer<EntityTypeCondition> {

        @Override
        public MapCodec<EntityTypeCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EntityTypeCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is of a specific entity type.";
        }
    }
}
