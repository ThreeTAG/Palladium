package net.threetag.palladium.addonpack;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.item.ArmorMaterialRegistry;

import java.util.Map;

public class ArmorMaterialLoader extends SimpleJsonResourceReloadListener<ArmorMaterial> {

    public ArmorMaterialLoader() {
        super(ArmorMaterialRegistry.DIRECT_CODEC, FileToIdConverter.json("armor_material"));
    }

    @Override
    protected void apply(Map<Identifier, ArmorMaterial> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        object.forEach(ArmorMaterialRegistry::register);
        AddonPackLog.info("Registered " + object.size() + " addonpack armor materials");
    }
}
