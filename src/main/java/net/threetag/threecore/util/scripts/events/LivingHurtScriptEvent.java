package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.threetag.threecore.util.scripts.ScriptParameterName;

public class LivingHurtScriptEvent extends LivingScriptEvent {

    private final DamageSource damageSource;
    private float damageAmount;

    public LivingHurtScriptEvent(LivingEntity livingEntity, DamageSource damageSource, float damageAmount) {
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

    public void setAmount(@ScriptParameterName("amount") float amount) {
        this.damageAmount = amount;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
