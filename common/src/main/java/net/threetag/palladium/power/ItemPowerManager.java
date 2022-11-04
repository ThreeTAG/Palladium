package net.threetag.palladium.power;

import com.google.gson.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.LegacySupportJsonReloadListener;
import net.threetag.palladiumcore.registry.ReloadListenerRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPowerManager extends LegacySupportJsonReloadListener {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static ItemPowerManager INSTANCE;

    private final Map<String, Map<Item, List<Power>>> itemPowers = new HashMap<>();

    public static void init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, Palladium.id("item_powers"), INSTANCE = new ItemPowerManager());
    }

    public ItemPowerManager() {
        super(GSON, "palladium/item_powers", "item_powers");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.itemPowers.clear();
        object.forEach((id, json) -> {
            try {
                JsonObject jsonObject = GsonHelper.convertToJsonObject(json, "$");
                String slot = GsonHelper.getAsString(jsonObject, "slot");

                List<Power> powers = new ArrayList<>();
                if (jsonObject.get("power").isJsonPrimitive()) {
                    var power = PowerManager.getInstance(null).getPower(new ResourceLocation(jsonObject.get("power").getAsString()));

                    if (power == null) {
                        AddonPackLog.warning("Unknown power used for item '" + jsonObject.get("power").getAsString() + "'");
                    } else {
                        powers.add(power);
                    }
                } else if (jsonObject.get("power").isJsonArray()) {
                    for (JsonElement jsonElement : GsonHelper.getAsJsonArray(jsonObject, "power")) {
                        var power = PowerManager.getInstance(null).getPower(new ResourceLocation(jsonElement.getAsString()));

                        if (power == null) {
                            AddonPackLog.warning("Unknown power used for item '" + jsonElement.getAsString() + "'");
                        } else {
                            powers.add(power);
                        }
                    }
                } else {
                    throw new JsonSyntaxException("Expected power to be string or array of strings");
                }

                List<Item> items = new ArrayList<>();
                if (jsonObject.get("item").isJsonPrimitive()) {
                    ResourceLocation itemId = new ResourceLocation(jsonObject.get("item").getAsString());

                    if (!Registry.ITEM.containsKey(itemId)) {
                        throw new JsonParseException("Unknown item '" + itemId + "'");
                    }

                    items = List.of(Registry.ITEM.get(itemId));
                } else if (jsonObject.get("item").isJsonArray()) {
                    for (JsonElement jsonElement : GsonHelper.getAsJsonArray(jsonObject, "item")) {
                        ResourceLocation itemId = new ResourceLocation(jsonElement.getAsString());

                        if (!Registry.ITEM.containsKey(itemId)) {
                            throw new JsonParseException("Unknown item '" + itemId + "'");
                        }

                        items.add(Registry.ITEM.get(itemId));
                    }
                } else {
                    throw new JsonSyntaxException("Expected item to be string or array of strings");
                }

                for (Item item : items) {
                    this.itemPowers.computeIfAbsent(slot, s -> new HashMap<>()).computeIfAbsent(item, item1 -> new ArrayList<>()).addAll(powers);
                }
            } catch (Exception exception) {
                AddonPackLog.error("Parsing error loading item powers {}", id, exception);
            }
        });
        AddonPackLog.info("Loaded {} item powers", this.itemPowers.size());
    }

    @Nullable
    public List<Power> getPowerForItem(String slot, Item item) {
        return this.itemPowers.containsKey(slot) ? this.itemPowers.get(slot).get(item) : null;
    }

    public static ItemPowerManager getInstance() {
        return INSTANCE;
    }
}
