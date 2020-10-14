package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class ItemThreeData extends ThreeData<Item> {

    public ItemThreeData(String key) {
        super(key);
    }

    @Override
    public Item parseValue(JsonObject jsonObject, Item defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(jsonObject, this.jsonKey)));
        if (item == null)
            throw new JsonSyntaxException("Item " + JSONUtils.getString(jsonObject, this.jsonKey) + " does not exist!");
        return item;
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Item value) {
        nbt.putString(this.key, Objects.requireNonNull(value.getRegistryName()).toString());
    }

    @Override
    public Item readFromNBT(CompoundNBT nbt, Item defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString(this.key)));
    }

    @Override
    public JsonElement serializeJson(Item value) {
        return new JsonPrimitive(ForgeRegistries.ITEMS.getKey(value).toString());
    }
}
