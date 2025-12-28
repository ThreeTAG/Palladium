package net.threetag.palladium.multiverse;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiversalItemVariantsManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static MultiversalItemVariantsManager INSTANCE;
    public Map<ResourceLocation, MultiversalItemVariants> byUniverseId = ImmutableMap.of();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("multiversal_item_variants"), INSTANCE = new MultiversalItemVariantsManager());
    }

    public MultiversalItemVariantsManager() {
        super(GSON, "palladium/multiversal_item_variants");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<ResourceLocation, MultiversalItemVariants> byUniverseId = new HashMap<>();
        AtomicInteger count = new AtomicInteger();
        object.forEach((id, jsonEl) -> {
            try {
                var json = jsonEl.getAsJsonObject();
                var universeId = GsonUtil.getAsResourceLocation(json, "universe");
                var variants = GsonHelper.getAsJsonObject(json, "variants");

                for (String universeIdRaw : variants.keySet()) {
                    var categoryId = ResourceLocation.tryParse(universeIdRaw);

                    if (categoryId == null) {
                        AddonPackLog.error("Invalid category ID {}", universeIdRaw);
                        continue;
                    }

                    for (JsonElement jsonElement : GsonHelper.getAsJsonArray(variants, universeIdRaw)) {
                        var itemId = ResourceLocation.tryParse(jsonElement.getAsString());

                        if (itemId == null || !BuiltInRegistries.ITEM.containsKey(itemId)) {
                            AddonPackLog.error("Invalid item ID {}", jsonElement.getAsString());
                            continue;
                        }

                        byUniverseId.computeIfAbsent(universeId, MultiversalItemVariants::new)
                                .addVariant(categoryId, BuiltInRegistries.ITEM.get(itemId));
                        count.getAndIncrement();
                    }
                }
            } catch (Exception e) {
                AddonPackLog.error("Parsing error loading multiversal item variant {}", id, e);
            }
        });
        this.byUniverseId = ImmutableMap.copyOf(byUniverseId);
        AddonPackLog.info("Loaded {} multiversal item variants", count.get());
    }

    public static MultiversalItemVariantsManager getInstance(Level level) {
        return INSTANCE;
    }

    public List<ResourceLocation> getCategoriesOfItem(Item item) {
        List<ResourceLocation> categories = new ArrayList<>();
        for (MultiversalItemVariants value : this.byUniverseId.values()) {
            categories.addAll(value.getCategoriesOfItem(item));
        }
        return categories;
    }

    public List<Item> getVariantsOf(Item item, Universe universe) {
        List<ResourceLocation> categories = getCategoriesOfItem(item);
        List<Item> variants = new ArrayList<>();
        if (this.byUniverseId.containsKey(universe.getId())) {
            for (ResourceLocation category : categories) {
                for (Item variant : this.byUniverseId.get(universe.getId()).getVariants(category)) {
                    if (variant != item) {
                        variants.add(variant);
                    }
                }
            }
        }
        return variants;
    }
}
