package net.threetag.palladium.client.renderer.trail;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.AddonParser;
import net.threetag.palladium.documentation.IDocumentedConfigurable;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.HashMap;
import java.util.Map;

public class TrailRendererManager extends SimpleJsonResourceReloadListener {

    public static final TrailRendererManager INSTANCE = new TrailRendererManager();
    private static final Map<ResourceLocation, TypeSerializer> PARSERS = new HashMap<>();
    private Map<ResourceLocation, TrailRenderer> renderer = new HashMap<>();

    public TrailRendererManager() {
        super(AddonParser.GSON, "palladium/trails");
    }

    static {
        registerParser(new AfterImageTrailRenderer.Serializer());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, TrailRenderer> builder = ImmutableMap.builder();

        object.forEach((resourceLocation, jsonElement) -> {
            try {
                TrailRenderer trailRenderer = fromJson(GsonHelper.convertToJsonObject(jsonElement, "$"));
                builder.put(resourceLocation, trailRenderer);
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading trail renderer {}", resourceLocation, e);
            }
        });

        this.renderer = builder.build();
    }

    public TrailRenderer getRenderer(ResourceLocation id) {
        return this.renderer.get(id);
    }

    public static void registerParser(TypeSerializer serializer) {
        PARSERS.put(serializer.getId(), serializer);
    }

    public static TrailRenderer fromJson(JsonObject json) {
        ResourceLocation parserId = GsonUtil.getAsResourceLocation(json, "type");

        if (!PARSERS.containsKey(parserId)) {
            throw new JsonParseException("Unknown trail renderer type '" + parserId + "'");
        }

        return PARSERS.get(parserId).parse(json);
    }

    public interface TypeSerializer extends IDocumentedConfigurable {

        TrailRenderer parse(JsonObject json);

    }
}
