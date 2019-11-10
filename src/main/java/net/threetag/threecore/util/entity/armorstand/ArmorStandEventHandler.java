package net.threetag.threecore.util.entity.armorstand;

import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.item.HammerItem;
import net.threetag.threecore.util.player.PlayerHelper;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class ArmorStandEventHandler {

    @SubscribeEvent
    public static void rightClickEntity(PlayerInteractEvent.EntityInteractSpecific e) {
        if (e.getTarget() instanceof ArmorStandEntity && e.getItemStack().getItem() instanceof HammerItem) {
            ArmorStandEntity armorStand = (ArmorStandEntity) e.getTarget();
            armorStand.setShowArms(!armorStand.getShowArms());
            PlayerHelper.playSoundToAll(armorStand.world, armorStand.posX, armorStand.posY + armorStand.size.height / 2F, armorStand.posZ, 50, !armorStand.getShowArms() ? SoundEvents.BLOCK_STONE_BREAK : SoundEvents.BLOCK_STONE_PLACE, armorStand.getSoundCategory());
            e.setCanceled(true);
            e.setCancellationResult(ActionResultType.SUCCESS);
        }
    }

    @SubscribeEvent
    public static void leftClickEntity(AttackEntityEvent e) {
        if (e.getTarget() instanceof ArmorStandEntity) {
            ItemStack stack = e.getPlayer().getHeldItemMainhand();

            if (!stack.isEmpty() && stack.getItem() instanceof HammerItem) {
                ArmorStandEntity armorStand = (ArmorStandEntity) e.getTarget();
                armorStand.setNoBasePlate(!armorStand.hasNoBasePlate());
                PlayerHelper.playSoundToAll(armorStand.world, armorStand.posX, armorStand.posY + armorStand.size.height / 2F, armorStand.posZ, 50, armorStand.hasNoBasePlate() ? SoundEvents.BLOCK_STONE_BREAK : SoundEvents.BLOCK_STONE_PLACE, armorStand.getSoundCategory());
                e.setCanceled(true);
            }
        }
    }

}
