package net.threetag.palladium.addonpack.parser;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.entity.BasicItemListing;
import net.threetag.palladium.util.json.GsonUtil;
import net.threetag.palladiumcore.event.LifecycleEvents;
import net.threetag.palladiumcore.registry.VillagerTradeRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VillagerTradeParser extends SimpleJsonResourceReloadListener {

    private Map<ResourceLocation, JsonObject> trades = ImmutableMap.of();

    public VillagerTradeParser() {
        super(AddonParser.GSON, "villager_trades");

        LifecycleEvents.SETUP.register(() -> {
            for (JsonObject json : trades.values()) {
                ResourceLocation profId = GsonUtil.getAsResourceLocation(json, "villager_profession");
                List<BasicItemListing> listings = new ArrayList<>();

                GsonUtil.forEachInListOrPrimitive(json.get("trades"), el -> {
                    JsonObject tradeJson = GsonHelper.convertToJsonObject(el, "trades");
                    listings.add(parseListing(tradeJson));
                });

                if (profId.toString().equals("minecraft:wandering_trader")) {
                    boolean rare = GsonHelper.getAsBoolean(json, "rare", false);
                    VillagerTradeRegistry.registerForWanderingTrader(rare, listings.toArray(new BasicItemListing[0]));
                } else {
                    if (BuiltInRegistries.VILLAGER_PROFESSION.containsKey(profId)) {
                        VillagerTradeRegistry.registerForProfession(BuiltInRegistries.VILLAGER_PROFESSION.get(profId), GsonUtil.getAsIntMin(json, "level", 1), listings.toArray(new BasicItemListing[0]));
                    } else {
                        throw new JsonParseException("Unknown villager profession " + profId);
                    }
                }
            }
        });
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        ImmutableMap.Builder<ResourceLocation, JsonObject> builder = ImmutableMap.builder();

        object.forEach((id, jsonElement) -> {
            JsonObject json = GsonHelper.convertToJsonObject(jsonElement, "top element");
            builder.put(id, json);
        });

        this.trades = builder.build();
    }

    public static BasicItemListing parseListing(JsonObject json) {
        ItemStack price = GsonUtil.getAsItemStack(json, "price");
        ItemStack price2 = GsonUtil.getAsItemStack(json, "price_2", ItemStack.EMPTY);
        ItemStack forSale = GsonUtil.getAsItemStack(json, "for_sale");
        int maxTrades = GsonUtil.getAsIntMin(json, "max_trades", 1);
        int xp = GsonUtil.getAsIntMin(json, "max_trades", 0);
        float priceMult = GsonHelper.getAsFloat(json, "price_multiplier", 1);
        return new BasicItemListing(price, price2, forSale, maxTrades, xp, priceMult);
    }
}
