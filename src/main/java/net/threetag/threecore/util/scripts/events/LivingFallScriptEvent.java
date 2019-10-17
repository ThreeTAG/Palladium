package net.threetag.threecore.util.scripts.events;

import net.minecraftforge.event.entity.living.LivingFallEvent;

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

    public void setDistance(float distance) {
        this.getEvent().setDistance(distance);
    }

    public float getDamageMultiplier() {
        return getEvent().getDamageMultiplier();
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.getEvent().setDamageMultiplier(damageMultiplier);
    }

}
