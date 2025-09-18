package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;

public class IsMovingCondition implements Condition {

    public static final IsMovingCondition INSTANCE = new IsMovingCondition();

    public static final MapCodec<IsMovingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsMovingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.getEntity();

        if (entity == null) {
            return false;
        }

        // TODO
//        if (entity.level().isClientSide) {
            return entity.xo != entity.getX() || entity.yo != entity.getY() || entity.zo != entity.getZ();
//        } else {
//            return entity.walkDist != entity.walkDistO;
//        }
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
        public String getDocumentationDescription() {
            return "Checks if the entity is moving.";
        }
    }
}
