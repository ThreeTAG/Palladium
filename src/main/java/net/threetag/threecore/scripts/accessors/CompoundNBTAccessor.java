package net.threetag.threecore.scripts.accessors;

import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.scripts.ScriptParameterName;

import java.util.UUID;

public class CompoundNBTAccessor extends ScriptAccessor<CompoundNBT> {

    protected CompoundNBTAccessor(CompoundNBT value) {
        super(value);
    }

    public CompoundNBTAccessor putByte(@ScriptParameterName("key") String key, @ScriptParameterName("value") byte value) {
        this.value.putByte(key, value);
        return this;
    }

    public CompoundNBTAccessor putShort(@ScriptParameterName("key") String key, @ScriptParameterName("value") short value) {
        this.value.putShort(key, value);
        return this;
    }

    public CompoundNBTAccessor putInt(@ScriptParameterName("key") String key, @ScriptParameterName("value") int value) {
        this.value.putInt(key, value);
        return this;
    }

    public CompoundNBTAccessor putLong(@ScriptParameterName("key") String key, @ScriptParameterName("value") long value) {
        this.value.putLong(key, value);
        return this;
    }

    public CompoundNBTAccessor putUniqueId(@ScriptParameterName("key") String key, @ScriptParameterName("value") String value) {
        this.value.putUniqueId(key, UUID.fromString(value));
        return this;
    }

    public CompoundNBTAccessor putFloat(@ScriptParameterName("key") String key, @ScriptParameterName("value") float value) {
        this.value.putFloat(key, value);
        return this;
    }

    public CompoundNBTAccessor putDouble(@ScriptParameterName("key") String key, @ScriptParameterName("value") double value) {
        this.value.putDouble(key, value);
        return this;
    }

    public CompoundNBTAccessor putString(@ScriptParameterName("key") String key, @ScriptParameterName("value") String value) {
        this.value.putString(key, value);
        return this;
    }

    public CompoundNBTAccessor putByteArray(@ScriptParameterName("key") String key, @ScriptParameterName("value") byte[] value) {
        this.value.putByteArray(key, value);
        return this;
    }

    public CompoundNBTAccessor putIntArray(@ScriptParameterName("key") String key, @ScriptParameterName("value") int[] value) {
        this.value.putIntArray(key, value);
        return this;
    }

    public CompoundNBTAccessor putLongArray(@ScriptParameterName("key") String key, @ScriptParameterName("value") long[] value) {
        this.value.putLongArray(key, value);
        return this;
    }

    public byte getTagId(@ScriptParameterName("key") String key) {
        return this.value.getTagId(key);
    }

    public boolean contains(@ScriptParameterName("key") String key) {
        return this.value.contains(key);
    }

    public boolean contains(@ScriptParameterName("key") String key, @ScriptParameterName("type") int type) {
        return this.value.contains(key, type);
    }

    public byte getByte(@ScriptParameterName("key") String key) {
        return this.value.getByte(key);
    }

    public short getShort(@ScriptParameterName("key") String key) {
        return this.value.getShort(key);
    }

    public int getInt(@ScriptParameterName("key") String key) {
        return this.value.getInt(key);
    }

    public long getLong(@ScriptParameterName("key") String key) {
        return this.value.getLong(key);
    }

    public float getFloat(@ScriptParameterName("key") String key) {
        return this.value.getFloat(key);
    }

    public double getDouble(@ScriptParameterName("key") String key) {
        return this.value.getDouble(key);
    }

    public String getString(@ScriptParameterName("key") String key) {
        return this.value.getString(key);
    }

    public byte[] getByteArray(@ScriptParameterName("key") String key) {
        return this.value.getByteArray(key);
    }

    public int[] getIntArray(@ScriptParameterName("key") String key) {
        return this.value.getIntArray(key);
    }

    public long[] getLongArray(@ScriptParameterName("key") String key) {
        return this.value.getLongArray(key);
    }

    public CompoundNBTAccessor getCompound(@ScriptParameterName("key") String key) {
        return new CompoundNBTAccessor(this.value.getCompound(key));
    }

    public boolean getBoolean(@ScriptParameterName("key") String key) {
        return this.value.getBoolean(key);
    }

    public String getUniqueId(@ScriptParameterName("key") String key) {
        return this.value.getUniqueId(key).toString();
    }

    public CompoundNBTAccessor remove(@ScriptParameterName("key") String key) {
        this.value.remove(key);
        return this;
    }

    public boolean isEmpty() {
        return this.value.isEmpty();
    }

    public CompoundNBTAccessor copy() {
        return new CompoundNBTAccessor(this.value.copy());
    }

    public CompoundNBTAccessor merge(@ScriptParameterName("otherTag") CompoundNBTAccessor otherTag) {
        return new CompoundNBTAccessor(this.value.merge(otherTag.value));
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
