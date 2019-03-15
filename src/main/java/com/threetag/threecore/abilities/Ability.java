package com.threetag.threecore.abilities;

import com.threetag.threecore.abilities.data.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class Ability implements INBTSerializable<NBTTagCompound> {

    public static final AbilityData<Boolean> ENABLED = new AbilityDataBoolean("enabled").disableSaving();
    public static final AbilityData<Integer> MAX_COOLDOWN = new AbilityDataInteger("max_cooldown").disableSaving().setSyncType(EnumSync.SELF).enableSetting("cooldown", "Maximum cooldown for using this ability");
    public static final AbilityData<Integer> COOLDOWN = new AbilityDataInteger("cooldown").setSyncType(EnumSync.SELF);
    public static final AbilityData<Boolean> SHOW_IN_BAR = new AbilityDataBoolean("show_in_bar").disableSaving().setSyncType(EnumSync.SELF).enableSetting("show_in_bar", "Determines if this ability should be displayed in the ability bar");
    public static final AbilityData<Boolean> HIDDEN = new AbilityDataBoolean("hidden").setSyncType(EnumSync.SELF);
    public static final AbilityData<ITextComponent> TITLE = new AbilityDataTextComponent("title").disableSaving().setSyncType(EnumSync.SELF).enableSetting("title", "Allows you to set a custom title for this ability");
    public static final AbilityData<AbilityDataIcon.Icon> ICON = new AbilityDataIcon("icon").disableSaving().setSyncType(EnumSync.SELF).enableSetting("icon", "Lets you customize the icon for the ability");

    protected final AbilityType type;
    String id;
    protected AbilityDataManager dataManager = new AbilityDataManager(this);
    protected int ticks = 0;
    public EnumSync sync = EnumSync.NONE;
    public boolean dirty = false;

    public Ability(AbilityType type) {
        this.type = type;

        registerData();
    }

    public abstract EnumAbilityType getAbilityType();

    public void registerData() {
        this.dataManager.register(ENABLED, false);
        if (this.getAbilityType() != EnumAbilityType.CONSTANT) {
            this.dataManager.register(MAX_COOLDOWN, 0);
            this.dataManager.register(COOLDOWN, 0);
        }
        this.dataManager.register(SHOW_IN_BAR, getAbilityType() != EnumAbilityType.CONSTANT);
        this.dataManager.register(HIDDEN, false);
        this.dataManager.register(TITLE, new TextComponentTranslation("ability." + this.type.getRegistryName().getNamespace() + "." + this.type.getRegistryName().getPath()));
        this.dataManager.register(ICON, new AbilityDataIcon.Icon(true));
    }

    public void tick(EntityLivingBase entity) {

    }

    // TODO Ability Conditions
    public boolean isUnlocked() {
        return true;
    }

    public AbilityDataManager getDataManager() {
        return dataManager;
    }

    public final String getId() {
        return id;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.putString("AbilityType", this.type.getRegistryName().toString());
        nbt.put("Data", this.dataManager.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.dataManager.deserializeNBT(nbt.getCompound("Data"));
    }
}
