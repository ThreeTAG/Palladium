package net.threetag.palladium.client.model;

import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.util.CodecExtras;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.*;

public record BedrockModel(List<Geometry> geometries) {

    public static final Codec<UVPair> UV_PAIR_CODEC = Codec.FLOAT.listOf(2, 2).xmap(floats -> new UVPair(floats.getFirst(), floats.getLast()), uvPair -> List.of(uvPair.u(), uvPair.v()));

    public static final Codec<BedrockModel> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Geometry.CODEC.listOf().fieldOf("minecraft:geometry").forGetter(BedrockModel::geometries)
    ).apply(instance, BedrockModel::new));

    public LayerDefinition buildLayerDefinition() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        var geometry = this.geometries.getFirst();
        int textureWidth = geometry.description.textureWidth;
        int textureHeight = geometry.description.textureHeight;

        Map<String, PartCache> cache = new HashMap<>();
        Map<String, PartDefinition> modelParts = new HashMap<>();

        for (Bone bone : geometry.bones) {
            cache.put(bone.name, new PartCache(bone.name, bone.parent, bone.pivot, bone.rotation, bone.cubes));
        }

        // Validate parents
        for (Map.Entry<String, PartCache> e : cache.entrySet()) {
            if (e.getValue().parentName != null) {
                if (!cache.containsKey(e.getValue().parentName)) {
                    throw new JsonParseException("Unknown parent '" + e.getValue().parentName + "'");
                } else {
                    e.getValue().parent = cache.get(e.getValue().parentName);
                    cache.get(e.getValue().parentName).children.add(e.getValue());
                }
            }
        }

        // Organize hierarchy
        while (!cache.isEmpty()) {
            var copy = new HashMap<>(cache);

            for (Map.Entry<String, PartCache> e : copy.entrySet()) {
                if (e.getValue().parentName == null) {
                    convertHierarchy(e.getValue(), "");
                    modelParts.put(e.getKey(), e.getValue().add(root));
                    cache.remove(e.getKey());
                } else {
                    var parent = modelParts.get(e.getValue().parentName);

                    if (parent != null) {
                        modelParts.put(e.getKey(), e.getValue().add(parent));
                        cache.remove(e.getKey());
                    }
                }
            }
        }

        return LayerDefinition.create(meshDefinition, textureWidth, textureHeight);
    }

    private static void convertHierarchy(PartCache part, String prefix) {
        part.convert();
        for (PartCache child : part.children) {
            convertHierarchy(child, prefix + "  ");
        }
    }

    public static class PartCache {

        private final String name;
        @Nullable
        public final String parentName;
        public PartCache parent;
        public List<PartCache> children = new ArrayList<>();
        private final Vector3f unconvertedPivot;
        private final Vector3f pivot;
        private final Vector3f rotation;
        private List<Cube> cubes;

        public PartCache(String name, @Nullable String parentName, Vector3f pivot, Vector3f rotation, List<Cube> cubes) {
            this.name = name;
            this.parentName = parentName;
            this.unconvertedPivot = pivot;
            this.pivot = pivot;
            this.rotation = rotation;
            this.cubes = cubes;
        }

        public PartDefinition add(PartDefinition parent) {
            var builder = ExtendedCubeListBuilder.create();

            for (Cube cube : this.cubes) {
                cube.addToBuilder(builder);
            }

            return parent.addOrReplaceChild(this.name, builder,
                    PartPose.offsetAndRotation(
                            this.pivot.x, this.pivot.y, this.pivot.z,
                            (float) Math.toRadians(this.rotation.x),
                            (float) Math.toRadians(this.rotation.y),
                            (float) Math.toRadians(this.rotation.z)));
        }

        public PartCache convert() {
            this.cubes = this.cubes.stream().map(cube -> cube.convert(this.pivot, this.name)).toList();
            this.pivot.mul(1, -1, 1).add(0, 24, 0);
            var parent = this.parent;

            while (parent != null) {
                this.pivot.sub(parent.unconvertedPivot);
                parent = parent.parent;
            }

            return this;
        }
    }

    private record Geometry(Description description, List<Bone> bones) {

        public static final Codec<Geometry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Description.CODEC.fieldOf("description").forGetter(Geometry::description),
                Bone.CODEC.listOf().optionalFieldOf("bones", Collections.emptyList()).forGetter(Geometry::bones)
        ).apply(instance, Geometry::new));

    }

    private record Description(int textureWidth, int textureHeight) {

        public static final Codec<Description> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("texture_width").forGetter(Description::textureWidth),
                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("texture_height").forGetter(Description::textureHeight)
        ).apply(instance, Description::new));

    }

    private record Bone(String name, @Nullable String parent, Vector3f pivot, Vector3f rotation, List<Cube> cubes) {

        public static final Codec<Bone> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("name").forGetter(Bone::name),
                Codec.STRING.optionalFieldOf("parent").forGetter(b -> Optional.ofNullable(b.parent())),
                CodecExtras.VECTOR_3F_CODEC.optionalFieldOf("pivot", new Vector3f(0, 0, 0)).forGetter(Bone::pivot),
                CodecExtras.VECTOR_3F_CODEC.optionalFieldOf("rotation", new Vector3f(0, 0, 0)).forGetter(Bone::rotation),
                Cube.CODEC.listOf().optionalFieldOf("cubes", Collections.emptyList()).forGetter(Bone::cubes)
        ).apply(instance, (name, parent, pivot, rot, cubes) -> new Bone(name, parent.orElse(null), pivot, rot, cubes)));

    }

    private record Cube(Vector3f origin, Vector3f size, float inflate, boolean mirror, UV uv) {

        public static final Codec<UV> UV_CODEC = Codec.either(BoxUV.CODEC, PerFaceUV.CODEC).xmap(either -> either.map(uv -> uv, uv -> uv), uv -> uv instanceof BoxUV ? Either.left((BoxUV) uv) : Either.right((PerFaceUV) uv));

        public static final Codec<Cube> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                CodecExtras.VECTOR_3F_CODEC.fieldOf("origin").forGetter(Cube::origin),
                CodecExtras.VECTOR_3F_CODEC.fieldOf("size").forGetter(Cube::size),
                Codec.FLOAT.optionalFieldOf("inflate", 0.0F).forGetter(Cube::inflate),
                Codec.BOOL.optionalFieldOf("mirror", false).forGetter(Cube::mirror),
                UV_CODEC.fieldOf("uv").forGetter(Cube::uv)
        ).apply(instance, Cube::new));

        public void addToBuilder(ExtendedCubeListBuilder builder) {
            if (this.uv instanceof BoxUV boxUV) {
                builder.mirror(this.mirror).texOffs((int) boxUV.uvPair.u(), (int) boxUV.uvPair.v()).addBox(
                        this.origin.x, this.origin.y, this.origin.z,
                        this.size.x, this.size.y, this.size.z,
                        new CubeDeformation(this.inflate));
            } else if (this.uv instanceof PerFaceUV perFaceUV) {
                var cubeBuilder = builder.addPerFaceUVCube().origin(this.origin).dimensions(this.size).grow(this.inflate);
                for (Map.Entry<Direction, PerFaceUV.PerFaceUVObject> e : Objects.requireNonNull(perFaceUV.uvMap).entrySet()) {
                    var direction = e.getKey().getAxis() == Direction.Axis.Z ? e.getKey() : e.getKey().getOpposite();
                    cubeBuilder.addFace(direction, e.getValue().uv(), e.getValue().uvSize());
                }
                cubeBuilder.build();
            }
        }

        public Cube convert(Vector3f pivot, String name) {
            var copy = new Vector3f(this.origin);
            copy.sub(pivot.x, 0, pivot.z);
            copy.y = pivot.y - copy.y - this.size.y;
            return new Cube(copy, this.size, this.inflate, this.mirror, this.uv);
        }

    }

    private static class BoxUV extends UV {

        public static final Codec<BoxUV> CODEC = UV_PAIR_CODEC.xmap(BoxUV::new, boxUV -> boxUV.uvPair);

        private final UVPair uvPair;

        private BoxUV(UVPair uvPair) {
            this.uvPair = uvPair;
        }
    }

    private static class PerFaceUV extends UV {

        private record PerFaceUVObject(UVPair uv, UVPair uvSize) {
            public static final Codec<PerFaceUVObject> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    UV_PAIR_CODEC.optionalFieldOf("uv", new UVPair(0, 0)).forGetter(PerFaceUVObject::uv),
                    UV_PAIR_CODEC.optionalFieldOf("uv_size", new UVPair(0, 0)).forGetter(PerFaceUVObject::uv)
            ).apply(instance, PerFaceUVObject::new));
        }

        public static final Codec<PerFaceUV> CODEC = Codec.unboundedMap(Direction.CODEC, PerFaceUVObject.CODEC).xmap(PerFaceUV::new, perFaceUV -> perFaceUV.uvMap);

        private final Map<Direction, PerFaceUVObject> uvMap;

        private PerFaceUV(Map<Direction, PerFaceUVObject> uvMap) {
            this.uvMap = uvMap;
        }

    }

    private static abstract class UV {

    }

}
