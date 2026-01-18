package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EntityType;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;
import net.threetag.palladium.util.MixedHolderSet;

import java.util.List;

public record EntityTypeCondition(MixedHolderSet<EntityType<?>> entityType) implements Condition {

    public static final MapCodec<EntityTypeCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                    MixedHolderSet.codec(Registries.ENTITY_TYPE).fieldOf("entity_type").forGetter(EntityTypeCondition::entityType)
            ).apply(instance, EntityTypeCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeCondition> STREAM_CODEC = StreamCodec.composite(
            MixedHolderSet.streamCodec(Registries.ENTITY_TYPE), EntityTypeCondition::entityType, EntityTypeCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        var holder = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType());
        return this.entityType.contains(holder);
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
        public void addDocumentation(CodecDocumentationBuilder<Condition, EntityTypeCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Entity Type")
                    .setDescription("Checks if the entity is of a specific entity type")
                    .add("entity_type", TYPE_ENTITY_TYPE_HOLDER_SET, "IDs or tags of the required entity type.")
                    .addExampleObject(new EntityTypeCondition(new MixedHolderSet<>(HolderSet.direct(provider.holderOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("sheep")))))))
                    .addExampleObject(new EntityTypeCondition(new MixedHolderSet<>(List.of(
                            provider.lookupOrThrow(Registries.ENTITY_TYPE).getOrThrow(EntityTypeTags.BURN_IN_DAYLIGHT),
                            HolderSet.direct(provider.holderOrThrow(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace("sheep"))))
                    ))));
        }
    }
}
