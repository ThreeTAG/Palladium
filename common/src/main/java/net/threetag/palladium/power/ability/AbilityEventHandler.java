package net.threetag.palladium.power.ability;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.entity.PalladiumAttributes;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.LivingEntityEvents;
import net.threetag.palladiumcore.event.PlayerEvents;

import java.util.concurrent.atomic.AtomicReference;

public class AbilityEventHandler implements LivingEntityEvents.Hurt, LivingEntityEvents.Attack, PlayerEvents.NameFormat, LivingEntityEvents.Jump {

    public static void init() {
        AbilityEventHandler handler = new AbilityEventHandler();
        LivingEntityEvents.ATTACK.register(handler);
        LivingEntityEvents.HURT.register(handler);
        LivingEntityEvents.JUMP.register(handler);
        PlayerEvents.NAME_FORMAT.register(handler);
    }

    @Override
    public EventResult livingEntityHurt(LivingEntity entity, DamageSource damageSource, AtomicReference<Float> amount) {
        if (damageSource.is(DamageTypes.IN_WALL) && AbilityUtil.isTypeEnabled(entity, Abilities.INTANGIBILITY.get())) {
            return EventResult.cancel();
        }

        for (AbilityEntry entry : AbilityUtil.getEnabledEntries(entity, Abilities.DAMAGE_IMMUNITY.get())) {
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
        return this.livingEntityHurt(entity, damageSource, new AtomicReference<>(amount));
    }

    @Override
    public void playerNameFormat(Player player, Component username, AtomicReference<Component> displayName) {
        AbilityUtil.getEnabledEntries(player, Abilities.NAME_CHANGE.get()).stream().filter(ab -> ab.getProperty(NameChangeAbility.ACTIVE)).findFirst().ifPresent(ability -> {
            displayName.set(ability.getProperty(NameChangeAbility.NAME));
        });
    }

    @Override
    public void livingEntityJump(LivingEntity entity) {
        float mul = 0F;

        for (AbilityEntry entry : AbilityUtil.getEnabledEntries(entity, Abilities.LEAP.getId())) {
            float val = entry.getProperty(LeapAbility.MULTIPLIER);
            if (val > mul) {
                mul = val;
            }
        }

        if (mul > 0F) {
            Vec3 vec3 = entity.getDeltaMovement();
            vec3 = vec3.add(vec3.x * mul, 0, vec3.z * mul);
            entity.setDeltaMovement(vec3);
        }
    }
}
