package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.context.DataContext;

import java.util.Arrays;
import java.util.List;

public class MaxNumberValue extends Value {

    public static final MapCodec<MaxNumberValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Value.CODEC.listOf().fieldOf("values").forGetter(v -> v.values),
            NumberType.CODEC.optionalFieldOf("number_type", NumberType.INTEGER).forGetter(v -> v.numberType)
    ).apply(instance, MaxNumberValue::new));

    private final List<Value> values;
    private final NumberType numberType;

    public MaxNumberValue(List<Value> values, NumberType numberType) {
        this.values = values;
        this.numberType = numberType;
    }

    @Override
    public Object get(DataContext context) {
        Number n = Double.MIN_VALUE;

        for (Value value : this.values) {
            n = Math.max(n.doubleValue(), value.getAsNumber(context).doubleValue());
        }

        return this.numberType.convertTo(n);
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.MAX_NUMBER.get();
    }

    public static class Serializer extends ValueSerializer<MaxNumberValue> {

        @Override
        public MapCodec<MaxNumberValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, MaxNumberValue> builder, HolderLookup.Provider provider) {
            builder.setName("Max Number").setDescription("Returns the highest number found within the given values")
                    .add("values", SettingType.list(TYPE_VALUE), "List of values")
                    .addOptional("number_type", SettingType.enumList(NumberType.values()), "The required number type the result should be formatted as")
                    .addExampleObject(new MaxNumberValue(Arrays.asList(new MoonPhaseValue(""), new StaticValue(5)), NumberType.INTEGER));
        }
    }
}
