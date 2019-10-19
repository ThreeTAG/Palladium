package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.condition.Condition;

public abstract class ConditionScriptEvent extends AbilityScriptEvent {

    // TODO condition accessor
    private final Condition condition;

    public ConditionScriptEvent(LivingEntity livingEntity, Ability ability, Condition condition) {
        super(livingEntity, ability);
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }
}
