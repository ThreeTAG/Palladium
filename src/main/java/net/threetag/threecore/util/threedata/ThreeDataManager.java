package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class ThreeDataManager implements INBTSerializable<CompoundNBT>, IThreeDataHolder {

    private Listener listener;
    protected Map<ThreeData<?>, ThreeDataEntry<?>> dataEntryList = new LinkedHashMap<>();
    protected Map<ThreeData<?>, Object> dataEntryDefaults = new LinkedHashMap<>();

    public ThreeDataManager setListener(Listener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public <T> ThreeDataManager register(ThreeData<T> data, T defaultValue) {
        dataEntryList.put(data, new ThreeDataEntry<T>(data, defaultValue));
        dataEntryDefaults.put(data, defaultValue);
        return this;
    }

    @Override
    public <T> ThreeDataManager set(ThreeData<T> data, T value) {
        ThreeDataEntry<T> entry = getEntry(data);

        if (entry != null && !entry.getValue().equals(value)) {
            T oldValue = entry.getValue();
            entry.setValue(value);
            if (this.listener != null)
                this.listener.dataChanged(data, oldValue, value);
        }

        return this;
    }

    @Override
    public <T> T readValue(ThreeData<T> data, CompoundNBT nbt) {
        ThreeDataEntry<T> entry = getEntry(data);

        if (entry != null) {
            T oldValue = entry.getValue();
            T newValue = data.readFromNBT(nbt, (T) this.dataEntryDefaults.get(data));

            if (!oldValue.equals(newValue)) {
                entry.setValue(data.readFromNBT(nbt, newValue));
                if (this.listener != null)
                    this.listener.dataChanged(data, oldValue, newValue);

                return newValue;
            }
        }

        return null;
    }

    @Override
    public <T> T get(ThreeData<T> data) {
        ThreeDataEntry<T> entry = getEntry(data);
        return entry == null ? null : entry.getValue();
    }

    @Override
    public <T> ThreeDataEntry<T> getEntry(ThreeData<T> data) {
        return (ThreeDataEntry<T>) dataEntryList.get(data);
    }

    @Override
    public boolean has(ThreeData data) {
        return dataEntryList.containsKey(data);
    }

    @Override
    public <T> T getDefaultValue(ThreeData<T> data) {
        return (T) this.dataEntryDefaults.get(data);
    }

    @Override
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

    @Override
    public Collection<ThreeDataEntry<?>> getDataEntries() {
        return this.dataEntryList.values();
    }

    @Override
    public ThreeData<?> getDataByName(String name) {
        for (ThreeData<?> data : getData()) {
            if (data.key.equals(name)) {
                return data;
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

    public interface Listener {

        <T> void dataChanged(ThreeData<T> data, T oldValue, T value);

    }

}
