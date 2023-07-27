package net.threetag.palladium.client.renderer.item.armor;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.renderlayer.ModelLookup;
import net.threetag.palladium.util.SkinTypedValue;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ArmorModelData {

    private final Map<String, SkinTypedValue<ModelLayerLocation>> modelByKey = new HashMap<>();
    private final Map<String, SkinTypedValue<HumanoidModel<?>>> generatedByKey = new HashMap<>();

    public ArmorModelData add(String key, SkinTypedValue<ModelLayerLocation> texture) {
        this.modelByKey.put(key, texture);
        return this;
    }

    @Nullable
    public HumanoidModel<?> get(String key, LivingEntity entity, boolean hasConditions) {
        if (!hasConditions) {
            var fallback = this.generatedByKey.get(ArmorRendererConditions.BASE_TEXTURE);
            return fallback != null ? fallback.get(entity) : null;
        }

        var data = this.generatedByKey.get(key);

        if (data == null) {
            throw new IllegalStateException("Unknown model key for armor: #" + key);
        }

        return data.get(entity);
    }

    public void buildModels(ModelLookup.Model modelType, EntityModelSet modelSet) {
        this.modelByKey.forEach((key, layer) -> {
            HumanoidModel<?> normal = (HumanoidModel<?>) modelType.getModel(modelSet.bakeLayer(layer.getNormal()));

            if (layer.getNormal().equals(layer.getSlim())) {
                this.generatedByKey.put(key, new SkinTypedValue<>(normal));
            } else {
                HumanoidModel<?> slim = (HumanoidModel<?>) modelType.getModel(modelSet.bakeLayer(layer.getSlim()));
                this.generatedByKey.put(key, new SkinTypedValue<>(normal, slim));
            }

        });
    }

    public static ArmorModelData fromJson(JsonElement json) {
        var data = new ArmorModelData();

        if (json == null) {
            return data;
        }

        // If primitive, or if it's specifically skin-typed
        if (json.isJsonPrimitive() || (json.isJsonObject() && json.getAsJsonObject().entrySet().size() == 2 && GsonHelper.isValidNode(json.getAsJsonObject(), "normal") && GsonHelper.isValidNode(json.getAsJsonObject(), "slim"))) {
            var texture = SkinTypedValue.fromJSON(json, j -> GsonUtil.convertToModelLayerLocation(j, "model_layers"));
            data.add(ArmorRendererConditions.BASE_TEXTURE, texture);
        } else if (json.isJsonObject()) {
            var object = GsonHelper.convertToJsonObject(json, "model_layers");

            for (Map.Entry<String, JsonElement> e : object.entrySet()) {
                var key = e.getKey();
                var textureJson = e.getValue();
                data.add(key, SkinTypedValue.fromJSON(textureJson, j -> GsonUtil.convertToModelLayerLocation(j, "model_layers")));
            }
        } else {
            throw new JsonParseException("Model layers must be json primitive or json object");
        }

        return data;
    }

}
