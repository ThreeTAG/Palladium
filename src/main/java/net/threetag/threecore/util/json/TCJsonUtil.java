package net.threetag.threecore.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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

}
