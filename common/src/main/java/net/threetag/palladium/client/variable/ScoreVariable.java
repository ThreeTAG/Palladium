package net.threetag.palladium.client.variable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.ScoreboardUtil;

public class ScoreVariable extends IntegerPathVariable {

    public static final MapCodec<ScoreVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("objective").forGetter(v -> v.objectiveName),
            modifyFunctionCodec()
    ).apply(instance, ScoreVariable::new));

    private final String objectiveName;

    public ScoreVariable(String objectiveName, String molang) {
        super(molang);
        this.objectiveName = objectiveName;
    }

    @Override
    public int getInteger(DataContext context) {
        var entity = context.getEntity();
        return entity != null ? ScoreboardUtil.getScore(entity, this.objectiveName, 0) : 0;
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.SCORE;
    }

    public static class Serializer extends IntSerializer<ScoreVariable> {

        @Override
        public MapCodec<ScoreVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, ScoreVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Score").setDescription("Returns the score of the entity for the given objective.")
                    .setExampleObject(new ScoreVariable("objective_name", "value * 2"));
        }
    }
}
