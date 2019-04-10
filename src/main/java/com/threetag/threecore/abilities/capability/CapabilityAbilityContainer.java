package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityMap;
import com.threetag.threecore.abilities.AbilityType;
import com.threetag.threecore.abilities.IAbilityContainer;
import com.threetag.threecore.abilities.data.EnumSync;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityAbilityContainer implements IAbilityContainer, INBTSerializable<NBTTagCompound> {

    @CapabilityInject(IAbilityContainer.class)
    public static Capability<IAbilityContainer> ABILITY_CONTAINER;
    public static final ResourceLocation ID = new ResourceLocation(ThreeCore.MODID, "entity");

    protected AbilityMap abilityMap;

    public CapabilityAbilityContainer() {
        this.abilityMap = new AbilityMap();
    }

    @Override
    public AbilityMap getAbilityMap() {
        return this.abilityMap;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.getAbilityMap().forEach((s, a) -> nbt.put(s, a.serializeNBT()));
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.abilityMap.clear();
        nbt.keySet().forEach((s) -> {
            NBTTagCompound tag = nbt.getCompound(s);
            AbilityType abilityType = AbilityType.REGISTRY.getValue(new ResourceLocation(tag.getString("AbilityType")));
            if (abilityType != null) {
                Ability ability = abilityType.create();
                ability.deserializeNBT(tag);
                this.abilityMap.put(s, ability);
            }
        });
    }

    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.getAbilityMap().forEach((s, a) -> nbt.put(s, a.getUpdateTag()));
        return nbt;
    }

    public void readUpdateTag(NBTTagCompound nbt) {
        this.abilityMap.clear();
        nbt.keySet().forEach((s) -> {
            NBTTagCompound tag = nbt.getCompound(s);
            AbilityType abilityType = AbilityType.REGISTRY.getValue(new ResourceLocation(tag.getString("AbilityType")));
            if (abilityType != null) {
                Ability ability = abilityType.create();
                ability.readUpdateTag(tag);
                this.abilityMap.put(s, ability);
            }
        });
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }
}
