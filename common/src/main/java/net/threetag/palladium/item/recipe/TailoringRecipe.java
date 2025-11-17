package net.threetag.palladium.item.recipe;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.threetag.palladium.item.PalladiumItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public abstract class TailoringRecipe implements Recipe<Inventory> {

    protected final ResourceLocation id;
    protected final Map<EquipmentSlot, ItemStack> results;
    protected final List<SizedIngredient> ingredients;
    protected final Ingredient toolIngredient;
    protected final ResourceLocation toolIcon;
    protected final ResourceLocation categoryId;
    protected final boolean requiresUnlocking;

    public TailoringRecipe(ResourceLocation id, Map<EquipmentSlot, ItemStack> results, List<SizedIngredient> ingredients,
                           Ingredient toolIngredient, ResourceLocation toolIcon, ResourceLocation categoryId, boolean requiresUnlocking) {
        this.id = id;
        this.results = results;
        this.ingredients = ingredients;
        this.toolIngredient = toolIngredient;
        this.toolIcon = toolIcon;
        this.categoryId = categoryId;
        this.requiresUnlocking = requiresUnlocking;
    }

    @Override
    public boolean matches(Inventory container, Level level) {
        if (container.player instanceof ServerPlayer serverPlayer) {
            return !this.requiresUnlocking || serverPlayer.getRecipeBook().contains(this);
        }

        return false;
    }

    public abstract Component getTitle();

    public Map<EquipmentSlot, ItemStack> getResults() {
        return ImmutableMap.copyOf(this.results);
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        List<Ingredient> list = this.ingredients.stream().map(SizedIngredient::ingredient).filter(Objects::nonNull).toList();
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(list.size(), Ingredient.EMPTY);

        for (int i = 0; i < list.size(); i++) {
            nonNullList.set(i, list.get(i));
        }

        return nonNullList;
    }

    public List<SizedIngredient> getSizedIngredients() {
        return this.ingredients;
    }

    public Ingredient getToolIngredient() {
        return toolIngredient;
    }

    @Nullable
    public ResourceLocation getToolIcon() {
        return toolIcon;
    }

    @Override
    public ItemStack assemble(Inventory container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.results.values().stream().findFirst().orElse(ItemStack.EMPTY);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ItemStack getToastSymbol() {
        return PalladiumItems.TAILORING_BENCH.get().getDefaultInstance();
    }

    @Override
    public RecipeType<?> getType() {
        return PalladiumRecipeSerializers.TAILORING.get();
    }

    @Nullable
    public ResourceLocation getCategoryId() {
        return categoryId;
    }

    @Nullable
    public static Component getCategoryTitle(ResourceLocation categoryId) {
        return categoryId != null ? Component.translatable(Util.makeDescriptionId("palladium.tailoring_recipe.category", categoryId)) : null;
    }
}
