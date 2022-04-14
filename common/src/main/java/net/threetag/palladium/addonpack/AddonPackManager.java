package net.threetag.palladium.addonpack;

import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.platform.Platform;
import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.Util;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.*;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Unit;
import net.threetag.palladium.addonpack.parser.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class AddonPackManager {

    private static AddonPackManager INSTANCE;
    public static boolean IGNORE_INJECT = false;
    public static ItemParser ITEM_PARSER;
    private static Map<String, PackData> PACKS = new HashMap<>();

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
        this.resourceManager.registerReloadListener(new ArmorMaterialParser());
        this.resourceManager.registerReloadListener(new ToolTierParser());
        this.resourceManager.registerReloadListener(ITEM_PARSER = new ItemParser());
        this.resourceManager.registerReloadListener(new SuitSetParser());

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

    public PackData getPackData(String id) {
        return PACKS.get(id);
    }

    @ExpectPlatform
    public static RepositorySource getWrappedPackFinder(RepositorySource folderPackFinder) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static PackType getPackType() {
        throw new AssertionError();
    }

    public CompletableFuture<AddonPackManager> beginLoading(Executor backgroundExecutor, Executor gameExecutor) {
        this.packList.reload();
        // Enable all packs
        this.packList.setSelected(this.packList.getAvailableIds());

        this.packList.getAvailablePacks().forEach(pack -> {
            try {
                InputStream stream = pack.open().getRootResource("pack.mcmeta");
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                JsonObject jsonobject = GsonHelper.parse(bufferedreader);
                PackData packData = PackData.fromJSON(jsonobject);
                if (PACKS.containsKey(packData.getId())) {
                    throw new RuntimeException("Duplicate addonpack: " + packData.getId());
                }
                PACKS.put(packData.getId(), packData);
                bufferedreader.close();
                stream.close();
            } catch (IOException | VersionParsingException e) {
                e.printStackTrace();
            }
        });

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
