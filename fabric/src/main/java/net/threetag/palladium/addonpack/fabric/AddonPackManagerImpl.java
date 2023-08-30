package net.threetag.palladium.addonpack.fabric;

import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.minecraft.server.packs.repository.RepositorySource;
import net.threetag.palladium.addonpack.AddonPackManager;

import java.util.Objects;

public class AddonPackManagerImpl {

    public static RepositorySource getModRepositorySource() {
        return new ModResourcePackCreator(Objects.requireNonNull(AddonPackManager.getPackType()));
    }

}
