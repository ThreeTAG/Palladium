package net.threetag.palladium.addonpack;

import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.threetag.palladium.addonpack.log.AddonPackLog;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AddonObjectLoader<T> extends SimpleJsonResourceReloadListener<T> {

    public static Identifier ID_TO_SET = null;
    public final ResourceKey<Registry<T>> resourceKey;
    public final AddonPackManager.RegisterCallback<T> callback;

    public AddonObjectLoader(HolderLookup.Provider provider, Codec<T> codec, ResourceKey<Registry<T>> resourceKey, AddonPackManager.RegisterCallback<T> callback) {
        super(provider, codec, resourceKey);
        this.resourceKey = resourceKey;
        this.callback = callback;
    }

    @Override
    protected void apply(Map<Identifier, T> objects, ResourceManager resourceManager, ProfilerFiller profiler) {
        AtomicInteger i = new AtomicInteger();

        objects.forEach((id, object) -> {
            this.callback.register(this.resourceKey, id, object);
            i.getAndIncrement();
        });

        AddonPackLog.info("Registered " + i.get() + " addonpack " + this.resourceKey.identifier().getPath());
    }

    public static <T> ResourceKey<T> resourceId(ResourceKey<Registry<T>> registry, Identifier id) {
        return ResourceKey.create(registry, id);
    }
}

