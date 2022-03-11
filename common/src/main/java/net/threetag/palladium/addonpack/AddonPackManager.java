package net.threetag.palladium.addonpack;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.minecraft.Util;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import net.threetag.palladium.addonpack.parser.CreativeModeTabParser;
import net.threetag.palladium.addonpack.parser.ItemParser;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AddonPackManager {

    private static AddonPackManager INSTANCE;
    private static PackType PACK_TYPE;
    public static boolean IGNORE_INJECT = false;

    public static AddonPackManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddonPackManager();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance();
    }

    private final ReloadableResourceManager resourceManager;
    private final RepositorySource folderPackFinder;
    private final PackRepository packList;

    private AddonPackManager() {
        IGNORE_INJECT = true;
        this.resourceManager = new ReloadableResourceManager(getPackType());
        this.folderPackFinder = new FolderRepositorySource(getLocation(), PackSource.DEFAULT);
        this.packList = new PackRepository(getPackType(), this.folderPackFinder);
        IGNORE_INJECT = false;

        this.resourceManager.registerReloadListener(new CreativeModeTabParser());
        this.resourceManager.registerReloadListener(new ItemParser());

        this.beginLoading(Util.backgroundExecutor(), Runnable::run);
    }

    public File getLocation() {
        File folder = Platform.getGameFolder().resolve("addonpacks").toFile();
        if (!folder.exists() && !folder.mkdirs())
            throw new RuntimeException("Could not create addonpacks directory! Please create the directory yourself, or make sure the name is not taken by a file and you have permission to create directories.");
        return folder;
    }

    public RepositorySource getWrappedPackFinder() {
        return getWrappedPackFinder(this.folderPackFinder);
    }

    @ExpectPlatform
    public static RepositorySource getWrappedPackFinder(RepositorySource folderPackFinder) {
        throw new AssertionError();
    }

    public static PackType getPackType() {
        if (PACK_TYPE == null) {
            for (PackType type : PackType.values()) {
                if (type.getDirectory().equalsIgnoreCase("addon")) {
                    PACK_TYPE = type;
                    return PACK_TYPE;
                }
            }
        }
        return PACK_TYPE;
    }

    public CompletableFuture<AddonPackManager> beginLoading(Executor backgroundExecutor, Executor gameExecutor) {
        this.packList.reload();
        // Enable all packs
        this.packList.setSelected(this.packList.getAvailableIds());

        return this.resourceManager
                .createReload(backgroundExecutor, gameExecutor, CompletableFuture.completedFuture(Unit.INSTANCE), this.packList.openAllSelected())
                .done().whenComplete((unit, throwable) -> {
                    if (throwable != null) {
                        this.resourceManager.close();
                        throwable.printStackTrace();
                    }
                })
                .thenApply((unit) -> this);
    }

}
