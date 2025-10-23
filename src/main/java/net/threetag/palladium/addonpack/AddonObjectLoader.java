package net.threetag.palladium.addonpack;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AddonObjectLoader<T> extends SimpleJsonResourceReloadListener<T> {

    public static ResourceLocation ID_TO_SET = null;
    public final ResourceKey<Registry<T>> resourceKey;
    public final AddonPackManager.RegisterCallback<T> callback;

    public AddonObjectLoader(Codec<T> codec, ResourceKey<Registry<T>> resourceKey, AddonPackManager.RegisterCallback<T> callback) {
        super(codec, FileToIdConverter.registry(resourceKey));
        this.resourceKey = resourceKey;
        this.callback = callback;
    }

    @Override
    protected void apply(Map<ResourceLocation, T> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        AtomicInteger i = new AtomicInteger();

        objects.forEach((id, object) -> {
            this.callback.register(this.resourceKey, id, object);
            i.getAndIncrement();
        });

        AddonPackLog.info("Registered " + i.get() + " addonpack " + this.resourceKey.location().getPath());
    }

    public static <T> ResourceKey<T> resourceId(ResourceKey<Registry<T>> registry, ResourceLocation id) {
        return ResourceKey.create(registry, id);
    }
}

