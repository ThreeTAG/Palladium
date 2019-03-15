package com.threetag.threecore.abilities.data;

public class AbilityDataEntry<T> {

    private final AbilityData<T> key;
    private T value;

    public AbilityDataEntry(AbilityData<T> key, T value) {
        this.key = key;
        this.value = value;
    }

    public AbilityData<T> getKey() {
        return this.key;
    }

    public void setValue(T valueIn) {
        this.value = valueIn;
    }

    public T getValue() {
        return this.value;
    }

    public AbilityDataEntry<T> copy() {
        return new AbilityDataEntry<T>(this.key, this.value);
    }

}
