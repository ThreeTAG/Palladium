package net.threetag.palladium.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.power.ability.RestrictSlotsAbility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public class AbstractContainerMenuMixin {

    @Shadow
    @Final
    public NonNullList<Slot> slots;

    @Shadow
    @Final
    private NonNullList<ItemStack> lastSlots;

    @Shadow
    @Final
    private NonNullList<ItemStack> remoteSlots;

    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "addSlot", at = @At("HEAD"), cancellable = true)
    private void addSlot(Slot slot, CallbackInfoReturnable<Slot> cir) {
        AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;

        if (menu instanceof InventoryMenu && slot.getContainerSlot() >= 36 && slot.getContainerSlot() <= 39) {
            Slot wrapped = new Slot(slot.container, slot.getContainerSlot(), slot.x, slot.y) {
                @Override
                public void set(ItemStack stack) {
                    slot.set(stack);
                }

                @Override
                public int getMaxStackSize() {
                    return slot.getMaxStackSize();
                }

                @Override
                public boolean mayPlace(ItemStack stack) {
                    Inventory inv = (Inventory) slot.container;
                    var equipSlot = this.getContainerSlot() == 39 ? EquipmentSlot.HEAD : (this.getContainerSlot() == 38 ? EquipmentSlot.CHEST : (this.getContainerSlot() == 37 ? EquipmentSlot.LEGS : (EquipmentSlot.FEET)));
                    return slot.mayPlace(stack) && !RestrictSlotsAbility.isRestricted(inv.player, equipSlot);
                }

                @Override
                public boolean mayPickup(Player player) {
                    return slot.mayPickup(player);
                }

                @Nullable
                @Override
                public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                    return slot.getNoItemIcon();
                }
            };
            wrapped.index = this.slots.size();
            this.slots.add(wrapped);
            this.lastSlots.add(ItemStack.EMPTY);
            this.remoteSlots.add(ItemStack.EMPTY);
            cir.setReturnValue(wrapped);
        }

    }

}
