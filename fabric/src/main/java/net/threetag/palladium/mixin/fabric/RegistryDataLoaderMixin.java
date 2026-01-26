package net.threetag.palladium.mixin.fabric;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {

    @Redirect(
            method = "loadRegistryContents",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/resources/FileToIdConverter;listMatchingResources(Lnet/minecraft/server/packs/resources/ResourceManager;)Ljava/util/Map;"
            )
    )
    private static Map<ResourceLocation, Resource> redirectResourceListing(FileToIdConverter instance, ResourceManager resourceManager) {
        Map<ResourceLocation, Resource> source = instance.listMatchingResources(resourceManager);
        Map<ResourceLocation, Resource> modified = new HashMap<>();

        for (Map.Entry<ResourceLocation, Resource> entry : source.entrySet()) {
            Resource resource = entry.getValue();
            try {
                Reader reader = resource.openAsReader();
                JsonElement jsonElement = JsonParser.parseReader(reader);

                if (!jsonElement.isJsonObject() || ResourceConditions.objectMatchesConditions(jsonElement.getAsJsonObject())) {
                    modified.put(entry.getKey(), resource);
                }

                reader.close();
            } catch (Exception e) {
                modified.put(entry.getKey(), resource);
            }
        }

        return modified;
    }

}
