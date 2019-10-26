package net.threetag.threecore.util.modellayer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;

import java.util.List;
import java.util.Map;

public class ModelLayerLoader extends JsonReloadListener {

    public static final List<Runnable> POST_LOAD_CALLBACKS = Lists.newLinkedList();
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private static Map<ResourceLocation, ModelLayer> LAYERS = Maps.newHashMap();

    public ModelLayerLoader() {
        super(GSON, "model_layers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> resourceLocationJsonObjectMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        LAYERS.clear();
        for (Map.Entry<ResourceLocation, JsonObject> entry : resourceLocationJsonObjectMap.entrySet()) {
            try {
                ModelLayer layer = ModelLayerManager.parseLayer(entry.getValue());
                LAYERS.put(entry.getKey(), layer);
            } catch (Exception e) {
                ThreeCore.LOGGER.error("Parsing error loading model layer {}", entry.getKey(), e);
            }
            ThreeCore.LOGGER.info("Loaded model layer {}", entry.getKey());
        }

        POST_LOAD_CALLBACKS.forEach(Runnable::run);
        POST_LOAD_CALLBACKS.clear();
    }

    public static ModelLayer getModelLayer(ResourceLocation id) {
        return LAYERS.get(id);
    }
}
