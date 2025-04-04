package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class MoonPhaseVariable extends IntegerPathVariable {

    public static final MapCodec<MoonPhaseVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, MoonPhaseVariable::new));

    public MoonPhaseVariable(String molang) {
        super(molang);
    }

    @Override
    public int getInteger(DataContext context) {
        var level = context.getLevel();
        return level != null ? level.getMoonPhase() : 0;
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.MOON_PHASE;
    }

    public static class Serializer extends IntSerializer<MoonPhaseVariable> {

        @Override
        public MapCodec<MoonPhaseVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, MoonPhaseVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Moon Phase").setDescription("Returns the current moon phase, an integer from 0 to 7.")
                    .setExampleObject(new MoonPhaseVariable("value * 2"));
        }
    }
}
