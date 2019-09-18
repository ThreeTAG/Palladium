package net.threetag.threecore.util.threedata;

import net.minecraft.nbt.CompoundNBT;

/**
 * Created by Nictogen on 2019-06-08.
 */
public interface IThreeDataHolder {

    default <T> void update(ThreeData<T> data, T value) {
        setDirty();
    }

    default void setData(String dataKey, CompoundNBT dataTag) {

    }

    void setDirty();

}
