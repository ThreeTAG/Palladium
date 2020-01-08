package net.threetag.threecore.addonpacks.item;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.IForgeRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.addonpacks.ThreeCoreAddonPacks;
import net.threetag.threecore.util.item.ItemGroupRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ItemGroupParser implements ItemParser.ISpecialItemParser {

    @Override
    public boolean applies(ResourceLocation resourceLocation) {
        return resourceLocation.getPath().startsWith("items/_item_groups");
    }

    @Override
    public void process(IResourceManager resourceManager, ResourceLocation resourceLocation, IForgeRegistry<Item> registry) {
        try (IResource iresource = resourceManager.getResource(resourceLocation)) {
            JsonArray jsonArray = JSONUtils.fromJson(ThreeCoreAddonPacks.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                String name = JSONUtils.getString(jsonObject, "name");
                ItemGroupRegistry.addItemGroup(name, () -> CraftingHelper.getItemStack(JSONUtils.getJsonObject(jsonObject, "icon"), true));
                ThreeCore.LOGGER.info("Registered addonpack item group {} from {}!", name, resourceLocation);
            }
        } catch (Throwable throwable) {
            ThreeCore.LOGGER.error("Couldn't read addonpack item group from {}", resourceLocation, throwable);
        }
    }
}
