package net.threetag.palladium.multiverse;

import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ClientMultiversalManager extends MultiverseManager {

    private static final Map<ResourceLocation, Universe> UNIVERSES = new HashMap<>();
    public static final MultiverseManager INSTANCE = new ClientMultiversalManager();

    @Override
    public Map<ResourceLocation, Universe> getUniverses() {
        return ImmutableMap.copyOf(UNIVERSES);
    }

    public static void updateUniverses(Map<ResourceLocation, Universe> universes) {
        UNIVERSES.clear();
        UNIVERSES.putAll(universes);
    }
}
