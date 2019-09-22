package net.threetag.threecore.util.threedata.capability;

import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.ThreeData;

public interface IThreeData extends IThreeDataHolder {

    <T> void register(ThreeData<T> data, T defaultValue);

    <T> void setData(ThreeData<T> data, T value);

    <T> T getData(ThreeData<T> data);

}
