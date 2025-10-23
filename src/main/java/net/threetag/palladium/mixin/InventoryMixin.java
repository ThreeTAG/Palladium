package net.threetag.palladium.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.ability.RestrictSlotsAbility;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public abstract class InventoryMixin {

    @Shadow
    public int selected;

    @Shadow
    @Final
    public Player player;

    @Shadow
    @Final
    public NonNullList<ItemStack> items;

    @Shadow
    protected abstract boolean hasRemainingSpaceForItem(ItemStack destination, ItemStack origin);

    @Shadow
    public abstract ItemStack getItem(int slot);

    @Inject(at = @At("RETURN"), method = "getFreeSlot", cancellable = true)
    public void getFreeSlot(CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() == this.selected && RestrictSlotsAbility.isRestricted(this.player, EquipmentSlot.MAINHAND)) {
            for (int i = 0; i < this.items.size(); ++i) {
                if (this.items.get(i).isEmpty() && i != this.selected) {
                    cir.setReturnValue(i);
                    return;
                }
            }
            cir.setReturnValue(-1);
        }
    }

    @Inject(at = @At("RETURN"), method = "getSlotWithRemainingSpace", cancellable = true)
    public void getSlotWithRemainingSpace(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (cir.getReturnValue() == this.selected && RestrictSlotsAbility.isRestricted(this.player, EquipmentSlot.MAINHAND)) {
            if (this.hasRemainingSpaceForItem(this.getItem(40), stack)) {
                cir.setReturnValue(40);
            } else {
                for (int i = 0; i < this.items.size(); ++i) {
                    if (i != this.selected && this.hasRemainingSpaceForItem(this.items.get(i), stack)) {
                        cir.setReturnValue(i);
                    }
                }

                cir.setReturnValue(-1);
            }
        }
    }

}
