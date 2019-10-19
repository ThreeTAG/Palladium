package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.abilities.condition.Condition;

public class ConditionDataUpdatedScriptEvent extends ConditionScriptEvent {

    private final String data;
    private final Object value;
    private final Object oldValue;

    public ConditionDataUpdatedScriptEvent(LivingEntity livingEntity, Ability ability, Condition condition, String data, Object value, Object oldValue) {
        super(livingEntity, ability, condition);
        this.data = data;
        this.value = value;
        this.oldValue = oldValue;
    }

    public String getData() {
        return data;
    }

    public Object getValue() {
        return value;
    }

    public Object getOldValue() {
        return oldValue;
    }

    @Override
    public boolean isCancelable() {
        return false;
    }
}
