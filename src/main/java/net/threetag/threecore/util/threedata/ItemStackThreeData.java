package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
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
    public JsonElement serializeJson(ItemStack value) {
        JsonObject object = new JsonObject();
        object.addProperty("item", ForgeRegistries.ITEMS.getKey(value.getItem()).toString());
        object.addProperty("count", value.getCount());
        return object;
    }
}
