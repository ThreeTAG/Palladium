package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class ThreeDataManager implements INBTSerializable<CompoundNBT> {

    public final IThreeDataHolder dataHolder;
    protected Map<ThreeData<?>, ThreeDataEntry<?>> dataEntryList = new LinkedHashMap<>();
    protected Map<ThreeData<?>, Object> dataEntryDefaults = new LinkedHashMap<>();

    public ThreeDataManager(IThreeDataHolder dataHolder) {
        this.dataHolder = dataHolder;
    }

    public <T> ThreeData<T> register(ThreeData<T> data, T defaultValue) {
        dataEntryList.put(data, new ThreeDataEntry<T>(data, defaultValue));
        dataEntryDefaults.put(data, defaultValue);
        return data;
    }

    public <T> void set(ThreeData<T> data, T value) {
        ThreeDataEntry entry = getEntry(data);

        if (entry != null && !entry.getValue().equals(value)) {
            entry.setValue(value);
            this.dataHolder.sync(data.syncType);
            this.dataHolder.setDirty();
        }
    }

    public <T> T get(ThreeData<T> data) {
        ThreeDataEntry entry = getEntry(data);
        return entry == null ? null : (T) entry.getValue();
    }

    public <T> ThreeDataEntry<T> getEntry(ThreeData<T> data) {
        return (ThreeDataEntry<T>) dataEntryList.get(data);
    }

    public boolean has(ThreeData data) {
        return dataEntryList.containsKey(data);
    }

    public <T> T getDefaultValue(ThreeData<T> data) {
        return (T) this.dataEntryDefaults.get(data);
    }

    public <T> ThreeDataManager reset(ThreeData<T> data) {
        this.set(data, getDefaultValue(data));
        return this;
    }

    public Set<ThreeData<?>> getData() {
        return this.dataEntryList.keySet();
    }

    public List<ThreeData<?>> getSettingData() {
        List<ThreeData<?>> list = new ArrayList<>();
        for (ThreeData<?> data : this.getData()) {
            if (data.isUserSetting()) {
                list.add(data);
            }
        }
        return list;
    }

    public Collection<ThreeDataEntry<?>> getDataEntries() {
        return this.dataEntryList.values();
    }

    public ThreeData<?> getAbilityDataByName(String name) {
        for (ThreeData<?> datas : getSettingData()) {
            if (datas.key.equals(name)) {
                return datas;
            }
        }
        return null;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        for (ThreeData data : dataEntryList.keySet()) {
            if (data.canBeSaved())
                data.writeToNBT(nbt, getEntry(data).getValue());
        }
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        for (ThreeData data : dataEntryList.keySet()) {
            if (data.canBeSaved())
                getEntry(data).setValue(data.readFromNBT(nbt, getDefaultValue(data)));
        }
    }

    public CompoundNBT getUpdatePacket() {
        CompoundNBT nbt = new CompoundNBT();
        for (ThreeData data : dataEntryList.keySet()) {
            data.writeToNBT(nbt, getEntry(data).getValue());
        }
        return nbt;
    }

    public void readUpdatePacket(CompoundNBT nbt) {
        for (ThreeData data : dataEntryList.keySet()) {
            getEntry(data).setValue(data.readFromNBT(nbt, getDefaultValue(data)));
        }
    }

    public void readFromJson(JsonObject jsonObject) {
        for (ThreeData data : dataEntryList.keySet()) {
            getEntry(data).setValue(data.parseValue(jsonObject, getDefaultValue(data)));
        }
    }

}
