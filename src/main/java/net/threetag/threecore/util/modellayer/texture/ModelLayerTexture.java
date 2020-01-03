package net.threetag.threecore.util.modellayer.texture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.modellayer.IModelLayerContext;
import net.threetag.threecore.util.modellayer.ModelLayerManager;
import net.threetag.threecore.util.modellayer.texture.transformer.ITextureTransformer;

public abstract class ModelLayerTexture {

    public abstract ResourceLocation getTexture(IModelLayerContext context);

    public abstract ModelLayerTexture transform(ITextureTransformer textureTransformer);

    public static ModelLayerTexture parse(JsonElement jsonElement) {
        if (jsonElement.isJsonPrimitive()) {
            return new DefaultModelTexture(jsonElement.getAsString(), null);
        } else if (jsonElement.isJsonObject()) {
            JsonObject json = jsonElement.getAsJsonObject();
            String type = JSONUtils.getString(json, "type", ThreeCore.MODID + ":default");
            ModelLayerTexture texture = ModelLayerManager.parseTexture(json);

            if (texture == null) {
                ThreeCore.LOGGER.warn("Model layer type '" + type + "' does not exist!");
                return null;
            }

            if (JSONUtils.hasField(json, "transformers")) {
                JsonArray transformers = JSONUtils.getJsonArray(json, "transformers");
                for (int i = 0; i < transformers.size(); i++) {
                    JsonObject transformerJson = transformers.get(i).getAsJsonObject();
                    ITextureTransformer transformer = ModelLayerManager.parseTextureTransformer(transformerJson);
                    if (transformer != null) {
                        texture.transform(transformer);
                    } else {
                        ThreeCore.LOGGER.warn("Texture transformer type '" + JSONUtils.getString(transformerJson, "type") + "' does not exist!");
                    }
                }
            }

            return texture;
        }

        return null;
    }

}
