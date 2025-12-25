package net.threetag.palladium.mixin.forge;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;
import net.threetag.palladium.item.MultiversalExtrapolatorItem;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MultiversalExtrapolatorItem.class)
public abstract class MultiversalExtrapolatorItemMixin implements IForgeItem {

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return oldStack != newStack;
    }
}
