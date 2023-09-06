package net.threetag.palladium.compat.kubejs.condition;

import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.util.context.DataContext;

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
    public boolean active(DataContext context) {
        return this.builder.test != null && this.builder.test.test(context.getLivingEntity(), this.extraProperties);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return this.serializer;
    }

}
