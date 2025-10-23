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

public record NotCondition(List<Condition> conditions) implements Condition {

    public static final MapCodec<NotCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(PalladiumCodecs.listOrPrimitive(Condition.FALSE_TRUE_WRAPPED_CODEC).fieldOf("conditions").forGetter(NotCondition::conditions)
            ).apply(instance, NotCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, NotCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, Condition.STREAM_CODEC), NotCondition::conditions, NotCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        for (Condition condition : this.conditions) {
            if (condition.test(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer<NotCondition> getSerializer() {
        return ConditionSerializers.NOT.get();
    }

    public static class Serializer extends ConditionSerializer<NotCondition> {

        @Override
        public MapCodec<NotCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, NotCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Returns true if all conditions are disabled.";
        }
    }
}
