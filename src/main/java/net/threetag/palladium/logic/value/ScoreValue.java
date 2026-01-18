package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.ScoreboardUtil;

public class ScoreValue extends IntegerValue {

    public static final MapCodec<ScoreValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("objective").forGetter(v -> v.objectiveName),
            modifyFunctionCodec()
    ).apply(instance, ScoreValue::new));

    private final String objectiveName;

    public ScoreValue(String objectiveName, String molang) {
        super(molang);
        this.objectiveName = objectiveName;
    }

    @Override
    public int getInteger(DataContext context) {
        var entity = context.getEntity();
        return entity != null ? ScoreboardUtil.getScore(entity, this.objectiveName, 0) : 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.SCORE.get();
    }

    public static class Serializer extends IntSerializer<ScoreValue> {

        @Override
        public MapCodec<ScoreValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, ScoreValue> builder, HolderLookup.Provider provider) {
            builder.setName("Score").setDescription("Returns the score of the entity for the given objective.")
                    .addExampleObject(new ScoreValue("objective_name", "value * 2"));
        }
    }
}
