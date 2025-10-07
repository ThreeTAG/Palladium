package net.threetag.palladium.addonpack;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ToolMaterial;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.item.ToolMaterialRegistry;

import java.util.Map;

public class ToolMaterialLoader extends SimpleJsonResourceReloadListener<ToolMaterial> {

    public ToolMaterialLoader() {
        super(ToolMaterialRegistry.DIRECT_CODEC, FileToIdConverter.json("tool_material"));
    }

    @Override
    protected void apply(Map<ResourceLocation, ToolMaterial> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        object.forEach(ToolMaterialRegistry::register);
        AddonPackLog.info("Registered " + object.size() + " addonpack tool materials");
    }
}
