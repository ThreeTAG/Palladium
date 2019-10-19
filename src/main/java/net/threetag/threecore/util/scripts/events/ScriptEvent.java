package net.threetag.threecore.util.scripts.events;

import net.minecraftforge.eventbus.api.Event;
import net.threetag.threecore.util.scripts.ScriptEventManager;
import net.threetag.threecore.util.scripts.ScriptParameterName;

public class ScriptEvent {

    protected final Event event;

    public ScriptEvent(Event event) {
        this.event = event;
    }

    public void setCanceled(@ScriptParameterName("canceled") boolean canceled) {
        this.event.setCanceled(canceled);
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
