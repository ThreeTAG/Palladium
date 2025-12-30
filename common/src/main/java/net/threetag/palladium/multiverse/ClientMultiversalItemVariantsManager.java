package net.threetag.palladium.multiverse;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ClientMultiversalItemVariantsManager extends MultiversalItemVariantsManager {

    private static final Map<ResourceLocation, MultiversalItemVariants> ENTRIES = new HashMap<ResourceLocation, MultiversalItemVariants>();
    public static final ClientMultiversalItemVariantsManager INSTANCE = new ClientMultiversalItemVariantsManager();

    @Override
    public Map<ResourceLocation, MultiversalItemVariants> getEntries() {
        return ENTRIES;
    }

    public static void updateEntries(Map<ResourceLocation, MultiversalItemVariants> entries) {
        ENTRIES.clear();
        ENTRIES.putAll(entries);
    }

}
