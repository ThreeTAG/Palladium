package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.abilities.Ability;
import net.threetag.threecore.util.scripts.accessors.AbilityAccessor;

public abstract class AbilityScriptEvent extends LivingScriptEvent {

    private final AbilityAccessor abilityAccessor;

    public AbilityScriptEvent(LivingEntity livingEntity, Ability ability) {
        super(livingEntity);
        this.abilityAccessor = new AbilityAccessor(ability);
    }

    public AbilityAccessor getAbility() {
        return abilityAccessor;
    }

}
