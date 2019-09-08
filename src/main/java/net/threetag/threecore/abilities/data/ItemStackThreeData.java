package net.threetag.threecore.abilities.data;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Created by Nictogen on 2019-02-15.
 */
public class ItemStackThreeData extends ThreeData<ItemStack> {

    public ItemStackThreeData(String key) {
        super(key);
    }

    @Override
    public ItemStack parseValue(JsonObject jsonObject, ItemStack defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return CraftingHelper.getItemStack(JSONUtils.getJsonObject(jsonObject, this.jsonKey), true);
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, ItemStack value) {
        nbt.put(this.key, value.serializeNBT());
    }

    @Override
    public ItemStack readFromNBT(CompoundNBT nbt, ItemStack defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ItemStack.read(nbt.getCompound(this.key));
    }

    @Override
    public String getDisplay(ItemStack value) {
        return "{ \"item\": \"" + ForgeRegistries.ITEMS.getKey(value.getItem()).toString() + "\", \"count\": " + value.getCount() + " }";
    }
}
