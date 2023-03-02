package net.threetag.palladium.compat.curiostinkets;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.threetag.palladium.util.PlayerSlot;

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

    default boolean canRightClickEquip() {
        return false;
    }

    default Multimap<Attribute, AttributeModifier> getModifiers(PlayerSlot slot, LivingEntity entity) {
        return ArrayListMultimap.create();
    }

}
