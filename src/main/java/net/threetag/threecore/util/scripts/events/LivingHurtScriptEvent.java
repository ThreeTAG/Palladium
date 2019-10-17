package net.threetag.threecore.util.scripts.events;

import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class LivingHurtScriptEvent extends LivingScriptEvent {

    public LivingHurtScriptEvent(LivingHurtEvent event) {
        super(event);
    }

    private LivingHurtEvent getEvent() {
        return (LivingHurtEvent) this.event;
    }

    // TODO make DamageSourceAccessor
    public DamageSource getDamageSource() {
        return getEvent().getSource();
    }

    public float getAmount() {
        return getEvent().getAmount();
    }

    public void setAmount(float amount) {
        this.getEvent().setAmount(amount);
    }

}
