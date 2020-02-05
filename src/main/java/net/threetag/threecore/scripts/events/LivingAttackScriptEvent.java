package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.threetag.threecore.scripts.accessors.DamageSourceAccessor;

public class LivingAttackScriptEvent extends LivingScriptEvent {

    private final DamageSourceAccessor damageSource;
    private final float damageAmount;

    public LivingAttackScriptEvent(LivingEntity livingEntity, DamageSource damageSource, float damageAmount) {
        super(livingEntity);
        this.damageSource = new DamageSourceAccessor(damageSource);
        this.damageAmount = damageAmount;
    }

    public DamageSourceAccessor getDamageSource() {
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
