package net.threetag.palladium.client.trail;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class TrailManager extends SimpleJsonResourceReloadListener<TrailConfiguration> {

    public static final ResourceLocation ID = Palladium.id("trails");
    public static final TrailManager INSTANCE = new TrailManager();

    public Map<ResourceLocation, TrailConfiguration> byName = ImmutableMap.of();

    public TrailManager() {
        super(TrailConfiguration.CODEC, FileToIdConverter.json("palladium/trails"));
    }

    @Override
    protected void apply(Map<ResourceLocation, TrailConfiguration> objects, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ImmutableMap.Builder<ResourceLocation, TrailConfiguration> builder = ImmutableMap.builder();
        objects.forEach(builder::put);
        this.byName = builder.build();
        AddonPackLog.info("Loaded {} trails", this.byName.size());
    }

    @Nullable
    public TrailConfiguration get(ResourceLocation id) {
        return this.byName.get(id);
    }
}
