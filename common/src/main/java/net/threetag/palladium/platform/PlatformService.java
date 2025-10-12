package net.threetag.palladium.platform;

public interface PlatformService {

    AddonPackManagerService getAddonPackManager();

    AttachmentService getAttachments();

    BlockService getBlocks();

    EntityService getEntities();

    RegistryService getRegistries();

    ClientRegistryService getClientRegistries();

}
