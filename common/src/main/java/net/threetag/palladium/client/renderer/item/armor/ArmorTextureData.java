package net.threetag.palladium.client.renderer.item.armor;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.client.dynamictexture.DynamicTexture;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.context.DataContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ArmorTextureData {

    private final Map<String, SkinTypedValue<DynamicTexture>> textureByKey = new HashMap<>();

    public ArmorTextureData add(String key, SkinTypedValue<DynamicTexture> texture) {
        this.textureByKey.put(key, texture);
        return this;
    }

    @NotNull
    public ResourceLocation get(String key, DataContext context) {
        var data = this.textureByKey.get(key);

        if (data == null) {
            throw new IllegalStateException("Unknown texture key for armor: #" + key);
        }

        return data.get(context.getEntity()).getTexture(context);
    }

    public boolean has(String key) {
        return this.textureByKey.containsKey(key);
    }

    public static ArmorTextureData fromJson(JsonElement json) {
        var data = new ArmorTextureData();

        // If primitive, or if it's specifically skin-typed
        if (json.isJsonPrimitive() || (json.isJsonObject() && json.getAsJsonObject().entrySet().size() == 2 && GsonHelper.isValidNode(json.getAsJsonObject(), "normal") && GsonHelper.isValidNode(json.getAsJsonObject(), "slim"))) {
            var texture = SkinTypedValue.fromJSON(json, DynamicTexture::parse);
            data.add(ArmorRendererConditions.BASE_TEXTURE, texture);
        } else if (json.isJsonObject()) {
            var object = GsonHelper.convertToJsonObject(json, "textures");

            for (Map.Entry<String, JsonElement> e : object.entrySet()) {
                var key = e.getKey();
                var textureJson = e.getValue();
                data.add(key, SkinTypedValue.fromJSON(textureJson, DynamicTexture::parse));
            }
        } else {
            throw new JsonParseException("Textures must be json primitive or json object");
        }

        return data;
    }

}
