package net.threetag.threecore.util.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.threetag.threecore.util.scripts.accessors.DamageSourceAccessor;

public class LivingDeathScriptEvent extends LivingScriptEvent {

    private final DamageSourceAccessor damageSource;

    public LivingDeathScriptEvent(LivingEntity livingEntity, DamageSource damageSource) {
        super(livingEntity);
        this.damageSource = new DamageSourceAccessor(damageSource);
    }

    public DamageSourceAccessor getDamageSource() {
        return this.damageSource;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

}
