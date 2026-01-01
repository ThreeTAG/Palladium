package net.threetag.palladium.util;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class CollectionDelta<T> {

    private final Collection<T> target;
    private final Set<T> desired;

    private Consumer<T> onAdd = (x) -> {
    };
    private Consumer<T> onRemove = (x) -> {
    };

    public CollectionDelta(Collection<T> target, Collection<T> desired) {
        this.target = Objects.requireNonNull(target);
        this.desired = Set.copyOf(desired);
    }

    public static <T> CollectionDelta<T> between(
            Collection<T> current,
            Collection<T> desired
    ) {
        return new CollectionDelta<>(current, desired);
    }

    public CollectionDelta<T> onAdd(Consumer<T> handler) {
        this.onAdd = Objects.requireNonNull(handler);
        return this;
    }

    public CollectionDelta<T> onRemove(Consumer<T> handler) {
        this.onRemove = Objects.requireNonNull(handler);
        return this;
    }

    public void apply() {
        Set<T> currentSnapshot = Set.copyOf(target);

        // removals
        for (T value : currentSnapshot) {
            if (!this.desired.contains(value)) {
                this.target.remove(value);
                this.onRemove.accept(value);
            }
        }

        // additions
        for (T value : this.desired) {
            if (!currentSnapshot.contains(value)) {
                this.target.add(value);
                this.onAdd.accept(value);
            }
        }
    }
}
