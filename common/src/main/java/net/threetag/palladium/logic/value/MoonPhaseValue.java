package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class MoonPhaseValue extends IntegerValue {

    public static final MapCodec<MoonPhaseValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, MoonPhaseValue::new));

    public MoonPhaseValue(String molang) {
        super(molang);
    }

    @Override
    public int getInteger(DataContext context) {
        var level = context.getLevel();
        return level != null ? level.getMoonPhase() : 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.MOON_PHASE.get();
    }

    public static class Serializer extends IntSerializer<MoonPhaseValue> {

        @Override
        public MapCodec<MoonPhaseValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, MoonPhaseValue> builder, HolderLookup.Provider provider) {
            builder.setName("Moon Phase").setDescription("Returns the current moon phase, an integer from 0 to 7.")
                    .setExampleObject(new MoonPhaseValue("value * 2"));
        }
    }
}
