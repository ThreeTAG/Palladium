package net.threetag.palladium.platform;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryBuilder;
import org.jetbrains.annotations.Nullable;

public interface RegistryService {

    <T> void register(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object);

    <T> Registry<T> createRegistry(RegistryBuilder<T> registryBuilder);

    <T> void createDataPackRegistry(ResourceKey<? extends Registry<T>> key, Codec<T> dataCodec, @Nullable Codec<T> networkCodec);

    <T> DeferredRegister<T> createDeferredRegister(String modId, ResourceKey<? extends Registry<T>> resourceKey);

    DeferredRegister.Items createDeferredItemRegister(String modId);

}
