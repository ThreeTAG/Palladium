package net.threetag.palladium.power.ability;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.LivingEntityEvents;

public class AbilityEventHandler implements LivingEntityEvents.Hurt {

    public static void init() {
        AbilityEventHandler handler = new AbilityEventHandler();
        LivingEntityEvents.HURT.register(handler);
    }

    @Override
    public EventResult livingEntityHurt(LivingEntity entity, DamageSource damageSource, float amount) {
        for(AbilityEntry entry : Ability.getEnabledEntries(entity, Abilities.DAMAGE_IMMUNITY.get())) {
            if(DamageImmunityAbility.isImmuneAgainst(entry, damageSource)) {
                return EventResult.cancel();
            }
        }
        return EventResult.pass();
    }
}
