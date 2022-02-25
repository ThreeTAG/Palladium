package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.provider.IPowerProvider;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientPowerManager extends PowerManager {

    private static Map<ResourceLocation, IPowerProvider> PROVIDERS = new HashMap<>();
    private static final Map<ResourceLocation, Power> POWERS = new HashMap<>();
    public static final ClientPowerManager INSTANCE = new ClientPowerManager();

    @Override
    public Power getPower(ResourceLocation id) {
        return POWERS.get(id);
    }

    @Override
    public Collection<Power> getPowers() {
        return POWERS.values();
    }

    @Override
    public Collection<IPowerProvider> getProviders() {
        return PROVIDERS.values();
    }

    public static void updatePowers(Map<ResourceLocation, Power> powers) {
        PROVIDERS = generateProviders();
        POWERS.values().forEach(Power::invalidate);
        POWERS.clear();
        POWERS.putAll(powers);
    }
}
