package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.EntityUtil;

public class IsMovingCondition implements Condition {

    public static final IsMovingCondition INSTANCE = new IsMovingCondition();

    public static final MapCodec<IsMovingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsMovingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        return EntityUtil.isMoving(context.getEntity());
    }

    @Override
    public ConditionSerializer<IsMovingCondition> getSerializer() {
        return ConditionSerializers.IS_MOVING.get();
    }

    public static class Serializer extends ConditionSerializer<IsMovingCondition> {

        @Override
        public MapCodec<IsMovingCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsMovingCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsMovingCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is moving")
                    .setDescription("Checks if the entity is currently moving.")
                    .addExampleObject(new IsMovingCondition());
        }
    }
}
