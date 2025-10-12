package net.threetag.palladium.neoforge.platform;

import net.threetag.palladium.platform.*;

public class NeoForgePlatform implements PlatformService {

    private static final AddonPackManagerService ADDON_PACK_MANAGER = new NeoForgeAddonPackManager();
    private static final AttachmentService ATTACHMENT_SERVICE = new NeoForgeAttachments();
    private static final NeoForgeBlocks BLOCKS = new NeoForgeBlocks();
    private static final NeoForgeEntities ENTITIES = new NeoForgeEntities();
    private static final NeoForgeRegistries REGISTRIES = new NeoForgeRegistries();
    private static final NeoForgeClientRegistries CLIENT_REGISTRIES = new NeoForgeClientRegistries();

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
