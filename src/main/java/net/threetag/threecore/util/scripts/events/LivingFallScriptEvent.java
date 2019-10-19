package net.threetag.threecore.util.scripts.events;

import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.threetag.threecore.util.scripts.ScriptParameterName;

public class LivingFallScriptEvent extends LivingScriptEvent {

    public LivingFallScriptEvent(LivingFallEvent event) {
        super(event);
    }

    private LivingFallEvent getEvent() {
        return (LivingFallEvent) this.event;
    }

    public float getDistance() {
        return getEvent().getDistance();
    }

    public void setDistance(@ScriptParameterName("distance") float distance, @ScriptParameterName("damageMultiplier") float test) {
        this.getEvent().setDistance(distance);
    }

    public float getDamageMultiplier() {
        return getEvent().getDamageMultiplier();
    }

    public void setDamageMultiplier(@ScriptParameterName("damageMultiplier") float damageMultiplier) {
        this.getEvent().setDamageMultiplier(damageMultiplier);
    }

}
