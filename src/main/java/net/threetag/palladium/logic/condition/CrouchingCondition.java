package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public class CrouchingCondition implements Condition {

    public static final CrouchingCondition INSTANCE = new CrouchingCondition();

    public static final MapCodec<CrouchingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CrouchingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);
        return entity != null && entity.isCrouching();
    }

    @Override
    public ConditionSerializer<CrouchingCondition> getSerializer() {
        return ConditionSerializers.CROUCHING.get();
    }

    public static class Serializer extends ConditionSerializer<CrouchingCondition> {

        @Override
        public MapCodec<CrouchingCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, CrouchingCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Crouching")
                    .setDescription("Checks if the entity is crouching.")
                    .addExampleObject(new CrouchingCondition());
        }
    }

}
