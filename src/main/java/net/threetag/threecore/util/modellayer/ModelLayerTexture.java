package net.threetag.threecore.util.modellayer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ModelLayerTexture {

    public final ResourceLocation[] frames;
    public final int delay;

    public ModelLayerTexture(String texture) {
        this(texture, 1, 1);
    }

    public ModelLayerTexture(String texture, int frameAmount, int delay) {
        this.frames = new ResourceLocation[frameAmount];
        this.delay = delay;

        for (int i = 0; i < frameAmount; i++) {
            this.frames[i] = new ResourceLocation(String.format(texture, i));
        }
    }

    public ModelLayerTexture(int delay, ResourceLocation... frames) {
        this.frames = frames;
        this.delay = delay;
    }

    public ResourceLocation getTexture() {
        return this.frames[0];
    }

    public ResourceLocation getTexture(int frame) {
        return this.frames[(frame / this.delay) % this.frames.length];
    }

    public boolean isAnimated() {
        return this.frames.length > 1;
    }

    public int getAnimationTime() {
        return this.frames.length * this.delay;
    }

    public static ModelLayerTexture fromJson(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new ModelLayerTexture(jsonElement.getAsString());
        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            ResourceLocation[] frames = new ResourceLocation[array.size()];
            for (int i = 0; i < array.size(); i++) {
                frames[i] = new ResourceLocation(array.get(i).getAsString());
            }
            return new ModelLayerTexture(1, frames);
        } else {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (JSONUtils.hasField(jsonObject, "texture"))
                return new ModelLayerTexture(JSONUtils.getString(jsonObject, "texture"), JSONUtils.getInt(jsonObject, "frames", 1), JSONUtils.getInt(jsonObject, "delay", 1));
            else {
                JsonArray array = JSONUtils.getJsonArray(jsonObject, "textures");
                ResourceLocation[] frames = new ResourceLocation[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    frames[i] = new ResourceLocation(array.get(i).getAsString());
                }
                return new ModelLayerTexture(JSONUtils.getInt(jsonObject, "delay", 1), frames);
            }
        }
    }

}
