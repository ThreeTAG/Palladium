package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;

public class InLavaCondition implements Condition {

    public static final InLavaCondition INSTANCE = new InLavaCondition();

    public static final MapCodec<InLavaCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, InLavaCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInLava();
    }

    @Override
    public ConditionSerializer<InLavaCondition> getSerializer() {
        return ConditionSerializers.IN_LAVA.get();
    }

    public static class Serializer extends ConditionSerializer<InLavaCondition> {

        @Override
        public MapCodec<InLavaCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, InLavaCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in lava.";
        }
    }
}
