package net.threetag.palladium.client.model;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.HashMap;
import java.util.Map;

public class ModelLayerManager extends SimpleJsonResourceReloadListener<BedrockModel> {

    public static final ResourceLocation ID = Palladium.id("entity_models");
    public static final ModelLayerManager INSTANCE = new ModelLayerManager();

    public ModelLayerManager() {
        super(BedrockModel.CODEC, FileToIdConverter.json("palladium/model_layers"));
    }

    @Override
    protected void apply(Map<ResourceLocation, BedrockModel> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ModelLayerLocation, LayerDefinition> jsonRoots = new HashMap<>();
        EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
        var codeRoots = entityModels.roots;
        var roots = new HashMap<>(codeRoots);

        object.forEach((id, bedrockModel) -> {
            ModelLayerLocation layerLocation = mapPathToModelLayerLoc(id);
            try {
                LayerDefinition layerDefinition = bedrockModel.buildLayerDefinition();
                if (layerLocation != null)
                    jsonRoots.put(layerLocation, layerDefinition);
                jsonRoots.put(new ModelLayerLocation(id, "main"), layerDefinition);
            } catch (Exception e) {
                Palladium.LOGGER.error("Error parsing entity model json {}", id, e);
            }
        });

        roots.putAll(jsonRoots);
        entityModels.roots = ImmutableMap.copyOf(roots);
        AddonPackLog.info("Loaded {} model layers", object.size());
    }

    private static ModelLayerLocation mapPathToModelLayerLoc(ResourceLocation path) {
        int idx = path.getPath().indexOf('/');
        if (idx == -1) {
            return null;
        }

        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(path.getNamespace(), path.getPath().substring(idx + 1)), path.getPath().substring(0, idx));
    }
}
