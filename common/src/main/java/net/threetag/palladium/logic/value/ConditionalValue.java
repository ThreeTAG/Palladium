package net.threetag.palladium.logic.value;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.CrouchingCondition;
import net.threetag.palladium.logic.context.DataContext;

public class ConditionalValue extends Value {

    public static final MapCodec<ConditionalValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Condition.CODEC.fieldOf("condition").forGetter(v -> v.condition),
            Value.CODEC.fieldOf("if_true").forGetter(v -> v.ifTrue),
            Value.CODEC.fieldOf("if_false").forGetter(v -> v.ifFalse)
    ).apply(instance, ConditionalValue::new));

    private final Condition condition;
    private final Value ifTrue;
    private final Value ifFalse;

    public ConditionalValue(Condition condition, Value ifTrue, Value ifFalse) {
        this.condition = condition;
        this.ifTrue = ifTrue;
        this.ifFalse = ifFalse;
    }

    @Override
    public Object get(DataContext context) {
        return this.condition.test(context) ? this.ifTrue.get(context) : this.ifFalse.get(context);
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.CONDITIONAL.get();
    }

    public static class Serializer extends ValueSerializer<ConditionalValue> {

        @Override
        public MapCodec<ConditionalValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, ConditionalValue> builder, HolderLookup.Provider provider) {
            builder.setName("Conditional Value").setDescription("Returns a value based on whether a condition is met.")
                    .add("condition", TYPE_CONDITION_LIST, "The condition to test.")
                    .add("if_true", TYPE_VALUE, "The value to return if the condition is met.")
                    .add("if_false", TYPE_VALUE, "The value to return if the condition is not met.")
                    .setExampleObject(new ConditionalValue(
                            new CrouchingCondition(),
                            new StaticValue(new Dynamic<>(JsonOps.INSTANCE, new JsonPrimitive("crouching"))),
                            new StaticValue(new Dynamic<>(JsonOps.INSTANCE, new JsonPrimitive("not_crouching")))));
        }
    }
}
