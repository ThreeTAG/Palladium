package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ItemTagThreeData extends ThreeData<Tag<Item>> {

    public ItemTagThreeData(String key) {
        super(key);
    }

    @Override
    public Tag<Item> parseValue(JsonObject jsonObject, Tag<Item> defaultValue) {
        return new ItemTags.Wrapper(new ResourceLocation(JSONUtils.getString(jsonObject, this.jsonKey, defaultValue.getId().toString())));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Tag<Item> value) {
        nbt.putString(this.key, value.getId().toString());
    }

    @Override
    public Tag<Item> readFromNBT(CompoundNBT nbt, Tag<Item> defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return new ItemTags.Wrapper(new ResourceLocation(nbt.getString(this.key)));
    }

    @Override
    public JsonElement serializeJson(Tag<Item> value) {
        return new JsonPrimitive(value.getId().toString());
    }
}
