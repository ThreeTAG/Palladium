package net.threetag.threecore.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IWorldPosCallable;
import net.threetag.threecore.item.recipe.AbstractConstructionTableRecipe;
import net.threetag.threecore.item.recipe.ChestplateCraftingRecipe;
import net.threetag.threecore.item.recipe.TCRecipeSerializers;

public class ChestplateCraftingContainer extends AbstractConstructionTableContainer<ChestplateCraftingRecipe> {

    public ChestplateCraftingContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public ChestplateCraftingContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(TCContainerTypes.CHESTPLATE_CRAFTING.get(), id, playerInventory, worldPosCallable);

        this.addSlot(new Slot(this.craftingInventory, 0, 19, 28));
        this.addSlot(new Slot(this.craftingInventory, 1, 37, 28));
        this.addSlot(new Slot(this.craftingInventory, 2, 55, 28));
        this.addSlot(new Slot(this.craftingInventory, 3, 73, 28));
        this.addSlot(new Slot(this.craftingInventory, 4, 91, 28));

        this.addSlot(new Slot(this.craftingInventory, 5, 13, 46));
        this.addSlot(new Slot(this.craftingInventory, 6, 37, 46));
        this.addSlot(new Slot(this.craftingInventory, 7, 55, 46));
        this.addSlot(new Slot(this.craftingInventory, 8, 73, 46));
        this.addSlot(new Slot(this.craftingInventory, 9, 97, 46));

        this.addSlot(new Slot(this.craftingInventory, 10, 13, 64));
        this.addSlot(new Slot(this.craftingInventory, 11, 37, 64));
        this.addSlot(new Slot(this.craftingInventory, 12, 55, 64));
        this.addSlot(new Slot(this.craftingInventory, 13, 73, 64));
        this.addSlot(new Slot(this.craftingInventory, 14, 97, 64));

        this.addSlot(new Slot(this.craftingInventory, 15, 37, 82));
        this.addSlot(new Slot(this.craftingInventory, 16, 55, 82));
        this.addSlot(new Slot(this.craftingInventory, 17, 73, 82));

        this.addSlot(new Slot(this.craftingInventory, 18, 137, 46));

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
        return (AbstractConstructionTableRecipe.Serializer) TCRecipeSerializers.CHESTPLATE_CRAFTING.get();
    }

    @Override
    public IRecipeType<ChestplateCraftingRecipe> getRecipeType() {
        return ChestplateCraftingRecipe.RECIPE_TYPE;
    }
}
