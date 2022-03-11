package net.threetag.palladium.util;

import com.google.gson.*;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.core.Registry;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class PalladiumGsonHelper {

    public static int[] getIntArray(JsonObject jsonObject, int fields, String key) {
        if (!GsonHelper.isValidNode(jsonObject, key))
            throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonArray");

        JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, key);

        if (jsonArray.size() != fields)
            throw new JsonParseException("Array " + key + " must have " + fields + " entries!");

        int[] array = new int[fields];

        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = jsonArray.get(i).getAsInt();
        }

        return array;
    }

    public static int[] getIntArray(JsonObject jsonObject, int fields, String key, int... fallback) {
        if (!GsonHelper.isValidNode(jsonObject, key))
            return fallback;
        return getIntArray(jsonObject, fields, key);
    }

    public static float[] getFloatArray(JsonObject jsonObject, int fields, String key) {
        if (!GsonHelper.isValidNode(jsonObject, key))
            throw new JsonSyntaxException("Missing " + key + ", expected to find a JsonArray");

        JsonArray jsonArray = GsonHelper.getAsJsonArray(jsonObject, key);

        if (jsonArray.size() != fields)
            throw new JsonParseException("Array " + key + " must have " + fields + " entries!");

        float[] array = new float[fields];

        for (int i = 0; i < jsonArray.size(); i++) {
            array[i] = jsonArray.get(i).getAsFloat();
        }

        return array;
    }

    public static float[] getFloatArray(JsonObject jsonObject, int fields, String key, float... fallback) {
        if (!GsonHelper.isValidNode(jsonObject, key))
            return fallback;
        return getFloatArray(jsonObject, fields, key);
    }

    public static ResourceLocation getAsResourceLocation(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return new ResourceLocation(GsonHelper.getAsString(json, memberName));
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find a resource location");
        }
    }

    public static ResourceLocation getAsResourceLocation(JsonObject json, String memberName, @Nullable ResourceLocation fallback) {
        return json.has(memberName) ? getAsResourceLocation(json, memberName) : fallback;
    }

    public static ModelLayerLocation getAsModelLayerLocation(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            String[] s = GsonHelper.getAsString(json, memberName).split("#", 2);

            if (s.length == 1) {
                return new ModelLayerLocation(new ResourceLocation(s[0]), "main");
            } else {
                return new ModelLayerLocation(new ResourceLocation(s[0]), s[1]);
            }
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find a model location");
        }
    }

    public static ModelLayerLocation getAsModelLayerLocation(JsonObject json, String memberName, @Nullable ModelLayerLocation fallback) {
        return json.has(memberName) ? getAsModelLayerLocation(json, memberName) : fallback;
    }

    public static JsonObject merge(JsonObject json1, JsonObject json2) {
        JsonObject json = GsonHelper.parse(json1.toString()); // copy

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

    public static JsonElement nbtToJson(Tag nbt) {
        if (nbt instanceof NumericTag) {
            return new JsonPrimitive(((NumericTag) nbt).getAsNumber());
        } else if (nbt instanceof CollectionTag) {
            JsonArray jsonArray = new JsonArray();
            for (int i = 0; i < ((CollectionTag<?>) nbt).size(); i++) {
                jsonArray.add(nbtToJson(((CollectionTag<?>) nbt).get(i)));
            }
            return jsonArray;
        } else if (nbt instanceof StringTag) {
            return new JsonPrimitive(nbt.getAsString());
        } else if (nbt instanceof CompoundTag) {
            JsonObject jsonObject = new JsonObject();
            for (String key : ((CompoundTag) nbt).getAllKeys()) {
                jsonObject.add(key, nbtToJson(((CompoundTag) nbt).get(key)));
            }
            return jsonObject;
        } else {
            return new JsonObject();
        }
    }

    public static JsonObject serializeItemStack(ItemStack stack) {
        return serializeItemStack(stack, true);
    }

    public static JsonObject serializeItemStack(ItemStack stack, boolean writeNbt) {
        JsonObject json = new JsonObject();

        json.addProperty("item", Registry.ITEM.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());

        if (writeNbt && stack.hasTag()) {
            json.add("nbt", nbtToJson(stack.getTag()));
        }

        return json;
    }

    public static ItemStack readItemStack(JsonObject json) {
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "item"));

        if (!Registry.ITEM.containsKey(id)) {
            throw new JsonParseException("Unknown item '" + id + "'");
        }

        Item item = Registry.ITEM.get(id);

        return new ItemStack(item, GsonHelper.getAsInt(json, "count", 1));
    }

}
