package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityMap;
import com.threetag.threecore.abilities.AbilityType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.INBTSerializable;

public class CapabilityAbilityContainer implements IAbilityContainer, INBTSerializable<NBTTagCompound> {

    @CapabilityInject(IAbilityContainer.class)
    public static Capability<IAbilityContainer> ABILITY_CONTAINER;

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
        this.abilityMap = new AbilityMap();
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
}
