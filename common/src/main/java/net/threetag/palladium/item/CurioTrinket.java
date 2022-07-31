package net.threetag.palladium.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public interface CurioTrinket {

    default void tick(LivingEntity entity, ItemStack stack) {
    }

    default void onEquip(ItemStack stack, LivingEntity entity) {
    }

    default void onUnequip(ItemStack stack, LivingEntity entity) {
    }

    default boolean canEquip(ItemStack stack, LivingEntity entity) {
        return true;
    }

    default boolean canUnequip(ItemStack stack, LivingEntity entity) {
        return !EnchantmentHelper.hasBindingCurse(stack);
    }

}
