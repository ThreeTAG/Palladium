package net.threetag.palladium.item.recipe;

import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TailoringRecipe extends Recipe<TailoringRecipeInput> {

    default boolean isAvailable(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            var key = serverPlayer.level().recipeAccess().getRecipes().stream().filter(h -> h.value() == this).findFirst().orElseThrow().id();

            return !this.requiresUnlocking() || serverPlayer.getRecipeBook().contains(key);
        }

        return true;
    }

    @Override
    default boolean matches(TailoringRecipeInput input, Level level) {
        if (input.playerInventory().player instanceof ServerPlayer serverPlayer) {
            var key = serverPlayer.level().recipeAccess().getRecipes().stream().filter(h -> h.value() == this).findFirst().orElseThrow().id();
            return !this.requiresUnlocking() || serverPlayer.getRecipeBook().contains(key);
        }

        return false;
    }

    @Override
    default ItemStack assemble(TailoringRecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    default @NotNull RecipeType<? extends Recipe<TailoringRecipeInput>> getType() {
        return PalladiumRecipeTypes.TAILORING.get();
    }

    @Override
    @NotNull
    RecipeSerializer<? extends Recipe<TailoringRecipeInput>> getSerializer();

    @Override
    default @NotNull RecipeBookCategory recipeBookCategory() {
        return PalladiumRecipeBookCategories.TAILORING.get();
    }

    Component title();

    Map<ArmorType, ItemStack> getResults();

    List<SizedIngredient> getIngredients();

    Ingredient toolIngredient();

    Optional<ResourceLocation> toolIcon();

    Optional<ResourceLocation> categoryId();

    boolean requiresUnlocking();

    @Nullable
    static Component getCategoryTitle(ResourceLocation categoryId) {
        return categoryId != null ? Component.translatable(Util.makeDescriptionId("palladium.tailoring_recipe.category", categoryId)) : null;
    }
}
