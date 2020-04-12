package net.threetag.threecore.util;

import com.google.gson.*;
import net.minecraft.nbt.*;
import net.minecraft.util.JSONUtils;

public class TCJsonUtil {

    public static int[] getIntArray(JsonObject jsonObject, int fields, String key, int... fallback) {
        if (!JSONUtils.hasField(jsonObject, key))
            return fallback;

        JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject, key);

        if (jsonArray.size() != fields)
            throw new JsonParseException("Array " + key + " must have " + fields + " entries!");

        int[] array = new int[fields];

        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = jsonArray.get(i).getAsInt();
        }

        return array;
    }

    public static float[] getFloatArray(JsonObject jsonObject, int fields, String key, float... fallback) {
        if (!JSONUtils.hasField(jsonObject, key))
            return fallback;

        JsonArray jsonArray = JSONUtils.getJsonArray(jsonObject, key);

        if (jsonArray.size() != fields)
            throw new JsonParseException("Array " + key + " must have " + fields + " entries!");

        float[] array = new float[fields];

        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = jsonArray.get(i).getAsFloat();
        }

        return array;
    }

    public static JsonObject merge(JsonObject json1, JsonObject json2) {
        JsonObject json = JSONUtils.fromJson(json1.toString()); // copy

        json2.entrySet().forEach((entry -> {
            if (!json.has(entry.getKey())) {
                json.add(entry.getKey(), entry.getValue());
            } else {
                if (json.get(entry.getKey()).isJsonPrimitive() && entry.getValue().isJsonPrimitive()) {
                    json.add(entry.getKey(), entry.getValue());
                } else if (json.get(entry.getKey()).isJsonArray() && entry.getValue().isJsonArray()) {
                    JsonArray jsonArray = json.get(entry.getKey()).getAsJsonArray();
                    JsonArray json2Array = entry.getValue().getAsJsonArray();
                    for (int i = 0; i < json2Array.size(); i++) {
                        jsonArray.add(json2Array.get(i));
                    }
                    json.add(entry.getKey(), jsonArray);
                } else if (json.get(entry.getKey()).isJsonObject() && entry.getValue().isJsonObject()) {
                    json.add(entry.getKey(), merge(json.get(entry.getKey()).getAsJsonObject(), entry.getValue().getAsJsonObject()));
                }
            }
        }));

        return json;
    }

    public static JsonElement nbtToJson(INBT nbt) {
        if (nbt instanceof NumberNBT) {
            return new JsonPrimitive(((NumberNBT) nbt).getAsNumber());
        } else if (nbt instanceof CollectionNBT) {
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < ((CollectionNBT<?>) nbt).size(); i++) {
                jsonArray.add(nbtToJson(((CollectionNBT<?>) nbt).get(i)));
            }
            return jsonArray;
        } else if (nbt instanceof StringNBT) {
            return new JsonPrimitive(nbt.getString());
        } else if (nbt instanceof CompoundNBT) {
            JsonObject jsonObject = new JsonObject();
            for (String key : ((CompoundNBT) nbt).keySet()) {
                jsonObject.add(key, nbtToJson(((CompoundNBT) nbt).get(key)));
            }
            return jsonObject;
        } else {
            return new JsonObject();
        }
    }

}
