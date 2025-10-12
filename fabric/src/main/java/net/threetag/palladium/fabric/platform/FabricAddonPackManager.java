package net.threetag.palladium.fabric.platform;

import net.fabricmc.fabric.impl.resource.loader.FabricRecipeManager;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.threetag.palladium.platform.AddonPackManagerService;

public class FabricAddonPackManager implements AddonPackManagerService {

    @Override
    public void afterPackRepositoryCreation(PackRepository packRepository) {

    }

    @Override
    public RecipeManager getRecipeManager() {
        return new RecipeManagerWrapper();
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
