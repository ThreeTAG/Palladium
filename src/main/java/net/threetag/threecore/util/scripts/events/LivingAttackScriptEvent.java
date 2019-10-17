package net.threetag.threecore.util.scripts.events;

import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class LivingAttackScriptEvent extends LivingScriptEvent {

    public LivingAttackScriptEvent(LivingAttackEvent event) {
        super(event);
    }

    private LivingAttackEvent getEvent() {
        return (LivingAttackEvent) this.event;
    }

    // TODO make DamageSourceAccessor
    public DamageSource getDamageSource() {
        return getEvent().getSource();
    }

    public float getAmount() {
        return getEvent().getAmount();
    }

}
