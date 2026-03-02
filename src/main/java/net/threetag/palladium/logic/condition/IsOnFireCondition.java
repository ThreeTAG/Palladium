package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public class IsOnFireCondition implements Condition {

    public static final IsOnFireCondition INSTANCE = new IsOnFireCondition();

    public static final MapCodec<IsOnFireCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsOnFireCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isOnFire();
    }

    @Override
    public ConditionSerializer<IsOnFireCondition> getSerializer() {
        return ConditionSerializers.IS_ON_FIRE.get();
    }

    public static class Serializer extends ConditionSerializer<IsOnFireCondition> {

        @Override
        public MapCodec<IsOnFireCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsOnFireCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is on Fire")
                    .setDescription("Checks if the entity is currently on fire.")
                    .addExampleObject(new IsOnFireCondition());
        }
    }
}
