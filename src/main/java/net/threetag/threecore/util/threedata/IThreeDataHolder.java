package net.threetag.threecore.util.threedata;

import net.minecraft.nbt.CompoundNBT;

import java.util.Collection;

public interface IThreeDataHolder {

    <T> IThreeDataHolder register(ThreeData<T> data, T defaultValue);

    <T> IThreeDataHolder set(ThreeData<T> data, T value);

    <T> T readValue(ThreeData<T> data, CompoundNBT nbt);

    <T> T get(ThreeData<T> data);

    <T> ThreeDataEntry<T> getEntry(ThreeData<T> data);

    boolean has(ThreeData<?> data);

    <T> T getDefaultValue(ThreeData<T> data);

    ThreeData<?> getDataByName(String name);

    default <T> IThreeDataHolder reset(ThreeData<T> data) {
        this.set(data, getDefaultValue(data));
        return this;
    }

    Collection<ThreeDataEntry<?>> getDataEntries();

}
