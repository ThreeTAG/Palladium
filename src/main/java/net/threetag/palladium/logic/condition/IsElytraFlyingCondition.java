package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;

public class IsElytraFlyingCondition implements Condition {

    public static final IsElytraFlyingCondition INSTANCE = new IsElytraFlyingCondition();

    public static final MapCodec<IsElytraFlyingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsElytraFlyingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        return entity.isFallFlying();
    }

    @Override
    public ConditionSerializer<IsElytraFlyingCondition> getSerializer() {
        return ConditionSerializers.IS_ELYTRA_FLYING.get();
    }

    public static class Serializer extends ConditionSerializer<IsElytraFlyingCondition> {

        @Override
        public MapCodec<IsElytraFlyingCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsElytraFlyingCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is currently flying with an elytra.";
        }
    }
}
