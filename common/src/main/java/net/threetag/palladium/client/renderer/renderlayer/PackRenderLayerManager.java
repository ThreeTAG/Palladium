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
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.addonpack.parser.AddonParser;
import net.threetag.palladium.client.renderer.PalladiumRenderTypes;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.RenderLayerAbility;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PackRenderLayerManager extends SimpleJsonResourceReloadListener {

    private static PackRenderLayerManager INSTANCE;
    private static final List<Provider> RENDER_LAYERS_PROVIDERS = new ArrayList<>();
    private static final Map<ResourceLocation, Function<JsonObject, IPackRenderLayer>> RENDER_LAYERS_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, BiFunction<MultiBufferSource, ResourceLocation, VertexConsumer>> RENDER_TYPES = new HashMap<>();
    static Function<IPackRenderLayer, RenderLayerStates.State> STATE_FUNCTION = null;
    private Map<ResourceLocation, IPackRenderLayer> renderLayers = new HashMap<>();

    static {
        // Abilities
        registerProvider((entity, layers) -> {
            if (entity instanceof LivingEntity livingEntity) {
                for (AbilityEntry entry : AbilityUtil.getEnabledEntries(livingEntity, Abilities.RENDER_LAYER.get())) {
                    IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(entry.getProperty(RenderLayerAbility.RENDER_LAYER));
                    if (layer != null) {
                        layers.accept(IRenderLayerContext.ofAbility(entity, entry), layer);
                    }
                }
            }
        });
        // Armor
        registerProvider((entity, layers) -> {
            if (entity instanceof LivingEntity livingEntity) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    var stack = livingEntity.getItemBySlot(slot);

                    if (!stack.isEmpty() && stack.getItem() instanceof IAddonItem addonItem && addonItem.getRenderLayerContainer() != null) {
                        var container = addonItem.getRenderLayerContainer();

                        for (ResourceLocation id : container.get(slot.getName())) {
                            IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(id);

                            if (layer != null) {
                                layers.accept(IRenderLayerContext.ofItem(entity, stack), layer);
                            }
                        }
                    }
                }
            }
        });

        registerParser(new ResourceLocation(Palladium.MOD_ID, "default"), PackRenderLayer::parse);
        registerParser(new ResourceLocation(Palladium.MOD_ID, "compound"), CompoundPackRenderLayer::parse);

        registerRenderType(new ResourceLocation("minecraft", "solid"), (source, texture) -> ItemRenderer.getArmorFoilBuffer(source, RenderType.entityTranslucent(texture), false, false));
        registerRenderType(new ResourceLocation("minecraft", "glow"), (source, texture) -> ItemRenderer.getArmorFoilBuffer(source, PalladiumRenderTypes.getGlowing(texture), false, false));
    }

    public PackRenderLayerManager() {
        super(AddonParser.GSON, "palladium/render_layers");
        INSTANCE = this;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.renderLayers.values().forEach(IPackRenderLayer::onUnload);

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
        this.renderLayers.values().forEach(IPackRenderLayer::onLoad);
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

    public static void registerProvider(Provider provider) {
        RENDER_LAYERS_PROVIDERS.add(provider);
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

    public static void forEachLayer(Entity entity, BiConsumer<IRenderLayerContext, IPackRenderLayer> consumer) {
        for (Provider provider : RENDER_LAYERS_PROVIDERS) {
            provider.addRenderLayers(entity, consumer);
        }
    }

    public static void registerStateFunction(Function<IPackRenderLayer, RenderLayerStates.State> function) {
        STATE_FUNCTION = function;
    }

    public interface Provider {

        void addRenderLayers(Entity entity, BiConsumer<IRenderLayerContext, IPackRenderLayer> layers);

    }

}
