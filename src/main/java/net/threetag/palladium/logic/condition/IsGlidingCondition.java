package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class IsGlidingCondition implements Condition {

    public static final IsGlidingCondition INSTANCE = new IsGlidingCondition();

    public static final MapCodec<IsGlidingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsGlidingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        return entity.isFallFlying();
    }

    @Override
    public ConditionSerializer<IsGlidingCondition> getSerializer() {
        return ConditionSerializers.IS_GLIDING.get();
    }

    public static class Serializer extends ConditionSerializer<IsGlidingCondition> {

        @Override
        public MapCodec<IsGlidingCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsGlidingCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsGlidingCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is Elytra Flying")
                    .setDescription("Checks if the entity is currently flying with an elytra.")
                    .addExampleObject(new IsGlidingCondition());
        }
    }
}
