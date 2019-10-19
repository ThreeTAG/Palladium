package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.abilities.Ability;

public class AbilityDataUpdatedScriptEvent extends AbilityScriptEvent {

    private final String data;
    private final Object value;
    private final Object oldValue;

    public AbilityDataUpdatedScriptEvent(LivingEntity livingEntity, Ability ability, String data, Object value, Object oldValue) {
        super(livingEntity, ability);
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
