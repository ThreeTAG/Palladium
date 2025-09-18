package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;

public class IsInRainCondition implements Condition {

    public static final IsInRainCondition INSTANCE = new IsInRainCondition();

    public static final MapCodec<IsInRainCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsInRainCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInRain();
    }

    @Override
    public ConditionSerializer<IsInRainCondition> getSerializer() {
        return ConditionSerializers.IS_IN_RAIN.get();
    }

    public static class Serializer extends ConditionSerializer<IsInRainCondition> {

        @Override
        public MapCodec<IsInRainCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsInRainCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in rain.";
        }
    }
}
