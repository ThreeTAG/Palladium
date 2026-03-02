package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;
import net.threetag.palladium.util.ScoreboardUtil;

public record ObjectiveScoreCondition(String objectiveName, int min, int max) implements Condition {

    public static final MapCodec<ObjectiveScoreCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Codec.STRING.fieldOf("objective").forGetter(ObjectiveScoreCondition::objectiveName),
                    Codec.INT.optionalFieldOf("min", Integer.MIN_VALUE).forGetter(ObjectiveScoreCondition::min),
                    Codec.INT.optionalFieldOf("max", Integer.MAX_VALUE).forGetter(ObjectiveScoreCondition::max)
            ).apply(instance, ObjectiveScoreCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, ObjectiveScoreCondition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ObjectiveScoreCondition::objectiveName,
            ByteBufCodecs.VAR_INT, ObjectiveScoreCondition::min,
            ByteBufCodecs.VAR_INT, ObjectiveScoreCondition::max,
            ObjectiveScoreCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        int score = ScoreboardUtil.getScore(entity, this.objectiveName);
        return score >= this.min && score <= this.max;
    }

    @Override
    public ConditionSerializer<ObjectiveScoreCondition> getSerializer() {
        return ConditionSerializers.OBJECTIVE_SCORE.get();
    }

    public static class Serializer extends ConditionSerializer<ObjectiveScoreCondition> {

        @Override
        public MapCodec<ObjectiveScoreCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, ObjectiveScoreCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Objective Score")
                    .setDescription("Checks if the player has a score in a specific objective.")
                    .add("objective", TYPE_STRING, "Name of the scoreboard objective.")
                    .addOptional("min", TYPE_INT, "Minimum required score value.")
                    .addOptional("max", TYPE_INT, "Maximum required score value.")
                    .addExampleObject(new ObjectiveScoreCondition("example_objective", 5, 10));
        }
    }
}
