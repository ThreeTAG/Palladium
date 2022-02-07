package net.threetag.palladium.util.property;

import net.minecraft.nbt.Tag;

public record PalladiumPropertyValue<T>(PalladiumProperty<T> data, T value) {

    public PalladiumProperty<T> getData() {
        return data;
    }

    public T getValue() {
        return value;
    }

    public Tag toNBT() {
        return this.data.toNBT(this.value);
    }
}
