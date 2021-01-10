package net.threetag.threecore.scripts.bindings;

import net.minecraft.nbt.CompoundNBT;
import net.threetag.threecore.scripts.accessors.CompoundNBTAccessor;

public class CompoundNBTBuilder {
    public CompoundNBTAccessor create(){
        return new CompoundNBTAccessor(new CompoundNBT());
    }
}