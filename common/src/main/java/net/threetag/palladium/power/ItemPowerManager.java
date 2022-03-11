package net.threetag.palladium.power;

import com.google.gson.*;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ItemPowerManager extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static ItemPowerManager INSTANCE;

    private Map<String, Map<Item, Power>> itemPowers = new HashMap<>();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, INSTANCE = new ItemPowerManager());
    }

    public ItemPowerManager() {
        super(GSON, "item_powers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.itemPowers.clear();
        object.forEach((id, json) -> {
            String slot = GsonHelper.getAsString(json.getAsJsonObject(), "slot");
            Map<Item, Power> map = this.itemPowers.computeIfAbsent(slot, s -> new HashMap<>());
            JsonObject items = GsonHelper.getAsJsonObject(json.getAsJsonObject(), "items");

            for (String s : items.keySet()) {
                ResourceLocation itemId = new ResourceLocation(s);
                try {
                    ResourceLocation powerId = GsonUtil.getAsResourceLocation(items, s);
                    Power power = PowerManager.getInstance(null).getPower(powerId);

                    if (!Registry.ITEM.containsKey(itemId)) {
                        throw new JsonParseException("Unknown item '" + itemId + "'");
                    }

                    if (power == null) {
                        throw new JsonParseException("Unknown power '" + powerId + "'");
                    }

                    Item item = Registry.ITEM.get(itemId);
                    map.put(item, power);
                } catch (Exception e) {
                    Palladium.LOGGER.error("Parsing error loading power for item {}", id.toString() + "#" + itemId, e);
                }
            }
        });
        Palladium.LOGGER.info("Loaded {} item powers", this.itemPowers.size());
    }

    @Nullable
    public Power getPowerForItem(String slot, Item item) {
        return this.itemPowers.containsKey(slot) ? this.itemPowers.get(slot).get(item) : null;
    }

    public static ItemPowerManager getInstance() {
        return INSTANCE;
    }
}
