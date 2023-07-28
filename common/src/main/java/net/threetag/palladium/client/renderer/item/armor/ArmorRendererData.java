package net.threetag.palladium.client.renderer.item.armor;

import com.google.gson.JsonObject;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.client.renderer.renderlayer.ModelLookup;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArmorRendererData {

    private final ModelLookup.Model modelType;
    private final ArmorTextureData textures;
    private final ArmorModelData models;
    private final ArmorRendererConditions conditions;

    public ArmorRendererData(ModelLookup.Model modelType, ArmorTextureData textures, ArmorModelData models, ArmorRendererConditions conditions) {
        this.modelType = modelType;
        this.textures = textures;
        this.models = models;
        this.conditions = conditions;
    }

    public static ArmorRendererData fromJson(JsonObject json) {
        var modelType = ModelLookup.get(GsonUtil.getAsResourceLocation(json, "model_type", new ResourceLocation("humanoid")));
        var textures = ArmorTextureData.fromJson(json.get("textures"));
        var modelLayers = ArmorModelData.fromJson(json.get("model_layers"));
        var conditions = ArmorRendererConditions.fromJson(json.has("conditions") ? GsonHelper.getAsJsonArray(json, "conditions") : null);
        return new ArmorRendererData(modelType, textures, modelLayers, conditions);
    }

    public void buildModels(EntityModelSet modelSet) {
        this.models.buildModels(this.modelType, modelSet);
    }

    @NotNull
    public ResourceLocation getTexture(DataContext context) {
        String key = this.conditions.getTexture(context);
        return this.textures.get(key, context);
    }

    @NotNull
    public ResourceLocation getTexture(DataContext context, String key) {
        return this.textures.get(key, context);
    }

    @Nullable
    public HumanoidModel<?> getModel(LivingEntity entity, DataContext context) {
        String key = this.conditions.getModelLayer(context);
        return this.models.get(key, entity, !this.conditions.conditions.isEmpty());
    }

    public ArmorTextureData getTextures() {
        return textures;
    }

    public ArmorModelData getModels() {
        return models;
    }

}
