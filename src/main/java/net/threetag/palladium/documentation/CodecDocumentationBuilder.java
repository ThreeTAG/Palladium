package net.threetag.palladium.documentation;

import com.google.gson.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CodecDocumentationBuilder<T, R extends T> {

    public static final String DOCUMENTATION_FOLDER = "palladium/documentation";
    public static final String FOLDER = DOCUMENTATION_FOLDER + "/export";
    private static Map<ResourceLocation, List<JsonElement>> LISTENER;

    private final MapCodec<R> mapCodec;
    private final Codec<T> mainCodec;
    private final HolderLookup.Provider provider;
    private String name;
    private String description;
    private R exampleObject;
    private JsonElement exampleJson;
    private final Map<String, Entry> entries = new HashMap<>();
    private final List<String> ignoredKeys = new ArrayList<>();

    public CodecDocumentationBuilder(MapCodec<R> mapCodec, HolderLookup.Provider provider) {
        this.mapCodec = mapCodec;
        this.mainCodec = null;
        this.provider = provider;
    }

    public CodecDocumentationBuilder(MapCodec<R> mapCodec, Codec<T> mainCodec, HolderLookup.Provider provider) {
        this.mapCodec = mapCodec;
        this.mainCodec = mainCodec;
        this.provider = provider;
    }

    public CodecDocumentationBuilder<T, R> setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public CodecDocumentationBuilder<T, R> setDescription(String description) {
        this.description = description;
        return this;
    }

    @SuppressWarnings("unchecked")
    public CodecDocumentationBuilder<T, R> setExampleObject(R exampleObject) {
        this.exampleObject = exampleObject;
        Codec<T> codec = this.mainCodec != null ? this.mainCodec : (Codec<T>) this.mapCodec.codec();
        try {
            this.exampleJson = codec.encodeStart(this.provider.createSerializationContext(JsonOps.INSTANCE), exampleObject).getOrThrow();
        } catch (Exception e) {
            Palladium.LOGGER.error("Error while encoding example object for documentation {}", this.name, e);
        }
        return this;
    }

    public CodecDocumentationBuilder<T, R> setExampleJson(JsonElement exampleJson) {
        this.exampleJson = exampleJson;
        return this;
    }

    public CodecDocumentationBuilder<T, R> addToExampleJson(String key, JsonElement jsonElement) {
        if (this.exampleJson == null || !this.exampleJson.isJsonObject()) {
            this.exampleJson = new JsonObject();
        }

        ((JsonObject) this.exampleJson).add(key, jsonElement);
        return this;
    }

    public R getExampleObject() {
        return this.exampleObject;
    }

    public JsonElement getExampleJson() {
        return this.exampleJson;
    }

    public CodecDocumentationBuilder<T, R> ignore(String key) {
        this.ignoredKeys.add(key);
        return this;
    }

    public CodecDocumentationBuilder<T, R> add(String key, SettingType type, String description) {
        this.entries.put(key, new Entry(key, type, description, true, null));
        return this;
    }

    public CodecDocumentationBuilder<T, R> addOptional(String key, SettingType type, String description) {
        this.entries.put(key, new Entry(key, type, description, false, null));
        return this;
    }

    public CodecDocumentationBuilder<T, R> addOptional(String key, SettingType type, String description, Object fallback) {
        this.entries.put(key, new Entry(key, type, description, false, fallback));
        return this;
    }

    public JsonObject build(ResourceKey<?> id) {
        return this.build(id.registry(), id.location());
    }

    public JsonObject build(ResourceLocation type, ResourceLocation id) {
        var json = new JsonObject();

        json.addProperty("namespace", id.getNamespace());
        json.addProperty("path", id.getPath());

        if (this.name != null && !this.name.isEmpty()) {
            json.addProperty("name", this.name);
        }

        if (this.description != null && !this.description.isEmpty()) {
            json.addProperty("description", this.description);
        }

        var fields = new JsonArray();

        this.mapCodec.keys(JsonOps.INSTANCE).map(JsonElement::getAsString).distinct().forEach(key -> {
            if (!this.ignoredKeys.contains(key)) {
                if (!this.entries.containsKey(key)) {
                    Palladium.LOGGER.error("No documentation entry {} found for {}", key, type.toString() + "/" + id.toString());
                } else {
                    var documented = this.entries.get(key);
                    var entryJson = new JsonObject();
                    entryJson.addProperty("key", key);
                    entryJson.add("type", documented.type.toJson());
                    entryJson.addProperty("description", documented.description);
                    entryJson.addProperty("required", documented.required);
                    entryJson.add("fallback", toJsonElement(documented.fallback));
                    fields.add(entryJson);
                }
            }
        });

        json.add("fields", fields);

        if (this.exampleJson != null) {
            json.add("example", orderJsonObject(this.exampleJson));
        }

        return json;
    }

    public void addToHtml(HTMLBuilder.HTMLObject div, @Nullable ResourceLocation id) {
        if (this.name != null && !this.name.isEmpty()) {
            var heading = HTMLBuilder.subHeading(this.name);
            if (id != null) {
                heading.setId(id.toString());
            }
            div.add(heading);
        }

        if (this.description != null && !this.description.isEmpty()) {
            div.add(HTMLBuilder.paragraph(this.description));
        }

        if (!this.entries.isEmpty()) {
            div.add(HTMLBuilder.subSubHeading("Settings:"))
                    .add(HTMLBuilder.table(Arrays.asList("Setting", "Type", "Description", "Required", "Fallback Value"), this.entries.entrySet().stream().map(e -> {
                        List<Object> list = new ArrayList<>();
                        var entry = e.getValue();
                        list.add(e.getKey());
                        list.add(entry.type.toString());
                        list.add(entry.description);
                        list.add(entry.required);
                        list.add(Utils.orElse(entry.fallback, "/").toString());
                        return list;
                    }).collect(Collectors.toList())));
        }

        if (this.exampleJson != null) {
            div.add(HTMLBuilder.subSubHeading("Example:"))
                    .add(new HTMLBuilder.HTMLObject("pre", orderJsonObject(this.exampleJson).toString()).addAttribute("class", "json-snippet"));
        }
    }

    public static void startListening() {
        LISTENER = new HashMap<>();
    }

    public static void addToDocs(ResourceLocation type, JsonElement jsonElement) {
        if (LISTENER != null) {
            LISTENER.computeIfAbsent(type, k -> new ArrayList<>()).add(jsonElement);
        }
    }

    public static void createDocFiles() {
        if (LISTENER == null) {
            throw new IllegalStateException("LISTENER has not been initialized");
        } else {
            createDocFiles(LISTENER);
            LISTENER = null;
        }
    }

    public static void createDocFiles(Map<ResourceLocation, List<JsonElement>> generated) {
        for (Map.Entry<ResourceLocation, List<JsonElement>> e : generated.entrySet()) {
            try {
                var jsonArray = new JsonArray();
                for (JsonElement j : e.getValue()) {
                    jsonArray.add(j);
                }

                BufferedWriter bw = getBufferedWriter(e.getKey().getNamespace(), e.getKey().getPath());
                bw.write(jsonArray.toString());
                bw.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private static @NotNull BufferedWriter getBufferedWriter(String path, String filename) throws IOException {
        Path folder = FMLPaths.GAMEDIR.get().resolve(FOLDER).resolve(path);
        var file = folder.toFile();
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("Could not create palladium export directory! Please create the directory yourself, or make sure the name is not taken by a file and you have permission to create directories.");

        file = folder.resolve(filename + ".paldoc").toFile();
        return new BufferedWriter(new FileWriter(file));
    }

    private static JsonElement toJsonElement(Object object) {
        return switch (object) {
            case null -> JsonNull.INSTANCE;
            case Number n -> new JsonPrimitive(n);
            case Boolean b -> new JsonPrimitive(b);
            case String s -> new JsonPrimitive(s);
            case Character c -> new JsonPrimitive(c);
            default -> new JsonPrimitive(object.toString());
        };
    }

    private static JsonElement orderJsonObject(JsonElement jsonElement) {
        if (jsonElement.isJsonObject()) {
            JsonObject json = jsonElement.getAsJsonObject();
            JsonObject orderedJson = new JsonObject();

            if (json.has("type")) {
                orderedJson.add("type", json.get("type"));
            }

            for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
                if (!entry.getKey().equals("type")) {
                    orderedJson.add(entry.getKey(), orderJsonObject(entry.getValue()));
                }
            }

            return orderedJson;
        } else {
            return jsonElement;
        }
    }

    private record Entry(String key, SettingType type, String description, boolean required, Object fallback) {

    }
}
