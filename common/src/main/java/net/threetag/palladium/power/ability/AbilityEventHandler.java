package net.threetag.palladium.power.ability;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public class AbilityEventHandler implements EntityEvent.LivingHurt {

    public static void init() {
        AbilityEventHandler handler = new AbilityEventHandler();
        EntityEvent.LIVING_HURT.register(handler);
    }

    @Override
    public EventResult hurt(LivingEntity entity, DamageSource source, float amount) {
        for(AbilityEntry entry : Ability.getEnabledEntries(entity, Abilities.DAMAGE_IMMUNITY.get())) {
            if(DamageImmunityAbility.isImmuneAgainst(entry, source)) {
                return EventResult.interruptFalse();
            }
        }
        return EventResult.pass();
    }
}
