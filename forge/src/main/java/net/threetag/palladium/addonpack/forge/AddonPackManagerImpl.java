package net.threetag.palladium.addonpack.forge;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;

public class AddonPackManagerImpl {

    public static RepositorySource getWrappedPackFinder(RepositorySource folderPackFinder) {
        return (infoConsumer, infoFactory) -> folderPackFinder.loadPacks(infoConsumer, (string, component, bl, supplier, metadataSection, position, packSource, b) ->
                infoFactory.create("addonpack:" + string, component, true, supplier, metadataSection, position, packSource, b));
    }

    public static PackType getPackType() {
        AddonPackType.init();
        return AddonPackType.ADDON;
    }

}
