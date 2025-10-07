package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;

import java.util.ArrayList;
import java.util.List;

public record AndCondition(List<Condition> conditions) implements Condition {

    public static final MapCodec<AndCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(Condition.FALSE_TRUE_WRAPPED_CODEC.listOf().fieldOf("conditions").forGetter(AndCondition::conditions)
            ).apply(instance, AndCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AndCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, Condition.STREAM_CODEC), AndCondition::conditions, AndCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        for (Condition condition : this.conditions) {
            if (!condition.test(context)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ConditionSerializer<AndCondition> getSerializer() {
        return ConditionSerializers.AND.get();
    }

    public static class Serializer extends ConditionSerializer<AndCondition> {

        @Override
        public MapCodec<AndCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AndCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "A mainCondition that is active if all of the conditions in the array are active.";
        }
    }
}
