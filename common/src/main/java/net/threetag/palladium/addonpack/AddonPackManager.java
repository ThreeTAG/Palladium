package net.threetag.palladium.addonpack;

import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.targets.ArchitecturyTarget;
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
import net.threetag.palladium.addonpack.log.AddonPackLogEntry;
import net.threetag.palladium.addonpack.parser.*;
import net.threetag.palladium.addonpack.version.VersionParsingException;
import net.threetag.palladium.client.screen.AddonPackLogScreen;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.ScreenEvents;
import net.threetag.palladiumcore.util.Platform;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

public class AddonPackManager {

    private static AddonPackManager INSTANCE;
    public static boolean IGNORE_INJECT = false;
    public static ItemParser ITEM_PARSER;
    private final Map<String, PackData> packs = new HashMap<>();

    public static AddonPackManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddonPackManager();
        }
        return INSTANCE;
    }

    public static void init() {
        getInstance().beginLoading(Util.backgroundExecutor(), Runnable::run);
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
        this.resourceManager.registerReloadListener(new ParticleTypeParser());
        this.resourceManager.registerReloadListener(new PoiTypeParser());
        this.resourceManager.registerReloadListener(new VillagerProfessionParser());
        this.resourceManager.registerReloadListener(new VillagerTradeParser());
    }

    public File getLocation() {
        File folder = Platform.getFolder().resolve("addonpacks").toFile();
        if (!folder.exists() && !folder.mkdirs())
            throw new RuntimeException("Could not create addonpacks directory! Please create the directory yourself, or make sure the name is not taken by a file and you have permission to create directories.");
        return folder;
    }

    public RepositorySource getWrappedPackFinder() {
        return getWrappedPackFinder(this.folderPackFinder);
    }

    public Collection<PackData> getPacks() {
        return packs.values();
    }

    public PackData getPackData(String id) {
        return packs.get(id);
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
        packs.clear();
        this.packList.getAvailablePacks().forEach(pack -> {
            try {
                InputStream stream = pack.open().getRootResource("pack.mcmeta");
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
                JsonObject jsonobject = GsonHelper.parse(bufferedreader);
                PackData packData = PackData.fromJSON(jsonobject);
                if (packs.containsKey(packData.getId())) {
                    throw new RuntimeException("Duplicate addonpack: " + packData.getId());
                }
                packs.put(packData.getId(), packData);
                bufferedreader.close();
                stream.close();
            } catch (IOException | VersionParsingException e) {
                e.printStackTrace();
            }
        });

        // Check dependencies
        Map<PackData, List<PackData.Dependency>> dependencyConflicts = new HashMap<>();
        for (PackData pack : packs.values()) {
            for (PackData.Dependency dependency : pack.getDependenciesFor(ArchitecturyTarget.getCurrentTarget())) {
                if (!dependency.isValid()) {
                    dependencyConflicts.computeIfAbsent(pack, p -> new ArrayList<>()).add(dependency);
                }
            }
        }

        if (!dependencyConflicts.isEmpty()) {
            List<String> test = new ArrayList<>();
            for (Map.Entry<PackData, List<PackData.Dependency>> entry : dependencyConflicts.entrySet()) {
                for (PackData.Dependency dependency : entry.getValue()) {
                    test.add("Pack " + entry.getKey().getId() + " requires " + dependency.getId() + " " + Arrays.toString(dependency.getVersionRequirements().toArray()));
                }
            }

            if (Platform.isServer()) {
                throw new RuntimeException(Arrays.toString(test.toArray()));
            } else {
                ScreenEvents.OPENING.register((currentScreen, newScreen) -> {
                    if (newScreen.get() instanceof TitleScreen) {
                        newScreen.set(new AddonPackLogScreen(dependencyConflicts.keySet().stream().map(packData -> {
                            StringBuilder s = new StringBuilder("Addon Pack '" + packData.getId() + "' requires ");
                            for (PackData.Dependency dependency : dependencyConflicts.get(packData)) {
                                s.append(dependency.getId()).append(" ").append(Arrays.toString(dependency.getVersionRequirements().toArray())).append("; ");
                            }
                            s = new StringBuilder(s.substring(0, s.length() - 2));
                            return new AddonPackLogEntry(AddonPackLogEntry.Type.ERROR, s.toString());
                        }).collect(Collectors.toList()), null));
                    }

                    return EventResult.pass();
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
