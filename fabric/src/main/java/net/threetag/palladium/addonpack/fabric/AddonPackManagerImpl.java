package net.threetag.palladium.addonpack.fabric;

import com.google.common.base.Charsets;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackCreator;
import net.fabricmc.fabric.impl.resource.loader.ResourceManagerHelperImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.FileUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.PalladiumMixinPlugin;
import net.threetag.palladium.addonpack.AddonPackManager;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddonPackManagerImpl {

    public static RepositorySource getModRepositorySource() {
        return new ModPackSource(Objects.requireNonNull(AddonPackManager.getPackType()));
    }

    @SuppressWarnings("UnstableApiUsage")
    public static class ModPackSource implements RepositorySource {

        private final PackType type;

        public ModPackSource(PackType type) {
            this.type = type;
        }

        @Override
        public void loadPacks(Consumer<Pack> onLoad) {
            for (ModContainer container : FabricLoader.getInstance().getAllMods()) {
                if (container.getMetadata().getType().equals("builtin")) {
                    continue;
                }

                net.fabricmc.fabric.api.resource.ModResourcePack pack = ModNioResourcePack.create(container.getMetadata().getId(), container, null, type, ResourcePackActivationType.ALWAYS_ENABLED);

                if (pack == null || Objects.equals(pack.packId(), "minecraft")) continue;

                Pack resourcePackProfile = Pack.readMetaAndCreate("mod:" + pack.packId(), Component.nullToEmpty(pack.getFabricModMetadata().getName()),
                        false, factory -> new ModResourcePack(container, pack.packId(), true, container.getRootPath()), type, Pack.Position.BOTTOM,
                        ModResourcePackCreator.RESOURCE_PACK_SOURCE);

                onLoad.accept(resourcePackProfile);
            }

            // Register all built-in resource packs provided by mods.
            if (!PalladiumMixinPlugin.HAS_QUILT) {
                ResourceManagerHelperImpl.registerBuiltinResourcePacks(this.type, onLoad);
            }
        }
    }

    public static class ModResourcePack extends AbstractPackResources {

        private final ModContainer modContainer;
        private final Path source;

        public ModResourcePack(ModContainer modContainer, String packId, boolean isBuiltin, final Path source) {
            super(packId, isBuiltin);
            this.modContainer = modContainer;
            this.source = source;
        }

        public Path getSource() {
            return this.source;
        }

        protected Path resolve(String... paths) {
            String fileName = String.join("/", paths);
            return this.modContainer.findPath(fileName).orElse(null);
        }

        @Nullable
        @Override
        public IoSupplier<InputStream> getRootResource(String... paths) {
            String fileName = String.join("/", paths);
            final Path path = resolve(paths);

            if ("pack.mcmeta".equals(fileName) && (path == null || !Files.exists(path))) {
                String id = this.modContainer.getMetadata().getId();
                String version = this.modContainer.getMetadata().getVersion().getFriendlyString();
                String description = this.modContainer.getMetadata().getDescription();
                String pack = String.format("{\"pack\":{\"id\": \"%s\", \"version\": \"%s\", \"description\":\"%s\"}}", id, version, description);
                return () -> IOUtils.toInputStream(pack, Charsets.UTF_8);
            } else {
                if (!Files.exists(path))
                    return null;

                return IoSupplier.create(path);
            }
        }

        @Override
        public void listResources(PackType type, String namespace, String path, ResourceOutput resourceOutput) {
            FileUtil.decomposePath(path).get()
                    .ifLeft(parts -> net.minecraft.server.packs.PathPackResources.listPath(namespace, resolve(type.getDirectory(), namespace).toAbsolutePath(), parts, resourceOutput))
                    .ifRight(dataResult -> Palladium.LOGGER.error("Invalid path {}: {}", path, dataResult.message()));
        }

        @Override
        public Set<String> getNamespaces(PackType type) {
            return getNamespacesFromDisk(type);
        }

        @NotNull
        private Set<String> getNamespacesFromDisk(final PackType type) {
            try {
                Path root = resolve(type.getDirectory());
                try (Stream<Path> walker = Files.walk(root, 1)) {
                    return walker
                            .filter(Files::isDirectory)
                            .map(root::relativize)
                            .filter(p -> p.getNameCount() > 0) // Skip the root entry
                            .map(p -> p.toString().replaceAll("/$", "")) // Remove the trailing slash, if present
                            .filter(s -> !s.isEmpty()) // Filter empty strings, otherwise empty strings default to minecraft namespace in ResourceLocations
                            .collect(Collectors.toSet());
                }
            } catch (IOException e) {
                if (type == PackType.SERVER_DATA) // We still have to add the resource namespace if client resources exist, as we load langs (which are in assets) on server
                {
                    return this.getNamespaces(PackType.CLIENT_RESOURCES);
                } else {
                    return Collections.emptySet();
                }
            }
        }

        @Override
        public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
            return this.getRootResource(getPathFromLocation(location.getPath().startsWith("lang/") ? PackType.CLIENT_RESOURCES : type, location));
        }

        private static String[] getPathFromLocation(PackType type, ResourceLocation location) {
            String[] parts = location.getPath().split("/");
            String[] result = new String[parts.length + 2];
            result[0] = type.getDirectory();
            result[1] = location.getNamespace();
            System.arraycopy(parts, 0, result, 2, parts.length);
            return result;
        }

        @Override
        public void close() {
        }

        @Override
        public String toString() {
            return String.format(Locale.ROOT, "%s: %s (%s)", getClass().getName(), this.packId(), getSource());
        }

    }

}
