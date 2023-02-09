package net.threetag.palladium.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ExtendedArmor {

    default boolean hideSecondPlayerLayer(Player player, ItemStack stack, EquipmentSlot slot) {
        return false;
    }

    default ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return null;
    }

}
