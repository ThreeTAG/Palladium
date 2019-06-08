package com.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class ThreeDataItemStack extends ThreeData<ItemStack>
{

    public ThreeDataItemStack(String key) {
        super(key);
    }

    @Override
    public ItemStack parseValue(JsonObject jsonObject, ItemStack defaultValue) {
        if (!JsonUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return CraftingHelper.getItemStack(JsonUtils.getJsonObject(jsonObject, this.jsonKey), true);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt, ItemStack value) {
        nbt.put(this.key, value.serializeNBT());
    }

    @Override
    public ItemStack readFromNBT(NBTTagCompound nbt, ItemStack defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ItemStack.read(nbt.getCompound(this.key));
    }

    @Override
    public String getDisplay(ItemStack value) {
        return "{ \"item\": \"" + ForgeRegistries.ITEMS.getKey(value.getItem()).toString() + "\", \"count\": " + value.getCount() + " }";
    }
}
