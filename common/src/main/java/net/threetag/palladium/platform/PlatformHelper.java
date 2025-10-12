package net.threetag.palladium.platform;

import net.threetag.palladium.Palladium;

import java.util.ServiceLoader;

public class PlatformHelper {

    public static final PlatformService PLATFORM = load(PlatformService.class);

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        Palladium.LOGGER.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }

}
