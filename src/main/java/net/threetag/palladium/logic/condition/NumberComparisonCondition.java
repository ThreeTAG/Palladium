package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.value.EntityTickCountValue;
import net.threetag.palladium.logic.value.StaticValue;
import net.threetag.palladium.logic.value.Value;
import net.threetag.palladium.util.NumberComparator;

public record NumberComparisonCondition(Value value, NumberComparator operator, Value target) implements Condition {

    public static final MapCodec<NumberComparisonCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    Value.CODEC.fieldOf("value").forGetter(NumberComparisonCondition::value),
                    NumberComparator.CODEC.fieldOf("operator").forGetter(NumberComparisonCondition::operator),
                    Value.CODEC.fieldOf("target").forGetter(NumberComparisonCondition::target)
            ).apply(instance, NumberComparisonCondition::new)
    );

    @Override
    public boolean test(DataContext context) {
        var value = this.value.get(context);
        var target = this.target.get(context);

        if (value instanceof Number n1 && target instanceof Number n2) {
            return this.operator.compare(n1, n2);
        }

        return false;
    }

    @Override
    public ConditionSerializer<?> getSerializer() {
        return ConditionSerializers.NUMBER_COMPARISON.get();
    }

    public static class Serializer extends ConditionSerializer<NumberComparisonCondition> {

        @Override
        public MapCodec<NumberComparisonCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, NumberComparisonCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Number Comparison")
                    .setDescription("Compares two number values using the given operator.")
                    .add("value", TYPE_VALUE, "The main value in the comparison")
                    .add("operator", TYPE_NUMBER_COMPARATOR, "The comparison operator being used")
                    .add("target", TYPE_VALUE, "The value that the main value is being compared against")
                    .addExampleObject(new NumberComparisonCondition(new StaticValue(5), NumberComparator.GREATER_THAN, new StaticValue(3)))
                    .addExampleObject(new NumberComparisonCondition(new EntityTickCountValue(""), NumberComparator.LESS_OR_EQUAL, new StaticValue(20)));
        }
    }
}
