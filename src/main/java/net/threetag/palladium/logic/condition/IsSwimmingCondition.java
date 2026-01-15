package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public class IsSwimmingCondition implements Condition {

    public static final IsSwimmingCondition INSTANCE = new IsSwimmingCondition();

    public static final MapCodec<IsSwimmingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsSwimmingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isSwimming();
    }

    @Override
    public ConditionSerializer<IsSwimmingCondition> getSerializer() {
        return ConditionSerializers.IS_SWIMMING.get();
    }

    public static class Serializer extends ConditionSerializer<IsSwimmingCondition> {

        @Override
        public MapCodec<IsSwimmingCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsSwimmingCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is swimming.";
        }
    }
}
