package net.threetag.threecore.scripts.events;

import net.minecraft.entity.LivingEntity;
import net.threetag.threecore.scripts.ScriptParameterName;

public class LivingFallScriptEvent extends LivingScriptEvent {

    private float distance;
    private float damageMultiplier;

    public LivingFallScriptEvent(LivingEntity livingEntity, float distance, float damageMultiplier) {
        super(livingEntity);
        this.distance = distance;
        this.damageMultiplier = damageMultiplier;
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(@ScriptParameterName("distance") float distance) {
        this.distance = distance;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public void setDamageMultiplier(@ScriptParameterName("damageMultiplier") float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    @Override
    public boolean isCancelable() {
        return true;
    }
}
