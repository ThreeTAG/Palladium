package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.ArrayList;
import java.util.List;

public record OrCondition(List<Condition> conditions) implements Condition {

    public static final MapCodec<OrCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(PalladiumCodecs.listOrPrimitive(Condition.FALSE_TRUE_WRAPPED_CODEC).fieldOf("conditions").forGetter(OrCondition::conditions)
            ).apply(instance, OrCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, OrCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, Condition.STREAM_CODEC), OrCondition::conditions, OrCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        for (Condition condition : this.conditions) {
            if (condition.test(context)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConditionSerializer<OrCondition> getSerializer() {
        return ConditionSerializers.OR.get();
    }

    public static class Serializer extends ConditionSerializer<OrCondition> {

        @Override
        public MapCodec<OrCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, OrCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns true if at least one of the conditions is active.";
        }
    }
}
