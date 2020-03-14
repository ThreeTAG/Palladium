package net.threetag.threecore.addonpacks.item;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import net.minecraft.item.Item;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.addonpacks.AddonPackManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class LoadingOrderParser implements ItemParser.ISpecialItemParser {

    @Override
    public boolean applies(ResourceLocation resourceLocation) {
        return resourceLocation.getPath().startsWith("items/_loading_order");
    }

    @Override
    public void process(IResourceManager resourceManager, ResourceLocation resourceLocation, IForgeRegistry<Item> registry) {
        try (IResource iresource = resourceManager.getResource(resourceLocation)) {
            JsonArray jsonArray = JSONUtils.fromJson(AddonPackManager.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonArray.class);
            List<String> list = ItemParser.LOADING_ORDER.getOrDefault(resourceLocation.getNamespace(), Lists.newLinkedList());
            for (int i = 0; i < jsonArray.size(); i++) {
                list.add(jsonArray.get(i).getAsString());
            }
            ItemParser.LOADING_ORDER.put(resourceLocation.getNamespace(), list);
        } catch (Throwable throwable) {
            ThreeCore.LOGGER.error("Couldn't read custom item loading order from {}", resourceLocation, throwable);
        }
    }
}
