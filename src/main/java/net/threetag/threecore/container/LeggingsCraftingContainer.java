package net.threetag.threecore.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IWorldPosCallable;
import net.threetag.threecore.item.recipe.AbstractConstructionTableRecipe;
import net.threetag.threecore.item.recipe.LeggingsCraftingRecipe;
import net.threetag.threecore.item.recipe.TCRecipeSerializers;

public class LeggingsCraftingContainer extends AbstractConstructionTableContainer<LeggingsCraftingRecipe> {

    public LeggingsCraftingContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public LeggingsCraftingContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(TCContainerTypes.LEGGINGS_CRAFTING.get(), id, playerInventory, worldPosCallable);

        this.addSlot(new Slot(this.craftingInventory, 0, 37, 25));
        this.addSlot(new Slot(this.craftingInventory, 1, 55, 25));
        this.addSlot(new Slot(this.craftingInventory, 2, 73, 25));

        this.addSlot(new Slot(this.craftingInventory, 3, 37, 43));
        this.addSlot(new Slot(this.craftingInventory, 4, 73, 43));

        this.addSlot(new Slot(this.craftingInventory, 5, 37, 61));
        this.addSlot(new Slot(this.craftingInventory, 6, 73, 61));

        this.addSlot(new Slot(this.craftingInventory, 7, 37, 79));
        this.addSlot(new Slot(this.craftingInventory, 8, 73, 79));

        this.addSlot(new Slot(this.craftingInventory, 9, 137, 46));

        this.addSlot(new ConstructionTableResultSlot(playerInventory.player, this.getRecipeType(), this.craftingInventory, this.craftResultInventory, 0, 148, 99));

        for (int k = 0; k < 3; ++k) {
            for (int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(playerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 134 + k * 18));
            }
        }

        for (int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 192));
        }
    }

    @Override
    public AbstractConstructionTableRecipe.Serializer getRecipeSerializer() {
        return (AbstractConstructionTableRecipe.Serializer) TCRecipeSerializers.LEGGINGS_CRAFTING.get();
    }

    @Override
    public IRecipeType<LeggingsCraftingRecipe> getRecipeType() {
        return LeggingsCraftingRecipe.RECIPE_TYPE;
    }
}
