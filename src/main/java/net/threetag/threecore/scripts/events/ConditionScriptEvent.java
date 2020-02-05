package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.ability.Ability;
import net.threetag.threecore.ability.condition.Condition;
import net.threetag.threecore.scripts.accessors.ConditionAccessor;

public abstract class ConditionScriptEvent extends AbilityScriptEvent {

    private final ConditionAccessor conditionAccessor;

    public ConditionScriptEvent(LivingEntity livingEntity, Ability ability, Condition condition) {
        super(livingEntity, ability);
        this.conditionAccessor = new ConditionAccessor(condition);
    }

    public ConditionAccessor getCondition() {
        return conditionAccessor;
    }
}
