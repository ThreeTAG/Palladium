package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class HasMovementInputCondition implements Condition {

    public static final HasMovementInputCondition INSTANCE = new HasMovementInputCondition();

    public static final MapCodec<HasMovementInputCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, HasMovementInputCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.getEntity();
        return entity != null && Palladium.PROXY.hasMovementInput(entity);
    }

    @Override
    public ConditionSerializer<HasMovementInputCondition> getSerializer() {
        return ConditionSerializers.HAS_MOVEMENT_INPUT.get();
    }

    public static class Serializer extends ConditionSerializer<HasMovementInputCondition> {

        @Override
        public MapCodec<HasMovementInputCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, HasMovementInputCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Has Movement Input")
                    .setDescription("Checks if a player is currently pressing keys to move.")
                    .addExampleObject(new HasMovementInputCondition());
        }
    }
}
