package net.threetag.palladium.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class SlotMixin {

    @Shadow
    @Final
    public Container container;

    @Shadow
    public int index;

    @Shadow
    public abstract int getContainerSlot();

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    public void mayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (this.container instanceof Inventory
                && this.getContainerSlot() >= 39 - 3 && this.getContainerSlot() <= 39
                && AbilityUtil.isTypeEnabled(player, Abilities.LOCK_ARMOR.get())) {
            cir.setReturnValue(false);
        }
    }

}
