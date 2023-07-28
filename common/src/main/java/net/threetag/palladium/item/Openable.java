package net.threetag.palladium.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public interface Openable {

    String OPEN_TAG = "Palladium:Opened";
    String OPENING_TAG = "Palladium:Opening";

    default boolean canBeOpened(LivingEntity entity, ItemStack stack, @Nullable EquipmentSlot slot) {
        return true;
    }

    /**
     * Determines how long the opening process takes
     *
     * @param stack The relevant item stack
     * @return Return 0 for instant opening, anything greater represents the amount of ticks
     */
    default int getOpeningTime(ItemStack stack) {
        return 0;
    }

    default void setOpen(LivingEntity entity, ItemStack stack, @Nullable EquipmentSlot slot, boolean open) {
        if (this.isOpen(stack) != open) {
            if (!open || canBeOpened(entity, stack, slot)) {
                var nbt = stack.getOrCreateTag();
                nbt.putBoolean(OPEN_TAG, open);
                this.onOpeningStateChange(entity, stack, slot, open);

                if (getOpeningTime(stack) <= 0) {
                    if (open) {
                        this.onFullyOpened(entity, stack, slot);
                    } else {
                        this.onFullyClosed(entity, stack, slot);
                    }
                }
            }
        }
    }

    default boolean isOpen(ItemStack stack) {
        return stack.hasTag() && Objects.requireNonNull(stack.getTag()).getBoolean(OPEN_TAG);
    }

    default int getOpeningProgress(ItemStack stack) {
        return stack.hasTag() ? Objects.requireNonNull(stack.getTag()).getInt(OPENING_TAG) : 0;
    }

    default void onOpeningStateChange(LivingEntity entity, ItemStack stack, @Nullable EquipmentSlot slot, boolean open) {

    }

    default void onFullyOpened(LivingEntity entity, ItemStack stack, @Nullable EquipmentSlot slot) {

    }

    default void onFullyClosed(LivingEntity entity, ItemStack stack, @Nullable EquipmentSlot slot) {

    }

    static void onTick(LivingEntity entity, ItemStack stack, @Nullable EquipmentSlot slot) {
        if (stack.getItem() instanceof Openable openable) {
            var nbt = stack.getOrCreateTag();
            var max = openable.getOpeningTime(stack);

            if (max <= 0) {
                // nothing
            } else {
                var timer = nbt.getInt(OPENING_TAG);
                var isOpen = openable.isOpen(stack);

                if (isOpen && !openable.canBeOpened(entity, stack, slot)) {
                    isOpen = false;
                    nbt.putBoolean(OPEN_TAG, isOpen);
                    openable.onOpeningStateChange(entity, stack, slot, isOpen);
                }

                if (isOpen && timer < max) {
                    timer += 1;
                    nbt.putInt(OPENING_TAG, timer);

                    if (timer == max) {
                        openable.onFullyOpened(entity, stack, slot);
                    }
                } else if (!isOpen && timer > 0) {
                    timer -= 1;
                    nbt.putInt(OPENING_TAG, timer);

                    if (timer == 0) {
                        openable.onFullyClosed(entity, stack, slot);
                    }
                }
            }
        }
    }

}
