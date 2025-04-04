package net.threetag.palladium.client.variable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.CrouchingCondition;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

import java.util.Collections;
import java.util.List;

public class ConditionVariable extends BooleanPathVariable {

    public static final MapCodec<ConditionVariable> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Condition.LIST_CODEC.fieldOf("condition").forGetter(v -> v.conditions),
            onTrueCodec(), onFalseCodec()
    ).apply(instance, ConditionVariable::new));

    private final List<Condition> conditions;

    public ConditionVariable(List<Condition> conditions, String onTrue, String onFalse) {
        super(onTrue, onFalse);
        this.conditions = conditions;
    }

    @Override
    public boolean getBoolean(DataContext context) {
        return Condition.checkConditions(this.conditions, context);
    }

    @Override
    public PathVariableSerializer<?> getSerializer() {
        return PathVariableSerializers.CONDITION;
    }

    public static class Serializer extends BooleanSerializer<ConditionVariable> {

        @Override
        public MapCodec<ConditionVariable> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<PathVariable, ConditionVariable> builder, HolderLookup.Provider provider) {
            builder.setName("Condition").setDescription("Accepts to condition(s) to use different strings as variables")
                    .add("condition", TYPE_CONDITION_LIST, "The condition(s) to check. Can be a single condition or a list of conditions.")
                    .setExampleObject(new ConditionVariable(Collections.singletonList(new CrouchingCondition()), "is_crouching", "not_crouching"));
        }
    }
}
