package net.threetag.palladium.compat.accessories;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.List;

public class AccessoriesCompat {

    public static AccessoriesCompat INSTANCE = new AccessoriesCompat();

    public List<Identifier> getSlots(Level level) {
        return Collections.emptyList();
    }

    public List<ItemStack> getFromSlot(LivingEntity entity, Identifier slot) {
        return Collections.emptyList();
    }

    public ItemStack getFromSlot(LivingEntity entity, Identifier slot, int index) {
        return ItemStack.EMPTY;
    }

    public void setInSlot(LivingEntity entity, Identifier slot, int index, ItemStack stack) {

    }

    public int getSlotSize(LivingEntity entity, Identifier slot) {
        return 0;
    }

    public void clearSlot(LivingEntity entity, Identifier slot) {

    }

}
