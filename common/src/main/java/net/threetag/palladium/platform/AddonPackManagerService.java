package net.threetag.palladium.platform;

import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.item.crafting.RecipeManager;

public interface AddonPackManagerService {

    void afterPackRepositoryCreation(PackRepository packRepository);

    RecipeManager getRecipeManager();

}
