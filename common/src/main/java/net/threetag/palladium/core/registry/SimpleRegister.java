package net.threetag.palladium.core.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.platform.PlatformHelper;

public class SimpleRegister {

    public static <T> void register(ResourceKey<Registry<T>> registryResourceKey, ResourceLocation id, T object) {
        PlatformHelper.PLATFORM.getRegistries().register(registryResourceKey, id, object);
    }

}
