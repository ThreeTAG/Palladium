package net.threetag.threecore.base.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IWorldPosCallable;
import net.threetag.threecore.base.recipe.AbstractConstructionTableRecipe;
import net.threetag.threecore.base.recipe.BootsCraftingRecipe;
import net.threetag.threecore.base.recipe.TCBaseRecipeSerializers;

public class BootsCraftingContainer extends AbstractConstructionTableContainer<BootsCraftingRecipe> {

    public BootsCraftingContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public BootsCraftingContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(TCBaseContainerTypes.BOOTS_CRAFTING, id, playerInventory, worldPosCallable);

        this.addSlot(new Slot(this.craftingInventory, 0, 36, 29));
        this.addSlot(new Slot(this.craftingInventory, 1, 74, 29));

        this.addSlot(new Slot(this.craftingInventory, 2, 36, 47));
        this.addSlot(new Slot(this.craftingInventory, 3, 74, 47));

        this.addSlot(new Slot(this.craftingInventory, 4, 18, 72));
        this.addSlot(new Slot(this.craftingInventory, 5, 36, 65));
        this.addSlot(new Slot(this.craftingInventory, 6, 74, 65));
        this.addSlot(new Slot(this.craftingInventory, 7, 92, 72));

        this.addSlot(new Slot(this.craftingInventory, 8, 137, 46));

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
        return (AbstractConstructionTableRecipe.Serializer) TCBaseRecipeSerializers.BOOTS_CRAFTING;
    }

    @Override
    public IRecipeType<BootsCraftingRecipe> getRecipeType() {
        return BootsCraftingRecipe.RECIPE_TYPE;
    }
}
