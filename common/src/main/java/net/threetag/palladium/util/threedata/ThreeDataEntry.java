package net.threetag.palladium.util.threedata;

import net.minecraft.nbt.Tag;

public record ThreeDataEntry<T>(ThreeData<T> data, T value) {

    public ThreeData<T> getData() {
        return data;
    }

    public T getValue() {
        return value;
    }

    public Tag toNBT() {
        return this.data.toNBT(this.value);
    }
}
