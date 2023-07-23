package net.threetag.palladium.addonpack;

import com.google.gson.JsonObject;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.targets.ArchitecturyTarget;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
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
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLogEntry;
import net.threetag.palladium.addonpack.parser.*;
import net.threetag.palladium.addonpack.version.VersionParsingException;
import net.threetag.palladium.client.screen.AddonPackLogScreen;
import net.threetag.palladiumcore.event.EventResult;
import net.threetag.palladiumcore.event.ScreenEvents;
import net.threetag.palladiumcore.util.Platform;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AddonPackManager {

    private static AddonPackManager INSTANCE;
    public static boolean IGNORE_INJECT = false;
    public static ItemParser ITEM_PARSER;
    private static CompletableFuture<AddonPackManager> loaderFuture;

    public static AddonPackManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddonPackManager();
        }
        return INSTANCE;
    }

    public static void startLoading() {
        Palladium.LOGGER.info("Starting addonpack initialisation...");
        loaderFuture = getInstance().beginLoading(Util.backgroundExecutor());
    }

    public static void waitForLoading() {
        getInstance().waitForLoading(loaderFuture);
        loaderFuture = null;
    }

    private final Map<String, PackData> packs = new HashMap<>();
    private final ReloadableResourceManager resourceManager;
    private final RepositorySource folderPackFinder;
    private final PackRepository packList;
    private QueueableExecutor mainThreadExecutor;

    private AddonPackManager() {
        IGNORE_INJECT = true;
        this.resourceManager = new ReloadableResourceManager(getPackType());
        this.folderPackFinder = new FolderRepositorySource(getLocation(), PackSource.DEFAULT);
        var modSource = getModRepositorySource();
        RepositorySource[] sources = modSource == null ? new RepositorySource[]{this.folderPackFinder} : new RepositorySource[]{this.folderPackFinder, modSource};
        this.packList = new PackRepository(getPackType(), sources);
        IGNORE_INJECT = false;

        this.resourceManager.registerReloadListener(new CreativeModeTabParser());
        this.resourceManager.registerReloadListener(new ArmorMaterialParser());
        this.resourceManager.registerReloadListener(new ToolTierParser());
        this.resourceManager.registerReloadListener(new BlockParser());
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

    @ExpectPlatform
    public static RepositorySource getModRepositorySource() {
        throw new AssertionError();
    }

    @SuppressWarnings("ConstantConditions")
    public CompletableFuture<AddonPackManager> beginLoading(Executor backgroundExecutor) {
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

                if (packData == null && !(Platform.isForge() ? pack.getId().startsWith("mod:") : pack.getId().equalsIgnoreCase("Fabric Mods"))) {
                    bufferedreader.close();
                    stream.close();
                    throw new RuntimeException("Addonpack " + pack.getId() + " is missing key details in pack.mcmeta (\"id\" & \"version\" in \"packs\"-section)");
                }

                if (packData == null) {
                    bufferedreader.close();
                    stream.close();
                    return;
                }

                if (packs.containsKey(packData.getId())) {
                    bufferedreader.close();
                    stream.close();
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
            mainThreadExecutor = new QueueableExecutor();

            return this.resourceManager
                    .createReload(backgroundExecutor, mainThreadExecutor, CompletableFuture.completedFuture(Unit.INSTANCE), this.packList.openAllSelected())
                    .done().whenComplete((unit, throwable) -> {
                        if (throwable != null) {
                            this.resourceManager.close();
                            throwable.printStackTrace();
                        }
                    })
                    .thenRun(mainThreadExecutor::finish)
                    .thenApply((unit) -> this);
        }
        return null;
    }

    private void finish() {
        Palladium.LOGGER.info("Finished addonpack initialisation!");
    }

    public void waitForLoading(CompletableFuture<AddonPackManager> loaderFuture) {
        try {
            while (!loaderFuture.isDone()) {
                mainThreadExecutor.runQueue();
                mainThreadExecutor.waitForTasks();
            }

            mainThreadExecutor.runQueue();

            loaderFuture.get().finish();
        } catch (InterruptedException e) {
            Palladium.LOGGER.error("Addonpack loader future interrupted!");
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw new ReportedException(CrashReport.forThrowable(cause, "Error loading addonpacks"));
        }
    }

    public static class QueueableExecutor implements Executor {

        private final Thread thread = Thread.currentThread();
        private final ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
        private final Semaphore sem = new Semaphore(1);

        public boolean isSameThread() {
            return Thread.currentThread() == thread;
        }

        @Override
        public void execute(@NotNull Runnable command) {
            if (!this.isSameThread()) {
                queue.add(command);
                sem.release();
            } else {
                command.run();
            }
        }

        public void runQueue() {
            if (!isSameThread()) {
                throw new IllegalStateException("This method must be called in the main thread.");
            }

            while (queue.size() > 0) {
                var run = queue.poll();
                if (run != null) run.run();
            }
        }

        public void finish() {
            sem.release();
        }

        public void waitForTasks() throws InterruptedException {
            sem.acquire();
        }
    }

}
