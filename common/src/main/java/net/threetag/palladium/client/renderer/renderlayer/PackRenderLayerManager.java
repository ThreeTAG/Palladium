package net.threetag.palladium.client.renderer.renderlayer;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.parser.AddonParser;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class PackRenderLayerManager extends SimpleJsonResourceReloadListener {

    private static PackRenderLayerManager INSTANCE;
    private static Map<ResourceLocation, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer>> RENDER_TYPES = new HashMap<>();
    private Map<ResourceLocation, IPackRenderLayer> renderLayers;

    static {
        registerRenderType(new ResourceLocation("minecraft", "solid"), (source, texture) -> ItemRenderer.getArmorFoilBuffer(source, RenderType.armorCutoutNoCull(texture), false, false));
        registerRenderType(new ResourceLocation("minecraft", "glow"), (source, texture) -> ItemRenderer.getArmorFoilBuffer(source, PalladiumRenderTypes.getGlowing(texture), false, false));
    }

    public PackRenderLayerManager() {
        super(AddonParser.GSON, "render_layers");
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
                Palladium.LOGGER.error("Parsing error loading render layer {}", resourceLocation, e);
            }
        });

        this.renderLayers = builder.build();
    }

    public IPackRenderLayer getLayer(ResourceLocation id) {
        return this.renderLayers.get(id);
    }

    public static IPackRenderLayer parseLayer(JsonObject json) {
        ModelLayerLocation location = GsonUtil.getAsModelLayerLocation(json, "model");
        ResourceLocation texture = GsonUtil.getAsResourceLocation(json, "texture");
        var renderType = RENDER_TYPES.get(new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")));

        if (renderType == null) {
            throw new JsonParseException("Unknown render type '" + new ResourceLocation(GsonHelper.getAsString(json, "render_type", "solid")) + "'");
        }

        return new PackRenderLayer(location, texture, renderType);
    }

    public static PackRenderLayerManager getInstance() {
        return INSTANCE;
    }

    public static void registerRenderType(ResourceLocation id, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer> function) {
        RENDER_TYPES.put(id, function);
    }
}
