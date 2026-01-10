package net.threetag.palladium.item.recipe;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

public record TailoringRecipeInput(Inventory playerInventory, ItemStack tool) implements RecipeInput {

    @Override
    public @NotNull ItemStack getItem(int index) {
        if (index <= this.playerInventory.getContainerSize() - 1) {
            return this.playerInventory.getItem(index);
        } else if (index == this.playerInventory.getContainerSize()) {
            return this.tool;
        }
        throw new IllegalArgumentException("Recipe does not contain slot " + index);
    }

    @Override
    public int size() {
        return this.playerInventory.getContainerSize() + 1;
    }
}
