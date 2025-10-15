package net.threetag.palladium.core.event;

import java.util.*;
import java.util.function.Function;

public class PalladiumEvent<T> {

    private final Map<T, Priority> handlers;
    private final Function<List<T>, T> multiplexer;
    private T invoker;

    public PalladiumEvent(Class<T> handlerClass, Function<List<T>, T> multiplexer) {
        this.handlers = new LinkedHashMap<>();
        this.multiplexer = multiplexer;
        update();
    }

    public void register(T handler) {
        this.register(Priority.NORMAL, handler);
    }

    public void register(Priority priority, T handler) {
        this.handlers.put(handler, priority);
        this.update();
    }

    private void update() {
        List<T> listeners = new ArrayList<>(this.handlers.keySet());
        listeners.sort(Comparator.comparingInt(value -> this.handlers.get(value).ordinal()));
        this.invoker = this.multiplexer.apply(listeners);
    }

    public T invoker() {
        return invoker;
    }

    public static <T> PalladiumEventResult result(List<T> listeners, Function<T, PalladiumEventResult> function) {
        boolean cancel = false;

        for (T listener : listeners) {
            PalladiumEventResult result = function.apply(listener);

            if (result.cancelsEvent()) {
                cancel = true;
            }

            if (result.stopsListeners()) {
                break;
            }
        }
        return cancel ? PalladiumEventResult.cancel() : PalladiumEventResult.pass();
    }

    public enum Priority {

        HIGHEST,
        HIGH,
        NORMAL,
        LOW,
        LOWEST;

    }

}
