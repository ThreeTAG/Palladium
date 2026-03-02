package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public record DimensionCondition(ResourceKey<Level> dimension) implements Condition {

    public static final MapCodec<DimensionCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(DimensionCondition::dimension)
            ).apply(instance, DimensionCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, DimensionCondition> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DIMENSION), DimensionCondition::dimension, DimensionCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var level = context.get(DataContextKeys.LEVEL);
        return level != null && level.dimension().equals(this.dimension);
    }

    @Override
    public ConditionSerializer<DimensionCondition> getSerializer() {
        return ConditionSerializers.DIMENSION.get();
    }

    public static class Serializer extends ConditionSerializer<DimensionCondition> {

        @Override
        public MapCodec<DimensionCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, DimensionCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Dimension")
                    .setDescription("Tests for the current dimension.")
                    .add("dimension", TYPE_DIMENSION, "ID of the required dimension.")
                    .addExampleObject(new DimensionCondition(Level.OVERWORLD));
        }
    }
}
