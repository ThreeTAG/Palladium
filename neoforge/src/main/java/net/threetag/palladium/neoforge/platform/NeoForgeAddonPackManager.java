package net.threetag.palladium.neoforge.platform;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.threetag.palladium.addonpack.AddonPackManager;
import net.threetag.palladium.platform.AddonPackManagerService;

public class NeoForgeAddonPackManager implements AddonPackManagerService {

    @Override
    public void afterPackRepositoryCreation(PackRepository packRepository) {
        ResourcePackLoader.populatePackRepository(packRepository, AddonPackManager.getPackType(), false);
    }

    @Override
    public RecipeManager getRecipeManager() {
        return new RecipeManagerWrapper();
    }

    public static class RecipeManagerWrapper extends RecipeManager {

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

    }
}
