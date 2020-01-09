package net.threetag.threecore.util.threedata;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;

public class IngredientThreeData extends ThreeData<Ingredient> {

    public IngredientThreeData(String key) {
        super(key);
    }

    @Override
    public Ingredient parseValue(JsonObject jsonObject, Ingredient defaultValue) {
        if (!JSONUtils.hasField(jsonObject, this.jsonKey))
            return defaultValue;
        return Ingredient.deserialize(jsonObject.get(this.jsonKey));
    }

    @Override
    public void writeToNBT(CompoundNBT nbt, Ingredient value) {
        if (value != Ingredient.EMPTY && value.getMatchingStacks().length > 0) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("Ingredient", value.serialize());
            nbt.putString(this.key, jsonObject.toString());
        }
    }

    @Override
    public Ingredient readFromNBT(CompoundNBT nbt, Ingredient defaultValue) {
        if (!nbt.contains(this.key))
            return defaultValue;
        try {
            return Ingredient.deserialize(JSONUtils.fromJson(nbt.getString(this.key)).get("Ingredient"));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    @Override
    public String getDisplay(Ingredient value) {
        return value.serialize().toString();
    }
}
