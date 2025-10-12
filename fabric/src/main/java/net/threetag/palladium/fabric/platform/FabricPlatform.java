package net.threetag.palladium.fabric.platform;

import net.threetag.palladium.platform.*;

public class FabricPlatform implements PlatformService {

    private static final AddonPackManagerService ADDON_PACK_MANAGER = new FabricAddonPackManager();
    private static final AttachmentService ATTACHMENT_SERVICE = new FabricAttachments();
    private static final FabricBlocks BLOCKS = new FabricBlocks();
    private static final FabricEntities ENTITIES = new FabricEntities();
    private static final FabricRegistries REGISTRIES = new FabricRegistries();
    private static final FabricClientRegistries CLIENT_REGISTRIES = new FabricClientRegistries();

    @Override
    public AddonPackManagerService getAddonPackManager() {
        return ADDON_PACK_MANAGER;
    }

    @Override
    public AttachmentService getAttachments() {
        return ATTACHMENT_SERVICE;
    }

    @Override
    public BlockService getBlocks() {
        return BLOCKS;
    }

    @Override
    public EntityService getEntities() {
        return ENTITIES;
    }

    @Override
    public RegistryService getRegistries() {
        return REGISTRIES;
    }

    @Override
    public ClientRegistryService getClientRegistries() {
        return CLIENT_REGISTRIES;
    }
}
