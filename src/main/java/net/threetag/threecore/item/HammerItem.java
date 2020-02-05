package net.threetag.threecore.item;

import com.google.common.collect.Sets;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;

import java.util.Random;

public class HammerItem extends ToolItem {

    public HammerItem(float attackDamageIn, float attackSpeedIn, IItemTier tier, Properties builder) {
        super(attackDamageIn, attackSpeedIn, tier, Sets.newHashSet(), builder);
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack stack = itemStack.copy();
        stack.attemptDamageItem(1, new Random(), null);
        return stack.getDamage() < stack.getMaxDamage() ? stack : ItemStack.EMPTY;
    }

}
