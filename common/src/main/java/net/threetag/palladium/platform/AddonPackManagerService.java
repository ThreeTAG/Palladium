package net.threetag.palladium.platform;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.world.item.crafting.RecipeManager;

public interface AddonPackManagerService {

    default RepositorySource getAddonPackSource() {
        return null;
    }

    default void afterPackRepositoryCreation(PackRepository packRepository) {

    }

    RecipeManager getRecipeManager();

}
