package net.threetag.palladium.addonpack;

import com.google.gson.JsonObject;
import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.Util;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Unit;
import net.threetag.palladium.addonpack.parser.*;
import net.threetag.palladium.addonpack.version.VersionParsingException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
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

    public PackRepository getPackList() {
        return packList;
    }

    @ExpectPlatform
    public static RepositorySource getWrappedPackFinder(RepositorySource folderPackFinder) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static PackType getPackType() {
        throw new AssertionError();
    }

    @SuppressWarnings("ConstantConditions")
    public void beginLoading(Executor backgroundExecutor, Executor gameExecutor) {
        this.packList.reload();
        // Enable all packs
        this.packList.setSelected(this.packList.getAvailableIds());

        // Read pack.mcmetas
        PACKS.clear();
        this.packList.getAvailablePacks().forEach(pack -> {
            try {
                InputStream stream = pack.open().getRootResource("pack.mcmeta");
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                JsonObject jsonobject = GsonHelper.parse(bufferedreader);
                PackData packData = PackData.fromJSON(jsonobject);
                if (PACKS.containsKey(packData.id())) {
                    throw new RuntimeException("Duplicate addonpack: " + packData.id());
                }
                PACKS.put(packData.id(), packData);
                bufferedreader.close();
                stream.close();
            } catch (IOException | VersionParsingException e) {
                e.printStackTrace();
            }
        });

        // Check dependencies
        Map<PackData, List<PackData.Dependency>> dependencyConflicts = new HashMap<>();
        for (PackData pack : PACKS.values()) {
            for (PackData.Dependency dependency : pack.getDependenciesFor(ArchitecturyTarget.getCurrentTarget())) {
                if (!dependency.isValid()) {
                    dependencyConflicts.computeIfAbsent(pack, p -> new ArrayList<>()).add(dependency);
                }
            }
        }

        if(!dependencyConflicts.isEmpty()) {
            List<String> test = new ArrayList<>();
            for (Map.Entry<PackData, List<PackData.Dependency>> entry : dependencyConflicts.entrySet()) {
                for (PackData.Dependency dependency : entry.getValue()) {
                    test.add("Pack " + entry.getKey().id() + " requires " + dependency.getId() + " " + Arrays.toString(dependency.getVersionRequirements().toArray()));
                }
            }

            if(Platform.getEnvironment() == Env.SERVER) {
                throw new RuntimeException(Arrays.toString(test.toArray()));
            } else {
                ClientGuiEvent.SET_SCREEN.register(screen -> {
                    if(screen instanceof TitleScreen) {
                        return CompoundEventResult.interruptTrue(null);
                    }

                    return CompoundEventResult.pass();
                });
            }

        } else {
            this.resourceManager
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

}
