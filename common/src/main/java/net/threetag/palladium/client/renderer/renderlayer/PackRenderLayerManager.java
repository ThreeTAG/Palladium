package net.threetag.palladium.client.renderer.renderlayer;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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
import net.threetag.palladium.client.renderer.item.armor.ArmorRendererData;
import net.threetag.palladium.item.ArmorWithRenderer;
import net.threetag.palladium.item.IAddonItem;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.RenderLayerProviderAbility;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.json.GsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PackRenderLayerManager extends SimpleJsonResourceReloadListener {

    private static PackRenderLayerManager INSTANCE;
    private static final List<Provider> RENDER_LAYERS_PROVIDERS = new ArrayList<>();
    private static final Map<ResourceLocation, Function<JsonObject, IPackRenderLayer>> RENDER_LAYERS_PARSERS = new HashMap<>();
    private static final Map<ResourceLocation, RenderTypeFunction> RENDER_TYPES = new HashMap<>();
    private Map<ResourceLocation, IPackRenderLayer> renderLayers = new HashMap<>();

    static {
        // Abilities
        registerProvider((entity, layers) -> {
            if (entity instanceof LivingEntity livingEntity) {
                var manager = PackRenderLayerManager.getInstance();
                for (AbilityEntry entry : AbilityUtil.getEnabledRenderLayerEntries(livingEntity)) {
                    IPackRenderLayer layer = ((RenderLayerProviderAbility) entry.getConfiguration().getAbility()).getRenderLayer(entry, livingEntity, manager);
                    if (layer != null) {
                        layers.accept(DataContext.forAbility(livingEntity, entry), layer);
                    }
                }
            }
        });

        // Armor
        registerProvider((entity, layers) -> {
            if (entity instanceof LivingEntity livingEntity) {
                for (EquipmentSlot slot : EquipmentSlot.values()) {
                    var stack = livingEntity.getItemBySlot(slot);

                    if (!stack.isEmpty()) {
                        var context = DataContext.forArmorInSlot(livingEntity, slot);

                        if (stack.getItem() instanceof IAddonItem addonItem && addonItem.getRenderLayerContainer() != null) {
                            var container = addonItem.getRenderLayerContainer();

                            for (ResourceLocation id : container.get(slot.getName())) {
                                IPackRenderLayer layer = PackRenderLayerManager.getInstance().getLayer(id);

                                if (layer != null) {
                                    layers.accept(context, layer);
                                }
                            }
                        }

                        if (slot.isArmor() && stack.getItem() instanceof ArmorWithRenderer armorWithRenderer && armorWithRenderer.getCachedArmorRenderer() instanceof ArmorRendererData renderer) {
                            for (IPackRenderLayer layer : renderer.getRenderLayers()) {
                                layers.accept(context, layer);
                            }
                        }
                    }
                }
            }
        });

        registerParser(new ResourceLocation(Palladium.MOD_ID, "default"), PackRenderLayer::parse);
        registerParser(new ResourceLocation(Palladium.MOD_ID, "compound"), CompoundPackRenderLayer::parse);
        registerParser(new ResourceLocation(Palladium.MOD_ID, "skin_overlay"), SkinOverlayPackRenderLayer::parse);
        registerParser(new ResourceLocation(Palladium.MOD_ID, "lightning_sparks"), LightningSparksRenderLayer::parse);

        registerRenderType(new ResourceLocation("minecraft", "solid"), (source, texture, glint) -> ItemRenderer.getArmorFoilBuffer(source, RenderType.entityTranslucent(texture), false, glint));
        registerRenderType(new ResourceLocation("minecraft", "glow"), (source, texture, glint) -> ItemRenderer.getArmorFoilBuffer(source, PalladiumRenderTypes.getGlowing(texture), false, glint));
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

    public static void registerRenderType(ResourceLocation id, RenderTypeFunction function) {
        RENDER_TYPES.put(id, function);
    }

    public static RenderTypeFunction getRenderType(ResourceLocation id) {
        return RENDER_TYPES.get(id);
    }

    public static void forEachLayer(Entity entity, BiConsumer<DataContext, IPackRenderLayer> consumer) {
        for (Provider provider : RENDER_LAYERS_PROVIDERS) {
            provider.addRenderLayers(entity, consumer);
        }
    }

    public interface Provider {

        void addRenderLayers(Entity entity, BiConsumer<DataContext, IPackRenderLayer> layers);

    }

}
