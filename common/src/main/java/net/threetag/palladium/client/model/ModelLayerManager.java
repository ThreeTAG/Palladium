package net.threetag.palladium.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.documentation.HTMLBuilder;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModelLayerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public ModelLayerManager() {
        super(GSON, "palladium/model_layers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
        Map<ModelLayerLocation, LayerDefinition> jsonRoots = new HashMap<>();
        EntityModelSet entityModels = Minecraft.getInstance().getEntityModels();
        var codeRoots = entityModels.roots;
        var roots = new HashMap<>(codeRoots);

        object.forEach((id, jsonElement) -> {
            ModelLayerLocation layerLocation = mapPathToModelLayerLoc(id);
            try {
                LayerDefinition layerDefinition = parseLayerDefinition(jsonElement.getAsJsonObject());
                jsonRoots.put(layerLocation, layerDefinition);
            } catch (Exception e) {
                AddonPackLog.error("Error parsing entity model json " + id, e);
            }
        });

        roots.putAll(jsonRoots);
        entityModels.roots = ImmutableMap.copyOf(roots);

        dumpLayers();
    }

    public static LayerDefinition parseLayerDefinition(JsonObject json) {
        if (GsonHelper.isValidNode(json, "minecraft:geometry")) {
            return parseBedrockEntityFormat(json);
        }

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

    public static void parseCubeListBuilder(String name, PartDefinition parent, JsonObject json) {
        JsonArray cubes = GsonHelper.getAsJsonArray(json, "cubes");
        CubeListBuilder builder = CubeListBuilder.create();

        for (JsonElement j : cubes) {
            JsonObject cubeJson = j.getAsJsonObject();
            float[] origin = GsonUtil.getFloatArray(cubeJson, 3, "origin");
            float[] dimensions = GsonUtil.getFloatArray(cubeJson, 3, "dimensions");
            int[] textureOffset = GsonUtil.getIntArray(cubeJson, 2, "texture_offset", 0, 0);
            float[] textureScale = GsonUtil.getFloatArray(cubeJson, 2, "texture_scale", 1F, 1F);
            float[] deformation = GsonUtil.getFloatArray(cubeJson, 3, "deformation", 0F, 0F, 0F);

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

    }

    private static ModelLayerLocation mapPathToModelLayerLoc(ResourceLocation path) {
        int idx = path.getPath().indexOf('/');
        if (idx == -1) {
            AddonPackLog.error("Entity model path of {} was invalid, must contain at least one folder", path);
            return null;
        }

        return new ModelLayerLocation(new ResourceLocation(path.getNamespace(), path.getPath().substring(idx + 1)), path.getPath().substring(0, idx));
    }

    public static void dumpLayers() {
        File file = new File(HTMLBuilder.SUBFOLDER, "palladium/models");

        if (!file.exists() && !file.mkdirs())
            return;

        AtomicBoolean result = new AtomicBoolean(true);

        Minecraft.getInstance().getEntityModels().roots.forEach((modelLayerLocation, layerDefinition) -> {
            if (!modelLayerLocation.getModel().getNamespace().equalsIgnoreCase("minecraft")) {
                return;
            }
            File layerFolder = new File(file, modelLayerLocation.getModel().getNamespace() + "/" + modelLayerLocation.getLayer());
            File outputFile = new File(layerFolder, modelLayerLocation.getModel().getPath() + ".json");

            if (!outputFile.getParentFile().exists() && !outputFile.getParentFile().mkdirs())
                result.set(false);

            try {
                JsonObject json = new JsonObject();

                JsonObject mesh = new JsonObject();
                var root = layerDefinition.mesh.getRoot();
                root.children.forEach((s, partDefinition) -> mesh.add(s, serializePart(partDefinition)));

                json.add("mesh", mesh);
                json.addProperty("texture_width", layerDefinition.material.xTexSize);
                json.addProperty("texture_height", layerDefinition.material.yTexSize);

                Files.writeString(outputFile.toPath(), GSON.toJson(json));
            } catch (IOException e) {
                Palladium.LOGGER.error("Error while dumping model {}", outputFile, e);
                result.set(false);
            }
        });

        result.get();
    }

    private static JsonObject serializePart(PartDefinition part) {
        JsonObject json = new JsonObject();

        JsonArray cubes = new JsonArray();
        for (CubeDefinition cube : part.cubes) {
            JsonObject cubeJson = new JsonObject();
            cubeJson.add("origin", toJson(cube.origin));
            cubeJson.add("dimensions", toJson(cube.dimensions));
            cubeJson.add("texture_offset", toJson(cube.texCoord));
            cubeJson.add("deformation", toJson(cube.grow));
            cubeJson.addProperty("mirror", cube.mirror);
            cubes.add(cubeJson);
        }

        JsonObject partPose = new JsonObject();
        partPose.add("offset", toJson(new Vector3f(part.partPose.x, part.partPose.y, part.partPose.z)));
        partPose.add("rotation", toJson(new Vector3f((float) Math.toDegrees(part.partPose.xRot), (float) Math.toDegrees(part.partPose.yRot), (float) Math.toDegrees(part.partPose.zRot))));

        JsonObject children = new JsonObject();
        part.children.forEach((s, partDefinition) -> children.add(s, serializePart(partDefinition)));

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
        array.add(cubeDeformation.growX);
        array.add(cubeDeformation.growY);
        array.add(cubeDeformation.growZ);
        return array;
    }


    public static LayerDefinition parseBedrockEntityFormat(JsonObject json) {
        var geometry = GsonHelper.getAsJsonArray(json, "minecraft:geometry");

        if (geometry.isEmpty()) {
            throw new JsonParseException("Empty geometry!");
        }

        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();

        var geo = GsonHelper.convertToJsonObject(geometry.get(0), "minecraft:geometry[].$");
        var description = GsonHelper.getAsJsonObject(geo, "description", new JsonObject());
        int textureWidth = GsonHelper.getAsInt(description, "texture_width");
        int textureHeight = GsonHelper.getAsInt(description, "texture_height");

        var bones = GsonHelper.getAsJsonArray(geo, "bones", new JsonArray());
        Map<String, BedrockModelPartCache> cache = new HashMap<>();
        Map<String, PartDefinition> modelParts = new HashMap<>();

        for (JsonElement b : bones) {
            var bone = GsonHelper.convertToJsonObject(b, "minecraft:geomeotry[].bones.$");
            var name = GsonHelper.getAsString(bone, "name");
            var parent = GsonHelper.getAsString(bone, "parent", null);
            var pivot = GsonUtil.getFloatArray(bone, 3, "pivot", 0, 0, 0);
            var rotation = GsonUtil.getFloatArray(bone, 3, "rotation", 0, 0, 0);
            var cubesJson = GsonHelper.getAsJsonArray(bone, "cubes", new JsonArray());
            List<BedrockModelCube> cubes = new ArrayList<>();

            for (JsonElement cj : cubesJson) {
                var cubeJson = GsonHelper.convertToJsonObject(cj, "minecraft:geomeotry[].bones[].cubes[].$");
                var origin = GsonUtil.getFloatArray(cubeJson, 3, "origin");
                var size = GsonUtil.getFloatArray(cubeJson, 3, "size");
                var inflate = GsonHelper.getAsFloat(cubeJson, "inflate", 0);
                var uvJson = cubeJson.get("uv");
                var mirror = GsonHelper.getAsBoolean(cubeJson, "mirror", false);

                if (uvJson.isJsonArray()) {
                    var uv = GsonUtil.getIntArray(cubeJson, 2, "uv", 0, 0);
                    cubes.add(new BedrockModelCube(new Vector3f(origin[0], origin[1], origin[2]),
                            new Vector3f(size[0], size[1], size[2]),
                            inflate, mirror, new UVPair(uv[0], uv[1])));
                } else {
                    var uvs = GsonHelper.convertToJsonObject(uvJson, "minecraft:geomeotry[].bones[].cubes[].uv");
                    Map<Direction, ExtendedCubeListBuilder.PerFaceUV> uvMap = new HashMap<>();
                    for (Direction direction : Direction.values()) {
                        if (GsonHelper.isValidNode(uvs, direction.getName())) {
                            var directionJson = GsonHelper.getAsJsonObject(uvs, direction.getName());
                            var uv = GsonUtil.getIntArray(directionJson, 2, "uv", 0, 0);
                            var uvSize = GsonUtil.getIntArray(directionJson, 2, "uv_size", 0, 0);
                            uvMap.put(direction.getAxis() == Direction.Axis.Z ? direction : direction.getOpposite(), new ExtendedCubeListBuilder.PerFaceUV(new UVPair(uv[0], uv[1]), new UVPair(uvSize[0], uvSize[1])));
                        }
                    }
                    cubes.add(new BedrockModelCube(new Vector3f(origin[0], origin[1], origin[2]),
                            new Vector3f(size[0], size[1], size[2]),
                            inflate, mirror, uvMap));
                }
            }

            cache.put(name, new BedrockModelPartCache(name, parent, new Vector3f(pivot[0], pivot[1], pivot[2]),
                    new Vector3f((float) Math.toRadians(rotation[0]), (float) Math.toRadians(rotation[1]), (float) Math.toRadians(rotation[2])), cubes).convert());
        }

        // Validate parents
        for (Map.Entry<String, BedrockModelPartCache> e : cache.entrySet()) {
            if (e.getValue().parent != null && !cache.containsKey(e.getValue().parent)) {
                throw new JsonParseException("Unknown parent '" + e.getValue().parent + "'");
            }
        }

        // Organize hierarchy
        while (!cache.isEmpty()) {
            var copy = new HashMap<>(cache);

            for (Map.Entry<String, BedrockModelPartCache> e : copy.entrySet()) {
                if (e.getValue().parent == null) {
                    modelParts.put(e.getKey(), e.getValue().add(root));
                    cache.remove(e.getKey());
                } else {
                    var parent = modelParts.get(e.getValue().parent);

                    if (parent != null) {
                        modelParts.put(e.getKey(), e.getValue().add(parent));
                        cache.remove(e.getKey());
                    }
                }
            }
        }

        return LayerDefinition.create(meshDefinition, textureWidth, textureHeight);
    }

    public static class BedrockModelPartCache {

        private final String name;
        @Nullable
        public final String parent;
        private final Vector3f pivot;
        private final Vector3f rotation;
        private final List<BedrockModelCube> cubes;

        public BedrockModelPartCache(String name, @Nullable String parent, Vector3f pivot, Vector3f rotation, List<BedrockModelCube> cubes) {
            this.name = name;
            this.parent = parent;
            this.pivot = pivot;
            this.rotation = rotation;
            this.cubes = cubes;
        }

        public PartDefinition add(PartDefinition parent) {
            var builder = ExtendedCubeListBuilder.create();

            for (BedrockModelCube cube : this.cubes) {
                cube.add(builder);
            }

            return parent.addOrReplaceChild(this.name, builder,
                    PartPose.offsetAndRotation(
                            this.pivot.x, this.pivot.y, this.pivot.z,
                            this.rotation.x, this.rotation.y, this.rotation.z));
        }

        public BedrockModelPartCache convert() {
            this.cubes.forEach(c -> c.convert(this.pivot));
            this.pivot.mul(1, -1, 1).add(0, 24, 0);
            return this;
        }
    }

    public static class BedrockModelCube {

        private final Vector3f origin;
        private final Vector3f size;
        private final float inflate;
        private final UVPair uv;
        private final Map<Direction, ExtendedCubeListBuilder.PerFaceUV> uvMap;
        private final boolean mirror;

        public BedrockModelCube(Vector3f origin, Vector3f size, float inflate, boolean mirror, UVPair uv) {
            this.origin = origin;
            this.size = size;
            this.inflate = inflate;
            this.mirror = mirror;
            this.uv = uv;
            this.uvMap = null;
        }

        public BedrockModelCube(Vector3f origin, Vector3f size, float inflate, boolean mirror, Map<Direction, ExtendedCubeListBuilder.PerFaceUV> uvMap) {
            this.origin = origin;
            this.size = size;
            this.inflate = inflate;
            this.mirror = mirror;
            this.uv = null;
            this.uvMap = uvMap;
        }

        public void add(ExtendedCubeListBuilder builder) {
            if (this.uv != null) {
                builder.mirror(this.mirror).texOffs((int) this.uv.u(), (int) this.uv.v()).addBox(
                        this.origin.x, this.origin.y, this.origin.z,
                        this.size.x, this.size.y, this.size.z,
                        new CubeDeformation(this.inflate));
            } else {
                var cubeBuilder = builder.addPerFaceUVCube().origin(this.origin).dimensions(this.size).grow(this.inflate);
                for (Map.Entry<Direction, ExtendedCubeListBuilder.PerFaceUV> e : Objects.requireNonNull(this.uvMap).entrySet()) {
                    cubeBuilder.addFace(e.getKey(), e.getValue().uv(), e.getValue().size());
                }
                cubeBuilder.build();
            }
        }

        public void convert(Vector3f pivot) {
            this.origin.sub(pivot.x, 0, pivot.z);
            this.origin.y = pivot.y - this.origin.y - this.size.y;
        }
    }
}
