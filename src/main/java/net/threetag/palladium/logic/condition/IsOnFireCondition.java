package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;

public class IsOnFireCondition implements Condition {

    public static final IsOnFireCondition INSTANCE = new IsOnFireCondition();

    public static final MapCodec<IsOnFireCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsOnFireCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

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
        public StreamCodec<RegistryFriendlyByteBuf, IsOnFireCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is on fire.";
        }
    }
}
