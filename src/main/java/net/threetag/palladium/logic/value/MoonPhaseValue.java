package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

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
        var pos = context.getBlockPos();

        if (level == null || pos == null) {
            return 0;
        }

        var moonPhase = level.environmentAttributes().getValue(EnvironmentAttributes.MOON_PHASE, pos);
        return moonPhase.index();
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
                    .addExampleObject(new MoonPhaseValue("value * 2"));
        }
    }
}
