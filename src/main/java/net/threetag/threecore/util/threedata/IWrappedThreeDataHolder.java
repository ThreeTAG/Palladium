package net.threetag.threecore.util.threedata;

import net.minecraft.nbt.CompoundNBT;

import java.util.Collection;

public interface IWrappedThreeDataHolder extends IThreeDataHolder {

    IThreeDataHolder getThreeDataHolder();

    @Override
    default <T> IThreeDataHolder register(ThreeData<T> data, T defaultValue) {
        this.getThreeDataHolder().register(data, defaultValue);
        return this;
    }

    @Override
    default <T> IThreeDataHolder set(ThreeData<T> data, T value) {
        this.getThreeDataHolder().set(data, value);
        return this;
    }

    @Override
    default <T> T readValue(ThreeData<T> data, CompoundNBT nbt) {
        return this.getThreeDataHolder().readValue(data, nbt);
    }

    @Override
    default <T> T get(ThreeData<T> data) {
        return this.getThreeDataHolder().get(data);
    }

    @Override
    default <T> ThreeDataEntry<T> getEntry(ThreeData<T> data) {
        return this.getThreeDataHolder().getEntry(data);
    }

    @Override
    default boolean has(ThreeData<?> data) {
        return this.getThreeDataHolder().has(data);
    }

    @Override
    default <T> T getDefaultValue(ThreeData<T> data) {
        return this.getThreeDataHolder().getDefaultValue(data);
    }

    @Override
    default ThreeData<?> getDataByName(String name) {
        return this.getThreeDataHolder().getDataByName(name);
    }

    @Override
    default Collection<ThreeDataEntry<?>> getDataEntries() {
        return this.getThreeDataHolder().getDataEntries();
    }
}
