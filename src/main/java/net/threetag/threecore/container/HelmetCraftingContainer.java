package net.threetag.threecore.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.IWorldPosCallable;
import net.threetag.threecore.item.recipe.AbstractConstructionTableRecipe;
import net.threetag.threecore.item.recipe.HelmetCraftingRecipe;
import net.threetag.threecore.item.recipe.TCBaseRecipeSerializers;

public class HelmetCraftingContainer extends AbstractConstructionTableContainer<HelmetCraftingRecipe> {

    public HelmetCraftingContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, IWorldPosCallable.DUMMY);
    }

    public HelmetCraftingContainer(int id, PlayerInventory playerInventory, IWorldPosCallable worldPosCallable) {
        super(TCBaseContainerTypes.HELMET_CRAFTING, id, playerInventory, worldPosCallable);

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                this.addSlot(new Slot(this.craftingInventory, x + y * 4, 28 + x * 18, 22 + y * 18));
            }
        }

        this.addSlot(new Slot(this.craftingInventory, 16, 137, 46));

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
        return (AbstractConstructionTableRecipe.Serializer) TCBaseRecipeSerializers.HELMET_CRAFTING;
    }

    @Override
    public IRecipeType<HelmetCraftingRecipe> getRecipeType() {
        return HelmetCraftingRecipe.RECIPE_TYPE;
    }
}
