package net.threetag.palladium.addonpack;

import dev.architectury.platform.Platform;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.BlockTypes;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.block.BlockPropertiesCodec;
import net.threetag.palladium.item.CreativeModeTabCodec;
import net.threetag.palladium.item.ItemTypes;
import net.threetag.palladium.platform.PlatformHelper;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;

public class AddonPackManager {

    public static final int PACK_FORMAT = 2;

    private static AddonPackManager INSTANCE;
    public static PackType PACK_TYPE;
    public static final PackSource PACK_SOURCE = PackSource.create(PackSource.decorateWithSource("pack.source.addonpack"), true);
    public static final String FOLDER = "palladium/addonpacks";
    public static final String LEGACY_FOLDER = "addonpacks";
    private static final List<PreparableReloadListener> BASIC_LOADERS = new ArrayList<>();
    private static final List<LoaderEntry<?>> REGISTRY_LOADERS = new ArrayList<>();

    private final ReloadableResourceManager resourceManager;
    private final PackRepository packRepository;
    private static CompletableFuture<AddonPackManager> loaderFuture;
    private QueueableExecutor mainThreadExecutor;

    public static AddonPackManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AddonPackManager();
        }
        return INSTANCE;
    }

    static {
        registerLoader(DataAttachmentLoader.INSTANCE);
        registerLoader(new ToolMaterialLoader());
        registerLoader(new ArmorMaterialLoader());
        registerRegistryLoader(Registries.CREATIVE_MODE_TAB, callback -> new AddonObjectLoader<>(CreativeModeTabCodec.CODEC, Registries.CREATIVE_MODE_TAB, callback));
        registerRegistryLoader(Registries.BLOCK, callback -> new AddonObjectLoader<>(BlockTypes.CODEC.codec(), Registries.BLOCK, callback));
        registerRegistryLoader(Registries.ITEM, callback -> new AddonObjectLoader<>(ItemTypes.CODEC, Registries.ITEM, callback));
    }

    private AddonPackManager() {
        this.resourceManager = new ReloadableResourceManager(getPackType());
        RepositorySource[] sources = new RepositorySource[]{getWrappedPackFinder(getPackType())};
        this.packRepository = new PackRepository(sources);
        PlatformHelper.PLATFORM.getAddonPackManager().afterPackRepositoryCreation(this.packRepository);

        // Replace block properties codec because MC's one is not done yet
        BlockPropertiesCodec.replaceBlockPropertiesCodec();

        this.resourceManager.registerReloadListener(PlatformHelper.PLATFORM.getAddonPackManager().getRecipeManager());
    }

    public static <T> void registerLoader(PreparableReloadListener loader) {
        BASIC_LOADERS.add(loader);
    }

    public static <T> void registerRegistryLoader(ResourceKey<Registry<T>> registry, Function<RegisterCallback<T>, AddonObjectLoader<T>> registerCallback) {
        REGISTRY_LOADERS.add(new LoaderEntry<>(registry, registerCallback));
    }

    public static void initiateBasicLoaders() {
        startLoading(BASIC_LOADERS);
        waitForLoading();
    }

    public static <T> void initiateFor(ResourceKey<?> registry, RegisterCallback<T> callback) {
        for (LoaderEntry<?> loader : REGISTRY_LOADERS) {
            if (loader.registry.equals(registry)) {
                startLoading(Collections.singletonList(loader.get(callback)));
                waitForLoading();
            }
        }
    }

    public static void initiateAllLoaders(RegisterCallback<?> callback) {
        for (LoaderEntry<?> loader : REGISTRY_LOADERS) {
            startLoading(Collections.singletonList(loader.get(callback)));
            waitForLoading();
        }
    }

    private static void startLoading(List<PreparableReloadListener> parser) {
        loaderFuture = getInstance().beginLoading(Util.backgroundExecutor(), parser);
    }

    private static void waitForLoading() {
        getInstance().waitForLoading(loaderFuture);
        loaderFuture = null;
    }

    public static Path getLocation(String path) {
        Path folder = Platform.getGameFolder().resolve(path);
        var file = folder.toFile();
        if (!file.exists() && !file.mkdirs())
            throw new RuntimeException("Could not create addonpacks directory! Please create the directory yourself, or make sure the name is not taken by a file and you have permission to create directories.");
        return folder;
    }

    public static Path getLocation() {
        return getLocation(FOLDER);
    }

    public static Path getLegacyLocation() {
        return getLocation(LEGACY_FOLDER);
    }

    public static PackType getPackType() {
        // just to make sure the mixin is loaded
        var client = PackType.CLIENT_RESOURCES;
        return PACK_TYPE;
    }

    public static RepositorySource getWrappedPackFinder(PackType packType) {
        var folderPackFinder = new FolderRepositorySource(getLocation(), packType, PACK_SOURCE, LevelStorageSource.parseValidator(Platform.getGameFolder().resolve("allowed_symlinks.txt")));
        var legacyFolderPackFinder = new FolderRepositorySource(getLegacyLocation(), packType, PACK_SOURCE, LevelStorageSource.parseValidator(Platform.getGameFolder().resolve("allowed_symlinks.txt")));

        return (infoConsumer) -> {
            folderPackFinder.loadPacks(pack -> {
                pack.location = new PackLocationInfo("addonpack:" + pack.getId(), pack.location.title(), pack.location.source(), pack.location.knownPackInfo());
                pack.selectionConfig = new PackSelectionConfig(true, pack.selectionConfig.defaultPosition(), pack.selectionConfig.fixedPosition());
                infoConsumer.accept(pack);
            });

            legacyFolderPackFinder.loadPacks(pack -> {
                pack.location = new PackLocationInfo("addonpack:" + pack.getId(), pack.location.title(), pack.location.source(), pack.location.knownPackInfo());
                pack.selectionConfig = new PackSelectionConfig(true, pack.selectionConfig.defaultPosition(), pack.selectionConfig.fixedPosition());
                infoConsumer.accept(pack);
            });
        };
    }

    public CompletableFuture<AddonPackManager> beginLoading(Executor backgroundExecutor, List<PreparableReloadListener> parser) {
        this.resourceManager.listeners.clear();
        this.resourceManager.registerReloadListener(PlatformHelper.PLATFORM.getAddonPackManager().getRecipeManager());
        for (PreparableReloadListener listener : parser) {
            this.resourceManager.registerReloadListener(listener);
        }
        this.packRepository.reload();
        // Enable all packs
        this.packRepository.setSelected(this.packRepository.getAvailableIds());

//        // Read pack.mcmetas
//        packs.clear();
//        this.packList.getAvailablePacks().forEach(pack -> {
//            try {
//                InputStream stream = pack.open().getRootResource("pack.mcmeta").get();
//                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
//                JsonObject jsonobject = GsonHelper.parse(bufferedreader);
//                PackData packData = PackData.fromJSON(jsonobject);
//
//                if (packData == null) {
//                    bufferedreader.close();
//                    stream.close();
//                    Palladium.LOGGER.info("Skipping " + pack.getId() + " as it's not been marked as an addonpack");
//                }
//
//                if (packs.containsKey(packData.getId())) {
//                    bufferedreader.close();
//                    stream.close();
//                    throw new RuntimeException("Duplicate addonpack: " + packData.getId());
//                }
//
//                packs.put(packData.getId(), packData);
//                bufferedreader.close();
//                stream.close();
//            } catch (Exception e) {
//                AddonPackLog.error(e.getLocalizedMessage());
//            }
//        });
//
//        // Check dependencies
//        Map<PackData, List<PackData.Dependency>> dependencyConflicts = new HashMap<>();
//        for (PackData pack : packs.values()) {
//            for (PackData.Dependency dependency : pack.getDependenciesFor(ArchitecturyTarget.getCurrentTarget())) {
//                if (!dependency.isValid()) {
//                    dependencyConflicts.computeIfAbsent(pack, p -> new ArrayList<>()).add(dependency);
//                }
//            }
//        }
//
//        if (!dependencyConflicts.isEmpty()) {
//            List<String> test = new ArrayList<>();
//            for (Map.Entry<PackData, List<PackData.Dependency>> entry : dependencyConflicts.entrySet()) {
//                for (PackData.Dependency dependency : entry.getValue()) {
//                    test.add("Pack " + entry.getKey().getId() + " requires " + dependency.getId() + " " + Arrays.toString(dependency.getVersionRequirements().toArray()));
//                }
//            }
//
//            if (Platform.isServer()) {
//                throw new RuntimeException(Arrays.toString(test.toArray()));
//            } else {
//                ScreenEvents.OPENING.register((currentScreen, newScreen) -> {
//                    if (newScreen.get() instanceof TitleScreen) {
//                        newScreen.set(new AddonPackLogScreen(dependencyConflicts.keySet().stream().map(packData -> {
//                            StringBuilder s = new StringBuilder("Addon Pack '" + packData.getId() + "' requires ");
//                            for (PackData.Dependency dependency : dependencyConflicts.get(packData)) {
//                                s.append(dependency.getId()).append(" ").append(Arrays.toString(dependency.getVersionRequirements().toArray())).append("; ");
//                            }
//                            s = new StringBuilder(s.substring(0, s.length() - 2));
//                            return new AddonPackLogEntry(AddonPackLogEntry.Type.ERROR, s.toString());
//                        }).collect(Collectors.toList()), null));
//                    }
//
//                    return EventResult.pass();
//                });
//            }
//        }

        mainThreadExecutor = new QueueableExecutor();

        return this.resourceManager
                .createReload(backgroundExecutor, mainThreadExecutor, CompletableFuture.completedFuture(Unit.INSTANCE), this.packRepository.openAllSelected())
                .done().whenComplete((unit, throwable) -> {
                    if (throwable != null) {
                        this.resourceManager.close();
                        AddonPackLog.error(throwable.getMessage());
                    }
                })
                .thenRun(mainThreadExecutor::finish)
                .thenRun(() -> {

                })
                .thenApply((unit) -> this);
    }

    private void finish() {
        // finish
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

    @FunctionalInterface
    public interface RegisterCallback<T> {

        void register(ResourceKey<Registry<T>> registry, ResourceLocation id, T object);

    }

    private record LoaderEntry<T>(ResourceKey<Registry<T>> registry,
                                  Function<RegisterCallback<T>, AddonObjectLoader<T>> callback) {

        @SuppressWarnings("unchecked")
        public AddonObjectLoader<T> get(RegisterCallback<?> callback) {
            return this.callback.apply((RegisterCallback<T>) callback);
        }

    }

}
