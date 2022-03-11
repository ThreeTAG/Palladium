package net.threetag.palladium.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.mixin.EntityModelSetMixin;
import net.threetag.palladium.util.PalladiumGsonHelper;

import java.util.HashMap;
import java.util.Map;

public class EntityModelManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public EntityModelManager() {
        super(GSON, "models/entity");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ModelLayerLocation, LayerDefinition> jsonRoots = new HashMap<>();
        EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
        var codeRoots = ((EntityModelSetMixin) entityModels).getRoots();
        var roots = new HashMap<>(codeRoots);

        object.forEach((id, jsonElement) -> {
            ModelLayerLocation layerLocation = mapPathToModelLayerLoc(id);

            try {
                LayerDefinition layerDefinition = parseLayerDefinition(jsonElement.getAsJsonObject());
                jsonRoots.put(layerLocation, layerDefinition);
            } catch (Exception e) {
                Palladium.LOGGER.error("Error parsing entity model json " + id, e);
            }
        });

        roots.putAll(jsonRoots);
        ((EntityModelSetMixin) entityModels).setRoots(ImmutableMap.copyOf(roots));
    }

    public static LayerDefinition parseLayerDefinition(JsonObject json) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        JsonObject parts = GsonHelper.getAsJsonObject(json, "mesh");

        for (Map.Entry<String, JsonElement> entry : parts.entrySet()) {
            String key = entry.getKey();
            JsonObject part = entry.getValue().getAsJsonObject();
            JsonArray cubes = GsonHelper.getAsJsonArray(part, "cubes");
            CubeListBuilder builder = CubeListBuilder.create();

            for(int i = 0; i < cubes.size(); i++) {
                parseCubeListBuilder(builder, cubes.get(i).getAsJsonObject());
            }

            PartPose partPose = PartPose.ZERO;

            if (GsonHelper.isValidNode(part, "part_pose")) {
                JsonObject partPoseJson = GsonHelper.getAsJsonObject(part, "part_pose");
                float[] offset = PalladiumGsonHelper.getFloatArray(partPoseJson, 3, "offset", 0F, 0F, 0F);
                float[] rotation = PalladiumGsonHelper.getFloatArray(partPoseJson, 3, "rotation", 0F, 0F, 0F);
                partPose = PartPose.offsetAndRotation(offset[0], offset[1], offset[2], rotation[0], rotation[1], rotation[2]);
            }

            root.addOrReplaceChild(key, builder, partPose);
        }

        return LayerDefinition.create(meshDefinition, GsonHelper.getAsInt(json, "texture_width"), GsonHelper.getAsInt(json, "texture_height"));
    }

    public static CubeListBuilder parseCubeListBuilder(CubeListBuilder builder, JsonObject json) {
        float[] origin = PalladiumGsonHelper.getFloatArray(json, 3, "origin");
        float[] dimensions = PalladiumGsonHelper.getFloatArray(json, 3, "dimensions");
        int[] textureOffset = PalladiumGsonHelper.getIntArray(json, 2, "texture_offset", 0, 0);
        float[] textureScale = PalladiumGsonHelper.getFloatArray(json, 2, "texture_scale", 1F, 1F);
        float[] deformation = PalladiumGsonHelper.getFloatArray(json, 3, "deformation", 1F, 1F, 1F);

        builder.mirror(GsonHelper.getAsBoolean(json, "mirror", false));
        builder.texOffs(textureOffset[0], textureOffset[1]);
        builder.addBox(
                origin[0],
                origin[1],
                origin[2],
                dimensions[0],
                dimensions[1],
                dimensions[2],
                new CubeDeformation(deformation[0], deformation[1], deformation[2]),
                textureScale[0],
                textureScale[1]
        );

        return builder;
    }

    private static ModelLayerLocation mapPathToModelLayerLoc(ResourceLocation path) {
        int idx = path.getPath().indexOf('/');
        if (idx == -1) {
            Palladium.LOGGER.error("Entity model path of {} was invalid, must contain at least one folder", path);
            return null;
        }

        return new ModelLayerLocation(new ResourceLocation(path.getNamespace(), path.getPath().substring(idx + 1)), path.getPath().substring(0, idx));
    }
}
