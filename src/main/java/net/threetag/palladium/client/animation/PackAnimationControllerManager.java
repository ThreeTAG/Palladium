package net.threetag.palladium.client.animation;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.HashMap;
import java.util.Map;

public class PackAnimationControllerManager extends SimpleJsonResourceReloadListener<PackAnimationController> {

    public static final Identifier ID = Palladium.id("animation_controllers");
    public static final PackAnimationControllerManager INSTANCE = new PackAnimationControllerManager();

    private final Map<Identifier, PackAnimationController> byName = new HashMap<>();

    protected PackAnimationControllerManager() {
        super(PackAnimationController.CODEC, FileToIdConverter.json("palladium/animation_controllers"));
    }

    @Override
    protected void apply(Map<Identifier, PackAnimationController> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " animation controllers");
    }

    public PackAnimationController get(Identifier id) {
        return this.byName.get(id);
    }
}