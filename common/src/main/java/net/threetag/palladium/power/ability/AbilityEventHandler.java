package net.threetag.palladium.power.ability;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.LivingEntityEvents;

import java.util.concurrent.atomic.AtomicReference;

public class AbilityEventHandler implements LivingEntityEvents.Hurt, LivingEntityEvents.Attack {

    public static void init() {
        AbilityEventHandler handler = new AbilityEventHandler();
        LivingEntityEvents.ATTACK.register(handler);
        LivingEntityEvents.HURT.register(handler);
    }

    @Override
    public EventResult livingEntityHurt(LivingEntity entity, DamageSource damageSource, AtomicReference<Float> amount) {
        for (AbilityEntry entry : AbilityUtil.getEnabledEntries(entity, Abilities.DAMAGE_IMMUNITY.get())) {
            if (DamageImmunityAbility.isImmuneAgainst(entry, damageSource)) {
                return EventResult.cancel();
            }
        }

        if (damageSource == DamageSource.FALL && entity.getAttributes().hasAttribute(PalladiumAttributes.FALL_RESISTANCE.get())) {
            var resistance = entity.getAttributeValue(PalladiumAttributes.FALL_RESISTANCE.get());

            if (resistance == 100D) {
                return EventResult.cancel();
            } else {
                amount.set((float) (amount.get() * (1F / resistance)));
            }
        }

        return EventResult.pass();
    }

    @Override
    public EventResult livingEntityAttack(LivingEntity entity, DamageSource damageSource, float amount) {
        return this.livingEntityHurt(entity, damageSource, new AtomicReference<>(amount));
    }
}
