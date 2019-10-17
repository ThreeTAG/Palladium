package net.threetag.threecore.util.scripts.events;

import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class LivingDeathScriptEvent extends LivingScriptEvent {

    public LivingDeathScriptEvent(LivingDeathEvent event) {
        super(event);
    }

    public LivingDeathEvent getEvent() {
        return (LivingDeathEvent) this.event;
    }

    // TODO make DamageSourceAccessor
    public DamageSource getDamageSource() {
        return getEvent().getSource();
    }

}
