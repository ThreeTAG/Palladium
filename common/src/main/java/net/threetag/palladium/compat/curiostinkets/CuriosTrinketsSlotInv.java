package net.threetag.palladium.compat.curiostinkets;

import net.minecraft.world.item.ItemStack;

public interface CuriosTrinketsSlotInv {

    int getSlots();

    ItemStack getStackInSlot(int index);

    void setStackInSlot(int index, ItemStack stack);

    CuriosTrinketsSlotInv EMPTY = new CuriosTrinketsSlotInv() {
        @Override
        public int getSlots() {
            return 0;
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return ItemStack.EMPTY;
        }

        @Override
        public void setStackInSlot(int index, ItemStack stack) {

        }
    };

}
