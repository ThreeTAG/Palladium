package net.threetag.palladium.fabric.platform;

import net.fabricmc.fabric.api.resource.ModResourcePack;
import net.fabricmc.fabric.impl.resource.loader.FabricRecipeManager;
import net.fabricmc.fabric.impl.resource.loader.ModNioResourcePack;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackFactory;
import net.fabricmc.fabric.impl.resource.loader.ModResourcePackUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.platform.AddonPackManagerService;

import java.util.function.Consumer;

@SuppressWarnings("UnstableApiUsage")
public class FabricAddonPackManager implements AddonPackManagerService {

    private static final PackSelectionConfig MOD_PACK_SELECTION_CONFIG = new PackSelectionConfig(false, Pack.Position.TOP, false);

    @Override
    public RepositorySource getAddonPackSource() {
        return new ModAddonSource(AddonPackManager.getPackType());
    }

    @Override
    public RecipeManager getRecipeManager() {
        return new RecipeManagerWrapper();
    }

    public record ModAddonSource(PackType type) implements RepositorySource {

        @Override
        public void loadPacks(Consumer<Pack> onLoad) {
            for (ModResourcePack pack : ModResourcePackUtil.getModResourcePacks(FabricLoader.getInstance(), this.type, null)) {
                ModNioResourcePack fabricPack = (ModNioResourcePack) pack;

                Pack resourcePackProfile = Pack.readMetaAndCreate(
                        fabricPack.location(),
                        new ModResourcePackFactory(pack),
                        type,
                        MOD_PACK_SELECTION_CONFIG
                );

                onLoad.accept(resourcePackProfile);
            }
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static class RecipeManagerWrapper extends RecipeManager implements FabricRecipeManager {

        public RecipeManagerWrapper() {
            super(null);
        }

        @Override
        protected RecipeMap prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
            return null;
        }

        @Override
        protected void apply(RecipeMap recipeMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {

        }

        @Override
        public HolderLookup.Provider fabric_getRegistries() {
            return null;
        }
    }
}
