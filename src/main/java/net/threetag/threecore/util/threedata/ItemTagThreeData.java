package net.threetag.threecore.util.threedata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.JSONUtils;

public class ItemTagThreeData extends ThreeData<ITag.INamedTag<Item>> {

    public ItemTagThreeData(String key) {
        super(key);
    }

    @Override
    public ITag.INamedTag<Item> parseValue(JsonObject jsonObject, ITag.INamedTag<Item> defaultValue) {
        return ItemTags.makeWrapperTag(JSONUtils.getString(jsonObject, this.jsonKey, defaultValue.func_230234_a_().toString()));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, ITag.INamedTag<Item> value) {
        nbt.putString(this.key, value.func_230234_a_().toString());
    }

    @Override
    public ITag.INamedTag<Item> readFromNBT(CompoundNBT nbt, ITag.INamedTag<Item> defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        return ItemTags.makeWrapperTag(nbt.getString(this.key));
    }

    @Override
    public JsonElement serializeJson(ITag.INamedTag<Item> value) {
        return new JsonPrimitive(value.func_230234_a_().toString());
    }
}
