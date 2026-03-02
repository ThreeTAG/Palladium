package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class TrueCondition implements Condition {

    public static final TrueCondition INSTANCE = new TrueCondition();

    public static final MapCodec<TrueCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, TrueCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        return true;
    }

    @Override
    public ConditionSerializer<TrueCondition> getSerializer() {
        return ConditionSerializers.TRUE.get();
    }

    public static class Serializer extends ConditionSerializer<TrueCondition> {

        @Override
        public MapCodec<TrueCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, TrueCondition> builder, HolderLookup.Provider provider) {
            builder.setName("True")
                    .setDescription("It's just true. That's it.")
                    .addExampleObject(new TrueCondition());
        }
    }

}
