package com.threetag.threecore.util.block;

import net.minecraft.nbt.NBTTagCompound;

public interface ITileEntityListener {

    void sync(NBTTagCompound nbt);

}
