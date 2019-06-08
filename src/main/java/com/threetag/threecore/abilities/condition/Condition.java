package com.threetag.threecore.abilities.condition;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.data.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Condition implements INBTSerializable<NBTTagCompound>, IThreeDataHolder
{
    protected final Ability ability;
    protected final ConditionType type;
    public ThreeDataManager dataManager = new ThreeDataManager(this);

    public static final ThreeData<ITextComponent> NAME = new ThreeDataTextComponent("name").setSyncType(EnumSync.SELF).enableSetting("name", "The display name of the condition.");
    public static final ThreeData<Boolean> ENABLING = new ThreeDataBoolean("enabling").setSyncType(EnumSync.SELF).enableSetting("enabling", "If this condition enables. If false it instead decides whether the ability is unlocked.");
	public static final ThreeData<Boolean> NEEDS_KEY = new ThreeDataBoolean("needs_key").setSyncType(EnumSync.SELF);

    public Condition(ConditionType type, Ability ability){
        this.type = type;
        this.ability = ability;
        this.registerData();
    }

    public void registerData() {
        this.dataManager.register(NAME, new TextComponentTranslation(type.getRegistryName().toString()));
        this.dataManager.register(ENABLING, false);
        this.dataManager.register(NEEDS_KEY, false);
    }

    public abstract boolean test(EntityLivingBase entity);

    public void firstTick(){}
    public void lastTick(){}

    @Override public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.putString("ConditionType", this.type.getRegistryName().toString());
        nbt.put("Data", this.dataManager.serializeNBT());
        return nbt;
    }

    @Override public void deserializeNBT(NBTTagCompound nbt)
    {
        this.dataManager.deserializeNBT(nbt.getCompound("Data"));
    }

    @Override public void sync(EnumSync sync)
    {
        ability.sync(sync);
    }

    @Override public void setDirty()
    {
        ability.setDirty();
    }
}
