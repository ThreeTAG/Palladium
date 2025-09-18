package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.CrouchingCondition;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.Collections;
import java.util.List;

public class ConditionValue extends BooleanValue {

    public static final MapCodec<ConditionValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Condition.LIST_CODEC.fieldOf("condition").forGetter(v -> v.conditions),
            onTrueCodec(), onFalseCodec()
    ).apply(instance, ConditionValue::new));

    private final List<Condition> conditions;

    public ConditionValue(List<Condition> conditions, String onTrue, String onFalse) {
        super(onTrue, onFalse);
        this.conditions = conditions;
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return Condition.checkConditions(this.conditions, context);
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.CONDITION.get();
    }

    public static class Serializer extends BooleanSerializer<ConditionValue> {

        @Override
        public MapCodec<ConditionValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, ConditionValue> builder, HolderLookup.Provider provider) {
            builder.setName("Condition").setDescription("Accepts to condition(s) to use different strings as variables")
                    .add("condition", TYPE_CONDITION_LIST, "The condition(s) to check. Can be a single condition or a list of conditions.")
                    .setExampleObject(new ConditionValue(Collections.singletonList(new CrouchingCondition()), "is_crouching", "not_crouching"));
        }
    }
}
