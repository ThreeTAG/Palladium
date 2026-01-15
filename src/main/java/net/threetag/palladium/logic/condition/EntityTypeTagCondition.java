package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public record EntityTypeTagCondition(TagKey<EntityType<?>> tag) implements Condition {

    public static final MapCodec<EntityTypeTagCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(TagKey.codec(Registries.ENTITY_TYPE).fieldOf("entity_type_tag").forGetter(EntityTypeTagCondition::tag)
            ).apply(instance, EntityTypeTagCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeTagCondition> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC.map(loc -> TagKey.create(Registries.ENTITY_TYPE, loc), TagKey::location), EntityTypeTagCondition::tag, EntityTypeTagCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.getType().is(this.tag);
    }

    @Override
    public ConditionSerializer<EntityTypeTagCondition> getSerializer() {
        return ConditionSerializers.ENTITY_TYPE_TAG.get();
    }

    public static class Serializer extends ConditionSerializer<EntityTypeTagCondition> {

        @Override
        public MapCodec<EntityTypeTagCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, EntityTypeTagCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is of a certain tag.";
        }
    }
}
