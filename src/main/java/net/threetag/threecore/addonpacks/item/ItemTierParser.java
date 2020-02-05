package net.threetag.threecore.addonpacks.item;

import com.google.gson.JsonArray;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.addonpacks.AddonPackManager;
import net.threetag.threecore.item.ItemTierRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ItemTierParser implements ItemParser.ISpecialItemParser {

    @Override
    public boolean applies(ResourceLocation resourceLocation) {
        return resourceLocation.getPath().startsWith("items/_item_tiers");
    }

    @Override
    public void process(IResourceManager resourceManager, ResourceLocation resourceLocation, IForgeRegistry<Item> registry) {
        try (IResource iresource = resourceManager.getResource(resourceLocation)) {
            JsonArray jsonArray = JSONUtils.fromJson(AddonPackManager.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                String name = JSONUtils.getString(jsonArray.get(i).getAsJsonObject(), "name");
                IItemTier tier = ItemParser.parseItemTier(jsonArray.get(i).getAsJsonObject());
                ItemTierRegistry.addItemTier(name, tier);
                ThreeCore.LOGGER.info("Registered addonpack item tier {} from {}!", name, resourceLocation);
            }
        } catch (Throwable throwable) {
            ThreeCore.LOGGER.error("Couldn't read addonpack item tier from {}", resourceLocation, throwable);
        }
    }
}
