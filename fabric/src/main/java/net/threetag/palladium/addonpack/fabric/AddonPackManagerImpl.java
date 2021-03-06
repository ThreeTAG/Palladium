package net.threetag.palladium.addonpack.fabric;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;

public class AddonPackManagerImpl {

    public static RepositorySource getWrappedPackFinder(RepositorySource folderPackFinder) {
        return (infoConsumer, infoFactory) -> folderPackFinder.loadPacks(infoConsumer,
                (string, component, bl, supplier, packMetadataSection, position, packSource) ->
                        infoFactory.create("addonpack:" + string, component, true, supplier, packMetadataSection, position, packSource));
    }

    public static PackType getPackType() {
        for (PackType type : PackType.values()) {
            if (type.getDirectory().equalsIgnoreCase("addon")) {
                return type;
            }
        }
        return null;
    }

}
