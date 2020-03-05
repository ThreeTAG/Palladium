package net.threetag.threecore.util.threedata;

import javax.annotation.Nonnull;

public class ThreeDataEntry<T> {

    private final ThreeData<T> key;
    private T value;
    private T defaultValue;

    public ThreeDataEntry(@Nonnull ThreeData<T> key, @Nonnull T value, @Nonnull T defaultValue) {
        this.key = key;
        this.value = value;
        this.defaultValue = defaultValue;
    }

    public ThreeData<T> getKey() {
        return this.key;
    }

    public void setValue(@Nonnull T valueIn) {
        if (valueIn.equals(defaultValue)) {
            this.value = null;
        } else {
            this.value = valueIn;
        }
    }

    @Nonnull
    public T getValue() {
        return this.value == null ? this.getDefaultValue() : this.value;
    }

    @Nonnull
    public T getDefaultValue() {
        return this.defaultValue;
    }

    public boolean holdsDefaultValue() {
        return this.value == null;
    }

    public ThreeDataEntry<T> copy() {
        return new ThreeDataEntry<T>(this.key, this.value, this.defaultValue);
    }

}
