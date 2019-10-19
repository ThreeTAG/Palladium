package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;

public class LivingDeathScriptEvent extends LivingScriptEvent {

    private final DamageSource damageSource;

    public LivingDeathScriptEvent(LivingEntity livingEntity, DamageSource damageSource) {
        super(livingEntity);
        this.damageSource = damageSource;
    }

    // TODO make DamageSourceAccessor
    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

}
