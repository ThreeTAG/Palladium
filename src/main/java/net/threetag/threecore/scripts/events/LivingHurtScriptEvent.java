package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.threetag.threecore.scripts.ScriptParameterName;
import net.threetag.threecore.scripts.accessors.DamageSourceAccessor;

public class LivingHurtScriptEvent extends LivingScriptEvent {

    private final DamageSourceAccessor damageSource;
    private float damageAmount;

    public LivingHurtScriptEvent(LivingEntity livingEntity, DamageSource damageSource, float damageAmount) {
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

    public void setAmount(@ScriptParameterName("amount") float amount) {
        this.damageAmount = amount;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
