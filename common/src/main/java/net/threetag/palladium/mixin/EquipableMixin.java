package net.threetag.palladium.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.threetag.palladium.power.ability.Abilities;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equipable.class)
public interface EquipableMixin {

    @Inject(method = "swapWithEquipmentSlot", at = @At("HEAD"), cancellable = true)
    private static void swapWithEquipmentSlot(Item item, Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        ItemStack itemStack = player.getItemInHand(hand);
        EquipmentSlot equipmentSlot = Mob.getEquipmentSlotForItem(itemStack);

        if (equipmentSlot.isArmor() && AbilityUtil.isTypeEnabled(player, Abilities.LOCK_ARMOR.get())) {
            cir.setReturnValue(InteractionResultHolder.fail(itemStack));
        }
    }

}
