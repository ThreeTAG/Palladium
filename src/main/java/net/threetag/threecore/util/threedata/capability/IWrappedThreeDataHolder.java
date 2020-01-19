package net.threetag.threecore.util.threedata.capability;

import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.ThreeData;
import net.threetag.threecore.util.threedata.ThreeDataEntry;

import java.util.Collection;

public interface IWrappedThreeDataHolder extends IThreeDataHolder {

    IThreeDataHolder getThreeDataHolder();

    @Override
    default <T> ThreeData<T> register(ThreeData<T> data, T defaultValue) {
        return this.getThreeDataHolder().register(data, defaultValue);
    }

    @Override
    default <T> void set(ThreeData<T> data, T value) {
        this.getThreeDataHolder().set(data, value);
    }

    @Override
    default <T> void readValue(ThreeData<T> data, CompoundNBT nbt) {
        this.getThreeDataHolder().readValue(data, nbt);
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
