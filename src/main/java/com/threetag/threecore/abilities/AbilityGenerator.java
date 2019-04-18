package com.threetag.threecore.abilities;

import net.minecraft.nbt.NBTTagCompound;

public class AbilityGenerator {

    public final String key;
    public final AbilityType abilityType;
    public final NBTTagCompound nbtTagCompound;

    public AbilityGenerator(String key, AbilityType abilityType, NBTTagCompound nbtTagCompound) {
        this.key = key;
        this.abilityType = abilityType;
        this.nbtTagCompound = nbtTagCompound;
    }

    public Ability create() {
        Ability ability = this.abilityType.create();
        ability.readUpdateTag(this.nbtTagCompound);
        return ability;
    }

}
