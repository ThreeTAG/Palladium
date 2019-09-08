package com.threetag.threecore.abilities.data;

public class ThreeDataEntry<T> {

    private final ThreeData<T> key;
    private T value;

    public ThreeDataEntry(ThreeData<T> key, T value) {
        this.key = key;
        this.value = value;
    }

    public ThreeData<T> getKey() {
        return this.key;
    }

    public void setValue(T valueIn) {
        this.value = valueIn;
    }

    public T getValue() {
        return this.value;
    }

    public ThreeDataEntry<T> copy() {
        return new ThreeDataEntry<T>(this.key, this.value);
    }

}
