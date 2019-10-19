package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class LivingAttackScriptEvent extends LivingScriptEvent {

    private final DamageSource damageSource;
    private final float damageAmount;

    public LivingAttackScriptEvent(LivingEntity livingEntity, DamageSource damageSource, float damageAmount) {
        super(livingEntity);
        this.damageSource = damageSource;
        this.damageAmount = damageAmount;
    }

    // TODO make DamageSourceAccessor
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public float getAmount() {
        return this.damageAmount;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
