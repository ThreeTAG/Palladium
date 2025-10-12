package net.threetag.palladium.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

public class RegistryBuilder<T> {

    public static <T> RegistryBuilder<T> create(ResourceKey<Registry<T>> resourceKey) {
        return new RegistryBuilder<>(resourceKey);
    }

    private final ResourceKey<Registry<T>> resourceKey;
    @Nullable
    private ResourceLocation defaultKey;
    private boolean sync = false;

    private RegistryBuilder(ResourceKey<Registry<T>> resourceKey) {
        this.resourceKey = resourceKey;
    }

    public RegistryBuilder<T> defaultKey(ResourceLocation key) {
        this.defaultKey = key;
        return this;
    }

    public RegistryBuilder<T> defaultKey(ResourceKey<T> key) {
        this.defaultKey = key.location();
        return this;
    }

    public void sync(boolean sync) {
        this.sync = sync;
    }

    public ResourceKey<Registry<T>> getResourceKey() {
        return this.resourceKey;
    }

    public @Nullable ResourceLocation getDefaultKey() {
        return this.defaultKey;
    }

    public boolean isSynced() {
        return this.sync;
    }

    public Registry<T> build() {
        return PlatformHelper.PLATFORM.getRegistries().createRegistry(this);
    }
}
