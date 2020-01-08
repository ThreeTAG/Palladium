package net.threetag.threecore.addonpacks.item;

import com.google.gson.JsonArray;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.addonpacks.ThreeCoreAddonPacks;
import net.threetag.threecore.util.item.ArmorMaterialRegistry;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ArmorMaterialParser implements ItemParser.ISpecialItemParser {

    @Override
    public boolean applies(ResourceLocation resourceLocation) {
        return resourceLocation.getPath().startsWith("items/_armor_materials");
    }

    @Override
    public void process(IResourceManager resourceManager, ResourceLocation resourceLocation, IForgeRegistry<Item> registry) {
        try (IResource iresource = resourceManager.getResource(resourceLocation)) {
            JsonArray jsonArray = JSONUtils.fromJson(ThreeCoreAddonPacks.GSON, new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8)), JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++) {
                IArmorMaterial material = ItemParser.parseArmorMaterial(jsonArray.get(i).getAsJsonObject());
                ArmorMaterialRegistry.addArmorMaterial(material.getName(), material);
                ThreeCore.LOGGER.info("Registered addonpack armor material {} from {}!", material.getName(), resourceLocation);
            }
        } catch (Throwable throwable) {
            ThreeCore.LOGGER.error("Couldn't read addonpack armor materials from {}", resourceLocation, throwable);
        }
    }
}
