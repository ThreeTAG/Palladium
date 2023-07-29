package net.threetag.palladium.client.renderer.item.armor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.client.renderer.renderlayer.IPackRenderLayer;
import net.threetag.palladium.client.renderer.renderlayer.ModelLookup;
import net.threetag.palladium.client.renderer.renderlayer.PackRenderLayerManager;
import net.threetag.palladium.condition.Condition;
import net.threetag.palladium.condition.ConditionEnvironment;
import net.threetag.palladium.condition.ConditionSerializer;
import net.threetag.palladium.condition.FalseCondition;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArmorRendererData {

    private final ModelLookup.Model modelType;
    private final ArmorTextureData textures;
    private final ArmorModelData models;
    private final ArmorRendererConditions conditions;
    private final List<Condition> hideSecondLayer;
    private final List<IPackRenderLayer> renderLayers;

    public ArmorRendererData(ModelLookup.Model modelType, ArmorTextureData textures, ArmorModelData models, ArmorRendererConditions conditions) {
        this.modelType = modelType;
        this.textures = textures;
        this.models = models;
        this.conditions = conditions;
        this.hideSecondLayer = List.of(new FalseCondition());
        this.renderLayers = Collections.emptyList();
    }

    public ArmorRendererData(ModelLookup.Model modelType, ArmorTextureData textures, ArmorModelData models, ArmorRendererConditions conditions, List<Condition> hideSecondLayer, List<IPackRenderLayer> renderLayers) {
        this.modelType = modelType;
        this.textures = textures;
        this.models = models;
        this.conditions = conditions;
        this.hideSecondLayer = hideSecondLayer;
        this.renderLayers = renderLayers;
    }

    public static ArmorRendererData fromJson(JsonObject json) {
        var modelType = ModelLookup.get(GsonUtil.getAsResourceLocation(json, "model_type", new ResourceLocation("humanoid")));
        var textures = ArmorTextureData.fromJson(json.get("textures"));
        var modelLayers = ArmorModelData.fromJson(json.get("model_layers"));
        var conditions = ArmorRendererConditions.fromJson(json.has("conditions") ? GsonHelper.getAsJsonArray(json, "conditions") : null);
        List<Condition> hideSecondLayer = json.has("hide_second_layer") ? ConditionSerializer.listFromJSON(json.get("hide_second_layer"), ConditionEnvironment.ASSETS) : List.of(new FalseCondition());
        List<IPackRenderLayer> renderLayers = json.has("render_layers") ? parseRenderLayers(json.get("render_layers")) : Collections.emptyList();
        return new ArmorRendererData(modelType, textures, modelLayers, conditions, hideSecondLayer, renderLayers);
    }

    public static List<IPackRenderLayer> parseRenderLayers(JsonElement jsonElement) {
        List<IPackRenderLayer> layers = new ArrayList<>();

        if(jsonElement.isJsonPrimitive()) {
            var layerId = GsonUtil.convertToResourceLocation(jsonElement, "render_layers");
            var layer = PackRenderLayerManager.getInstance().getLayer(layerId);

            if(layer != null) {
                layers.add(layer);
            } else {
                AddonPackLog.warning("Unknown render layer '" + layerId + "'");
            }
        } else if(jsonElement.isJsonArray()) {
            var array = GsonHelper.convertToJsonArray(jsonElement, "render_layers");

            for (JsonElement element : array) {
                layers.addAll(parseRenderLayers(element));
            }
        } else if(jsonElement.isJsonObject()) {
            layers.add(PackRenderLayerManager.parseLayer(jsonElement.getAsJsonObject()));
        }

        return layers;
    }

    public void buildModels(EntityModelSet modelSet) {
        this.models.buildModels(this.modelType, modelSet);
    }

    @NotNull
    public ResourceLocation getTexture(DataContext context) {
        try {
            String key = this.conditions.getTexture(context, this.textures);
            return this.textures.get(key, context);
        } catch (Exception e) {
            Palladium.LOGGER.error("Error while rendering armor: " + e.getMessage());
            return TextureManager.INTENTIONAL_MISSING_TEXTURE;
        }
    }

    @NotNull
    public ResourceLocation getTexture(DataContext context, String key) {
        return this.textures.get(key, context);
    }

    @Nullable
    public HumanoidModel<?> getModel(LivingEntity entity, DataContext context) {
        String key = this.conditions.getModelLayer(context, this.models);
        return this.models.get(key, entity, !this.conditions.conditions.isEmpty());
    }

    public ArmorTextureData getTextures() {
        return textures;
    }

    public ArmorModelData getModels() {
        return models;
    }

    public List<IPackRenderLayer> getRenderLayers() {
        return renderLayers;
    }

    public boolean hidesSecondPlayerLayer(DataContext context) {
        return ConditionSerializer.checkConditions(this.hideSecondLayer, context);
    }

}
