package net.threetag.palladium.recipe.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface IRecipeConditionSerializer<T extends IRecipeCondition> {

    void write(JsonObject json, T value);

    T read(JsonObject json);

    ResourceLocation getID();

    default JsonObject getJson(T value) {
        JsonObject json = new JsonObject();
        this.write(json, value);
        json.addProperty("type", value.getID().toString());
        return json;
    }

}
