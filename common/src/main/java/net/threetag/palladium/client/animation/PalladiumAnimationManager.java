package net.threetag.palladium.client.animation;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.HashMap;
import java.util.Map;

public class PalladiumAnimationManager extends SimpleJsonResourceReloadListener<PalladiumAnimation> {

    public static final ResourceLocation ID = Palladium.id("animations");
    public static final PalladiumAnimationManager INSTANCE = new PalladiumAnimationManager();

    private final Map<ResourceLocation, PalladiumAnimation> byName = new HashMap<>();

    public PalladiumAnimationManager() {
        super(PalladiumAnimation.CODEC, FileToIdConverter.json("palladium/animations"));
    }

    @Override
    protected void apply(Map<ResourceLocation, PalladiumAnimation> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.byName.clear();
        this.byName.putAll(objects);
        AddonPackLog.info("Loaded " + objects.size() + " animations");
    }

    public PalladiumAnimation get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
