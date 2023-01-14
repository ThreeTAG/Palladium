package net.threetag.palladium.compat.kubejs.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ScriptableCondition extends Condition {

    public final ConditionBuilder builder;
    public final ConditionSerializer serializer;
	public final Map<String, Object> extraProperties;

    public ScriptableCondition(ConditionBuilder builder, ConditionSerializer serializer, Map<String, Object> extraProperties) {
        this.builder = builder;
        this.serializer = serializer;
	    this.extraProperties = extraProperties;

	    Palladium.LOGGER.info("ScriptableCondition constructor");
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return this.builder.test != null && this.builder.test.test(entity, this.extraProperties);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return this.serializer;
    }

}
