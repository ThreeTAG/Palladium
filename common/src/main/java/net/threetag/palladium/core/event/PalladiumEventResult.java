package net.threetag.palladium.core.event;

public class PalladiumEventResult {

    private static final PalladiumEventResult CANCEL = new PalladiumEventResult(true, true);
    private static final PalladiumEventResult CANCEL_AND_CONTINUE = new PalladiumEventResult(true, false);
    private static final PalladiumEventResult STOP_LISTENERS = new PalladiumEventResult(false, true);
    private static final PalladiumEventResult PASS = new PalladiumEventResult(false, false);

    private final boolean cancelEvent;
    private final boolean stopListeners;

    private PalladiumEventResult(boolean cancelEvent, boolean stopListeners) {
        this.cancelEvent = cancelEvent;
        this.stopListeners = stopListeners;
    }

    public boolean cancelsEvent() {
        return this.cancelEvent;
    }

    public boolean stopsListeners() {
        return this.stopListeners;
    }

    /**
     * @return Cancel the event and prevent further listeners from being executed
     */
    public static PalladiumEventResult cancel() {
        return CANCEL;
    }

    /**
     * @return Mark event as cancelled but still execute next listeners
     */
    public static PalladiumEventResult cancelAndContinue() {
        return CANCEL_AND_CONTINUE;
    }

    /**
     * @return Does not cancel event but stops next listeners from being executed
     */
    public static PalladiumEventResult stopListeners() {
        return STOP_LISTENERS;
    }

    /**
     * @return Does not intervene in the event or the execution of other listeners
     */
    public static PalladiumEventResult pass() {
        return PASS;
    }

}
