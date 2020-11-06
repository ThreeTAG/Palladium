package net.threetag.threecore.ability;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.threetag.threecore.compat.curios.DefaultCuriosHandler;

public class DropArmorAbility extends Ability {

    public DropArmorAbility() {
        super(AbilityType.DROP_ARMOR);
    }

    @Override
    public void action(LivingEntity entity) {
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            if (slot.getSlotType() == EquipmentSlotType.Group.ARMOR) {
                ItemStack stack = entity.getItemStackFromSlot(slot);
                if (!stack.isEmpty()) {
                    entity.entityDropItem(stack);
                    entity.setItemStackToSlot(slot, ItemStack.EMPTY);
                }
            }
        }

        DefaultCuriosHandler.INSTANCE.dropEveryItem(entity);
    }

    @Override
    public boolean isEffect() {
        return true;
    }
}
