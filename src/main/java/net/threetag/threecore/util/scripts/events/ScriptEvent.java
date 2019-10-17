package net.threetag.threecore.util.scripts.events;

import net.minecraftforge.eventbus.api.Event;
import net.threetag.threecore.util.scripts.ScriptEventManager;

public class ScriptEvent {

    protected final Event event;

    public ScriptEvent(Event event) {
        this.event = event;
    }

    public void setCancelled(boolean cancelled) {
        this.event.setCanceled(cancelled);
    }

    public boolean isCancelled() {
        return this.event.isCanceled();
    }

    public boolean isCancelable() {
        return this.event.isCancelable();
    }

    public void fire() {
        ScriptEventManager.fireEvent(this);
    }

}
