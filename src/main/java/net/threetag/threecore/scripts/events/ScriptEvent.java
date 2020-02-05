package net.threetag.threecore.scripts.events;

import net.minecraftforge.eventbus.api.Event;
import net.threetag.threecore.scripts.ScriptEventManager;
import net.threetag.threecore.scripts.ScriptParameterName;

import javax.annotation.Nullable;

public abstract class ScriptEvent {

    private boolean canceled = false;

    public void setCanceled(@ScriptParameterName("canceled") boolean canceled) {
        if (this.isCancelable())
            this.canceled = canceled;
    }

    public boolean isCancelled() {
        return this.isCancelable() && this.canceled;
    }

    public abstract boolean isCancelable();

    public boolean fire() {
        return this.fire(null);
    }

    public boolean fire(@Nullable Event event) {
        ScriptEventManager.fireEvent(this);
        if (event != null && event.isCanceled())
            event.setCanceled(true);
        return this.isCancelled();
    }

}
