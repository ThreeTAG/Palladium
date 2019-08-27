package com.threetag.threecore.addonpacks;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.util.item.ItemGroupRegistry;
import net.minecraft.client.util.JSONException;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

import static com.threetag.threecore.addonpacks.ThreeCoreAddonPacks.GSON;

public class ItemParser {

    public static final int resourcePrefix = "items/".length();
    public static final int resourceSuffix = ".json".length();
    private static Map<ResourceLocation, BiFunction<JsonObject, Item.Properties, Item>> itemFunctions = Maps.newHashMap();

    static {
        registerItemParser(new ResourceLocation(ThreeCore.MODID, "default"), (j, p) -> new Item(p));
    }

    public static void registerItemParser(ResourceLocation resourceLocation, BiFunction<JsonObject, Item.Properties, Item> function) {
        Preconditions.checkNotNull(resourceLocation);
        Preconditions.checkNotNull(function);
        itemFunctions.put(resourceLocation, function);
    }

    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> e) {
        IResourceManager resourceManager = ThreeCoreAddonPacks.getInstance().getResourceManager();

        for (ResourceLocation resourcelocation : resourceManager.getAllResourceLocations("items", (name) -> name.endsWith(".json"))) {
            String s = resourcelocation.getPath();
            ResourceLocation resourcelocation1 = new ResourceLocation(resourcelocation.getNamespace(), s.substring(resourcePrefix, s.length() - resourceSuffix));

            try (IResource iresource = resourceManager.getResource(resourcelocation)) {
                Item item = parse(JSONUtils.fromJson(GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonObject.class));
                if (item != null) {
                    item.setRegistryName(resourcelocation1);
                    e.getRegistry().register(item);
                    ThreeCore.LOGGER.info("Registered addonpack item {}!", resourcelocation1);
                }
            } catch (Throwable throwable) {
                ThreeCore.LOGGER.error("Couldn't read addonpack item {} from {}", resourcelocation1, resourcelocation, throwable);
            }

        }
    }

    public static Item parse(JsonObject json) throws JSONException {
        BiFunction<JsonObject, Item.Properties, Item> function = itemFunctions.get(new ResourceLocation(JSONUtils.getString(json, "type")));

        if (function == null)
            throw new JSONException("The item type '" + JSONUtils.getString(json, "type") + "' does not exist!");

        Item item = function.apply(json, parseProperties(JSONUtils.getJsonObject(json, "properties")));
        return Objects.requireNonNull(item);
    }

    public static Item.Properties parseProperties(JsonObject json) {
        Item.Properties properties = new Item.Properties();

        if (JSONUtils.hasField(json, "max_stack_size"))
            properties.maxStackSize(JSONUtils.getInt(json, "max_stack_size", 64));

        if (JSONUtils.hasField(json, "max_damage"))
            properties.maxDamage(JSONUtils.getInt(json, "max_damage"));

        if (JSONUtils.hasField(json, "container_item"))
            properties.containerItem(ForgeRegistries.ITEMS.getValue(new ResourceLocation(JSONUtils.getString(json, "container_item"))));

        if (JSONUtils.hasField(json, "group"))
            properties.group(ItemGroupRegistry.getItemGroup(JSONUtils.getString(json, "group")));

        if (JSONUtils.hasField(json, "rarity"))
            properties.rarity(Rarity.valueOf(JSONUtils.getString(json, "rarity")));

        if (JSONUtils.hasField(json, "tool_types")) {
            JsonArray array = JSONUtils.getJsonArray(json, "tool_types");

            for (int i = 0; i < array.size(); i++) {
                JsonObject jsonObject = array.get(i).getAsJsonObject();
                properties.addToolType(ToolType.get(JSONUtils.getString(jsonObject, "tool")), JSONUtils.getInt(jsonObject, "level"));
            }
        }

        // TODO Food

        return properties;
    }

}
