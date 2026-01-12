package net.threetag.palladium.item.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.threetag.palladium.item.PalladiumItems;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MultiversalExtrapolatorCloningRecipe extends CustomRecipe {

    public MultiversalExtrapolatorCloningRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        int designated = 0;
        int undesignated = 0;
        int circuits = 0;
        int total = 0;

        for (int j = 0; j < container.getContainerSize(); j++) {
            ItemStack stack = container.getItem(j);
            if (!stack.isEmpty()) {
                total++;

                if (stack.is(PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get())) {
                    if (!stack.getOrCreateTag().getString("Universe").isEmpty()) {
                        designated++;
                    } else {
                        undesignated++;
                    }
                } else if (stack.is(PalladiumItems.QUARTZ_CIRCUIT.get())) {
                    circuits++;
                }
            }
        }

        return designated == 1 && undesignated == 1 && circuits == 1 && total == 3;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        if (this.matches(container, null)) {
            for (int j = 0; j < container.getContainerSize(); j++) {
                ItemStack stack = container.getItem(j);
                if (!stack.isEmpty() && stack.is(PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get()) && !stack.getOrCreateTag().getString("Universe").isEmpty()) {
                    var result = PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get().getDefaultInstance();
                    result.getOrCreateTag().put("Universe", stack.getOrCreateTag().get("Universe"));
                    return result;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < remaining.size(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (itemStack.getItem().hasCraftingRemainingItem()) {
                remaining.set(i, new ItemStack(Objects.requireNonNull(itemStack.getItem().getCraftingRemainingItem())));
            } else if (itemStack.is(PalladiumItems.MULTIVERSAL_EXTRAPOLATOR.get()) && itemStack.getOrCreateTag().contains("Universe")) {
                remaining.set(i, itemStack.copyWithCount(1));
                break;
            }
        }

        return remaining;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return PalladiumRecipeSerializers.MULTIVERSAL_EXTRAPOLATOR_CLONING.get();
    }
}
