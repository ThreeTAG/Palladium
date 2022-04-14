package net.threetag.palladium.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.mojang.math.Vector3f;
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
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.mixin.client.*;
import net.threetag.palladium.util.json.GsonUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

        dumpLayers();
    }

    public static LayerDefinition parseLayerDefinition(JsonObject json) {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        JsonObject parts = GsonHelper.getAsJsonObject(json, "mesh");

        for (Map.Entry<String, JsonElement> entry : parts.entrySet()) {
            String key = entry.getKey();
            JsonObject part = entry.getValue().getAsJsonObject();
            parseCubeListBuilder(key, root, part);
        }

        return LayerDefinition.create(meshDefinition, GsonHelper.getAsInt(json, "texture_width"), GsonHelper.getAsInt(json, "texture_height"));
    }

    public static PartDefinition parseCubeListBuilder(String name, PartDefinition parent, JsonObject json) {
        JsonArray cubes = GsonHelper.getAsJsonArray(json, "cubes");
        CubeListBuilder builder = CubeListBuilder.create();

        for (JsonElement j : cubes) {
            JsonObject cubeJson = j.getAsJsonObject();
            float[] origin = GsonUtil.getFloatArray(cubeJson, 3, "origin");
            float[] dimensions = GsonUtil.getFloatArray(cubeJson, 3, "dimensions");
            int[] textureOffset = GsonUtil.getIntArray(cubeJson, 2, "texture_offset", 0, 0);
            float[] textureScale = GsonUtil.getFloatArray(cubeJson, 2, "texture_scale", 1F, 1F);
            float[] deformation = GsonUtil.getFloatArray(cubeJson, 3, "deformation", 1F, 1F, 1F);

            builder.mirror(GsonHelper.getAsBoolean(cubeJson, "mirror", false));
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
        }

        PartPose partPose = PartPose.ZERO;

        if (GsonHelper.isValidNode(json, "part_pose")) {
            JsonObject partPoseJson = GsonHelper.getAsJsonObject(json, "part_pose");
            float[] offset = GsonUtil.getFloatArray(partPoseJson, 3, "offset", 0F, 0F, 0F);
            float[] rotation1 = GsonUtil.getFloatArray(partPoseJson, 3, "rotation", 0F, 0F, 0F);
            double[] rotation = new double[3];
            for (int i = 0; i < 3; i++) {
                rotation[i] = Math.toRadians(rotation1[i]);
            }
            partPose = PartPose.offsetAndRotation(offset[0], offset[1], offset[2], (float) rotation[0], (float) rotation[1], (float) rotation[2]);
        }

        PartDefinition partDefinition = parent.addOrReplaceChild(name, builder, partPose);

        if (GsonHelper.isValidNode(json, "children")) {
            JsonObject children = GsonHelper.getAsJsonObject(json, "children");

            for (Map.Entry<String, JsonElement> entry : children.entrySet()) {
                String key = entry.getKey();
                JsonObject part = entry.getValue().getAsJsonObject();
                parseCubeListBuilder(key, partDefinition, part);
            }
        }

        return partDefinition;
    }

    private static ModelLayerLocation mapPathToModelLayerLoc(ResourceLocation path) {
        int idx = path.getPath().indexOf('/');
        if (idx == -1) {
            Palladium.LOGGER.error("Entity model path of {} was invalid, must contain at least one folder", path);
            return null;
        }

        return new ModelLayerLocation(new ResourceLocation(path.getNamespace(), path.getPath().substring(idx + 1)), path.getPath().substring(0, idx));
    }

    public static boolean dumpLayers() {
        File file = new File(HTMLBuilder.SUBFOLDER, "palladium/models");

        if (!file.exists() && !file.mkdirs())
            return false;

        AtomicBoolean result = new AtomicBoolean(true);

        ((EntityModelSetMixin) Minecraft.getInstance().getEntityModels()).getRoots().forEach((modelLayerLocation, layerDefinition) -> {
            File layerFolder = new File(file, modelLayerLocation.getModel().getNamespace() + "/" + modelLayerLocation.getLayer());
            File outputFile = new File(layerFolder, modelLayerLocation.getModel().getPath() + ".json");

            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs())
                result.set(false);

            try {
                JsonObject json = new JsonObject();

                JsonObject mesh = new JsonObject();
                var root = ((LayerDefinitionMixin) layerDefinition).getMesh().getRoot();
                ((PartDefinitionMixin) root).getChildren().forEach((s, partDefinition) -> {
                    var part = (PartDefinitionMixin) partDefinition;
                    mesh.add(s, serializePart(part));
                });

                json.add("mesh", mesh);
                json.addProperty("texture_width", ((MaterialDefinitionMixin) ((LayerDefinitionMixin) layerDefinition).getMaterial()).getXTexSize());
                json.addProperty("texture_height", ((MaterialDefinitionMixin) ((LayerDefinitionMixin) layerDefinition).getMaterial()).getYTexSize());

                Files.writeString(outputFile.toPath(), GSON.toJson(json));
            } catch (IOException e) {
                Palladium.LOGGER.error("Error while dumping model {}", outputFile, e);
                result.set(false);
            }
        });

        return result.get();
    }

    private static JsonObject serializePart(PartDefinitionMixin part) {
        JsonObject json = new JsonObject();

        JsonArray cubes = new JsonArray();
        for (CubeDefinition cube : part.getCubes()) {
            Object o = cube;
            CubeDefinitionMixin c = (CubeDefinitionMixin) o;
            JsonObject cubeJson = new JsonObject();
            cubeJson.add("origin", toJson(c.getOrigin()));
            cubeJson.add("dimensions", toJson(c.getDimensions()));
            cubeJson.add("texture_offset", toJson(c.getTexCoord()));
            cubeJson.add("deformation", toJson(c.getGrow()));
            cubeJson.addProperty("mirror", c.getMirror());
            cubes.add(cubeJson);
        }

        JsonObject partPose = new JsonObject();
        partPose.add("offset", toJson(new Vector3f(part.getPartPose().x, part.getPartPose().y, part.getPartPose().z)));
        partPose.add("rotation", toJson(new Vector3f((float) Math.toDegrees(part.getPartPose().xRot), (float) Math.toDegrees(part.getPartPose().yRot), (float) Math.toDegrees(part.getPartPose().zRot))));

        JsonObject children = new JsonObject();
        part.getChildren().forEach((s, partDefinition) -> {
            children.add(s, serializePart((PartDefinitionMixin) partDefinition));
        });

        json.add("cubes", cubes);
        json.add("part_pose", partPose);
        json.add("children", children);
        return json;
    }

    private static JsonArray toJson(Vector3f vec) {
        JsonArray array = new JsonArray();
        array.add(vec.x());
        array.add(vec.y());
        array.add(vec.z());
        return array;
    }

    private static JsonArray toJson(UVPair uv) {
        JsonArray array = new JsonArray();
        array.add(uv.u());
        array.add(uv.v());
        return array;
    }

    private static JsonArray toJson(CubeDeformation cubeDeformation) {
        JsonArray array = new JsonArray();
        array.add(((CubeDeformationMixin) cubeDeformation).getGrowX());
        array.add(((CubeDeformationMixin) cubeDeformation).getGrowY());
        array.add(((CubeDeformationMixin) cubeDeformation).getGrowZ());
        return array;
    }
}
