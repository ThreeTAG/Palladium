package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class FalseCondition implements Condition {

    public static final FalseCondition INSTANCE = new FalseCondition();

    public static final MapCodec<FalseCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, FalseCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        return false;
    }

    @Override
    public ConditionSerializer<FalseCondition> getSerializer() {
        return ConditionSerializers.FALSE.get();
    }

    public static class Serializer extends ConditionSerializer<FalseCondition> {

        @Override
        public MapCodec<FalseCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FalseCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, FalseCondition> builder, HolderLookup.Provider provider) {
            builder.setName("False")
                    .setDescription("It's just false. That's it.")
                    .addExampleObject(new FalseCondition());
        }
    }

}
