package net.threetag.palladium.compat.kubejs.condition;

import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;

import java.util.Map;

public class ScriptableCondition extends Condition {

    public final ConditionBuilder builder;
    public final ConditionSerializer serializer;
	public final Map<String, Object> extraProperties;

    public ScriptableCondition(ConditionBuilder builder, ConditionSerializer serializer, Map<String, Object> extraProperties) {
        this.builder = builder;
        this.serializer = serializer;
	    this.extraProperties = extraProperties;
    }

    @Override
    public boolean active(ConditionContext context) {
        return this.builder.test != null && this.builder.test.test(context.get(ConditionContextType.ENTITY), this.extraProperties);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return this.serializer;
    }

}
