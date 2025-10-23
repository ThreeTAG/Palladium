package net.threetag.palladium.addonpack;

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
import net.minecraft.server.packs.metadata.MetadataSectionType;
import net.minecraft.server.packs.metadata.pack.PackFormat;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.BlockTypes;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.block.BlockPropertiesCodec;
import net.threetag.palladium.item.CreativeModeTabCodec;
import net.threetag.palladium.item.ItemTypes;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class AddonPackManager {

    public static final PackFormat PACK_FORMAT = new PackFormat(2, 0);
    public static final MetadataSectionType<PackMetadataSection> ADDON_TYPE = new MetadataSectionType<>("pack", Objects.requireNonNull(PackMetadataSection.codecForPackType(AddonPackManager.getPackType())));

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
        this.packRepository = new PackRepository(getWrappedPackFinder(getPackType()));
        ResourcePackLoader.populatePackRepository(this.packRepository, AddonPackManager.getPackType(), false);

        // Replace block properties codec because MC's one is not done yet
        BlockPropertiesCodec.replaceBlockPropertiesCodec();
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    static void onRegister(RegisterEvent e) {
        if (e.getRegistryKey() == Registries.ATTRIBUTE) {
            AddonPackManager.initiateBasicLoaders();
        }

        AddonPackManager.initiateFor(e.getRegistryKey(), (registry, id, object) -> e.register(registry, id, () -> object));
    }

    @SubscribeEvent
    static void packFinder(AddPackFindersEvent e) {
        if (e.getPackType() != AddonPackManager.getPackType()) {
            e.addRepositorySource(AddonPackManager.getWrappedPackFinder(e.getPackType()));
        }
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
        Path folder = FMLPaths.GAMEDIR.get().resolve(path);
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
        var folderPackFinder = new FolderRepositorySource(getLocation(), packType, PACK_SOURCE, LevelStorageSource.parseValidator(FMLPaths.GAMEDIR.get().resolve("allowed_symlinks.txt")));
        var legacyFolderPackFinder = new FolderRepositorySource(getLegacyLocation(), packType, PACK_SOURCE, LevelStorageSource.parseValidator(FMLPaths.GAMEDIR.get().resolve("allowed_symlinks.txt")));

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
        for (PreparableReloadListener listener : parser) {
            this.resourceManager.registerReloadListener(listener);
        }
        this.packRepository.reload();
        // Enable all packs
        this.packRepository.setSelected(this.packRepository.getAvailableIds());

        this.mainThreadExecutor = new QueueableExecutor();

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
