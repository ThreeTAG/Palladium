package net.threetag.palladium.core.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.platform.PlatformHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Utility class to create custom datapack registries.
 * MUST be initialized during mod initialized so that it's registered early enough for Forge's registry event.
 * <p>
 * Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
 */
public class DataPackRegistryBuilder {

    /**
     * Registers the registry key as a datapack registry, which will cause data to be loaded from
     * a datapack folder based on the registry's name.
     * <p>
     * Data JSONs will be loaded from {@code data/<datapack_namespace>/modid/registryname/}, where {@code modid} is the namespace of the registry key.
     *
     * @param key          The root registry key of the new datapack registry
     * @param dataCodec    The codec to be used for loading data from datapacks on servers
     * @param networkCodec The codec to be used for syncing loaded data to clients.
     *                     If {@code networkCodec} is null, data will not be synced, and clients are not required to have this
     *                     datapack registry to join a server.
     *                     <p>
     *                     If {@code networkCodec} is not null, clients must have this datapack registry/mod
     *                     when joining a server that has this datapack registry/mod.
     */
    public static <T> void create(ResourceKey<? extends Registry<T>> key, Codec<T> dataCodec, @Nullable Codec<T> networkCodec) {
        PlatformHelper.PLATFORM.getRegistries().createDataPackRegistry(key, dataCodec, networkCodec);
    }

}
