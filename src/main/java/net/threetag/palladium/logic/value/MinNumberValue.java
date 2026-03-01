package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.context.DataContext;

import java.util.Arrays;
import java.util.List;

public class MinNumberValue extends Value {

    public static final MapCodec<MinNumberValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Value.CODEC.listOf().fieldOf("values").forGetter(v -> v.values)
    ).apply(instance, MinNumberValue::new));

    private final List<Value> values;

    public MinNumberValue(List<Value> values) {
        this.values = values;
    }

    @Override
    public Object get(DataContext context) {
        Number n = 0;

        for (Value value : this.values) {
            n = Math.min(n.doubleValue(), value.getAsNumber(context).doubleValue());
        }

        return n;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.MIN_NUMBER.get();
    }

    public static class Serializer extends ValueSerializer<MinNumberValue> {

        @Override
        public MapCodec<MinNumberValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, MinNumberValue> builder, HolderLookup.Provider provider) {
            builder.setName("Min Number").setDescription("Returns the lowest number found within the given values")
                    .add("values", SettingType.list(TYPE_VALUE), "List of values")
                    .addExampleObject(new MinNumberValue(Arrays.asList(new MoonPhaseValue(""), new StaticValue(5))));
        }
    }
}
