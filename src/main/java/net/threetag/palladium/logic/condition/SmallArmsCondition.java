package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PlayerUtil;

public class SmallArmsCondition implements Condition {

    public static final SmallArmsCondition INSTANCE = new SmallArmsCondition();

    public static final MapCodec<SmallArmsCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, SmallArmsCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var player = context.getPlayer();
        return player != null && PlayerUtil.hasSmallArms(player);
    }

    @Override
    public ConditionSerializer<SmallArmsCondition> getSerializer() {
        return ConditionSerializers.SMALL_ARMS.get();
    }

    public static class Serializer extends ConditionSerializer<SmallArmsCondition> {

        @Override
        public MapCodec<SmallArmsCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SmallArmsCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.ASSETS;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has small arms. Returns false if the entity is not a player or if this condition is being checked sever-side.";
        }
    }
}
