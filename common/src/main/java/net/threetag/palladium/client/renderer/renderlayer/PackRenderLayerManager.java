package net.threetag.palladium.client.renderer.renderlayer;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.AddonParser;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.util.LegacySupportJsonReloadListener;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PackRenderLayerManager extends LegacySupportJsonReloadListener {

    private static PackRenderLayerManager INSTANCE;
    private static final Map<ResourceLocation, Function<JsonObject, IPackRenderLayer>> RENDER_LAYERS_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer>> RENDER_TYPES = new HashMap<>();
    private Map<ResourceLocation, IPackRenderLayer> renderLayers = new HashMap<>();

    static {
        registerParser(new ResourceLocation(Palladium.MOD_ID, "default"), PackRenderLayer::parse);
        registerParser(new ResourceLocation(Palladium.MOD_ID, "compound"), CompoundPackRenderLayer::parse);

        registerRenderType(new ResourceLocation("minecraft", "solid"), (source, texture) -> ItemRenderer.getArmorFoilBuffer(source, RenderType.entityTranslucent(texture), false, false));
        registerRenderType(new ResourceLocation("minecraft", "glow"), (source, texture) -> ItemRenderer.getArmorFoilBuffer(source, PalladiumRenderTypes.getGlowing(texture), false, false));
    }

    public PackRenderLayerManager() {
        super(AddonParser.GSON, "palladium/render_layers", "render_layers");
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, IPackRenderLayer> builder = ImmutableMap.builder();

        object.forEach((resourceLocation, jsonElement) -> {
            try {
                IPackRenderLayer layer = parseLayer(GsonHelper.convertToJsonObject(jsonElement, "$"));
                builder.put(resourceLocation, layer);
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading render layer {}", resourceLocation, e);
            }
        });

        this.renderLayers = builder.build();
    }

    public IPackRenderLayer getLayer(ResourceLocation id) {
        return this.renderLayers.get(id);
    }

    public static IPackRenderLayer parseLayer(JsonObject json) {
        ResourceLocation parserId = GsonUtil.getAsResourceLocation(json, "type", new ResourceLocation(Palladium.MOD_ID, "default"));

        if (!RENDER_LAYERS_PARSERS.containsKey(parserId)) {
            throw new JsonParseException("Unknown render layer type '" + parserId + "'");
        }

        return RENDER_LAYERS_PARSERS.get(parserId).apply(json);
    }

    public static PackRenderLayerManager getInstance() {
        return INSTANCE;
    }

    public static void registerParser(ResourceLocation id, Function<JsonObject, IPackRenderLayer> function) {
        RENDER_LAYERS_PARSERS.put(id, function);
    }

    public static void registerRenderType(ResourceLocation id, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> function) {
        RENDER_TYPES.put(id, function);
    }

    public static BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> getRenderType(ResourceLocation id) {
        return RENDER_TYPES.get(id);
    }
}
