package net.threetag.palladium.power.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import net.threetag.palladiumcore.event.PlayerEvents;

import java.util.concurrent.atomic.AtomicReference;

public class AbilityEventHandler implements LivingEntityEvents.Hurt, LivingEntityEvents.Attack, PlayerEvents.NameFormat {

    public static void init() {
        AbilityEventHandler handler = new AbilityEventHandler();
        LivingEntityEvents.ATTACK.register(handler);
        LivingEntityEvents.HURT.register(handler);
        PlayerEvents.NAME_FORMAT.register(handler);
    }

    @Override
    public EventResult livingEntityHurt(LivingEntity entity, DamageSource damageSource, AtomicReference<Float> amount) {
        if (damageSource.is(DamageTypes.IN_WALL) && AbilityUtil.isTypeEnabled(entity, Abilities.INTANGIBILITY.get())) {
            return EventResult.cancel();
        }

        for (AbilityInstance entry : AbilityUtil.getEnabledInstances(entity, Abilities.DAMAGE_IMMUNITY.get())) {
            if (DamageImmunityAbility.isImmuneAgainst(entry, damageSource)) {
                return EventResult.cancel();
            }
        }

        if (damageSource.is(DamageTypes.FALL) && entity.getAttributes().hasAttribute(PalladiumAttributes.FALL_RESISTANCE.get())) {
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
        if (damageSource.getEntity() instanceof LivingEntity sourceEntity && AbilityUtil.isTypeEnabled(sourceEntity, Abilities.FIRE_ASPECT.get())) {
            boolean hasAddedExistingFire = false;
            int fireSeconds = 0;
            for (AbilityInstance entry : AbilityUtil.getEnabledInstances(sourceEntity, Abilities.FIRE_ASPECT.get())) {
                int time = Math.max(entry.getProperty(FireAspectAbility.TIME), 0);
                if (!hasAddedExistingFire && entry.getProperty(FireAspectAbility.SHOULD_STACK_TIME)) {
                    fireSeconds = Math.min(fireSeconds + (entity.getRemainingFireTicks() / 20), entry.getProperty(FireAspectAbility.MAX_TIME));
                    hasAddedExistingFire = true;
                }
                fireSeconds += time;
            }
            entity.setSecondsOnFire(fireSeconds);
        }
        return this.livingEntityHurt(entity, damageSource, new AtomicReference<>(amount));
    }

    @Override
    public void playerNameFormat(Player player, Component username, AtomicReference<Component> displayName) {
        AbilityUtil.getEnabledInstances(player, Abilities.NAME_CHANGE.get())
                .stream()
                .filter(ab -> ab.getProperty(NameChangeAbility.NAME_CACHED) != null)
                .findFirst()
                .ifPresent(ability -> displayName.set(ability.getProperty(NameChangeAbility.NAME_CACHED)));
    }
}
