package net.threetag.threecore.util.entity.armorstand;

import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.base.entity.SuitStandEntity;
import net.threetag.threecore.base.item.HammerItem;
import net.threetag.threecore.util.player.PlayerHelper;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class ArmorStandEventHandler {

    @SubscribeEvent
    public static void rightClickEntity(PlayerInteractEvent.EntityInteractSpecific e) {
        if (e.getTarget() instanceof ArmorStandEntity) {
            ArmorStandEntity armorStand = (ArmorStandEntity) e.getTarget();

            if (!e.getEntityLiving().isSneaking()) {
                if (!e.getItemStack().isEmpty()) {
                    if (e.getItemStack().getItem() instanceof HammerItem) {
                        armorStand.setShowArms(!armorStand.getShowArms());
                        e.getItemStack().damageItem(1, e.getPlayer(), (player) -> player.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                        PlayerHelper.playSoundToAll(armorStand.world, armorStand.posX, armorStand.posY + armorStand.size.height / 2F, armorStand.posZ, 50, getSound(armorStand, armorStand.getShowArms()), armorStand.getSoundCategory());
                        e.setCanceled(true);
                        e.setCancellationResult(ActionResultType.SUCCESS);
                    } else if (e.getItemStack().getItem().isIn(armorStand instanceof SuitStandEntity ? Tags.Items.GEMS_QUARTZ : Tags.Items.RODS_WOODEN)) {
                        if (armorStand.isSmall()) {
                            e.getItemStack().shrink(1);
                            armorStand.setSmall(false);
                            PlayerHelper.playSoundToAll(armorStand.world, armorStand.posX, armorStand.posY + armorStand.size.height / 2F, armorStand.posZ, 50, getSound(armorStand, true), armorStand.getSoundCategory());
                            e.setCanceled(true);
                            e.setCancellationResult(ActionResultType.SUCCESS);
                        }
                    }
                }
            } else {
                for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
                    if (slotType.getSlotType() == EquipmentSlotType.Group.ARMOR) {
                        ItemStack standStack = armorStand.getItemStackFromSlot(slotType);
                        ItemStack playerStack = e.getEntityLiving().getItemStackFromSlot(slotType);
                        armorStand.setItemStackToSlot(slotType, playerStack);
                        e.getEntityLiving().setItemStackToSlot(slotType, standStack);
                    }
                }
                e.setCanceled(true);
                e.setCancellationResult(ActionResultType.SUCCESS);
            }
        }
    }

    @SubscribeEvent
    public static void leftClickEntity(AttackEntityEvent e) {
        if (e.getTarget() instanceof ArmorStandEntity && !e.getPlayer().isSneaking()) {
            ItemStack stack = e.getPlayer().getHeldItemMainhand();
            ArmorStandEntity armorStand = (ArmorStandEntity) e.getTarget();

            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof HammerItem) {
                    armorStand.setNoBasePlate(!armorStand.hasNoBasePlate());
                    stack.damageItem(1, e.getPlayer(), (player) -> player.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                    PlayerHelper.playSoundToAll(armorStand.world, armorStand.posX, armorStand.posY + armorStand.size.height / 2F, armorStand.posZ, 50, !armorStand.hasNoBasePlate() ? SoundEvents.BLOCK_STONE_PLACE : SoundEvents.BLOCK_STONE_BREAK, armorStand.getSoundCategory());
                    e.setCanceled(true);
                } else if (armorStand instanceof SuitStandEntity ? stack.getItem() instanceof PickaxeItem : stack.getItem() instanceof AxeItem) {
                    if (!armorStand.isSmall()) {
                        armorStand.setSmall(true);
                        stack.damageItem(1, e.getPlayer(), (player) -> player.sendBreakAnimation(EquipmentSlotType.MAINHAND));
                        PlayerHelper.playSoundToAll(armorStand.world, armorStand.posX, armorStand.posY + armorStand.size.height / 2F, armorStand.posZ, 50, getSound(armorStand, false), armorStand.getSoundCategory());
                        e.setCanceled(true);
                    }
                }
            }
        }
    }

    public static SoundEvent getSound(ArmorStandEntity entity, boolean place) {
        if (entity instanceof SuitStandEntity)
            return place ? SoundEvents.BLOCK_STONE_PLACE : SoundEvents.BLOCK_STONE_BREAK;
        else
            return place ? SoundEvents.ENTITY_ARMOR_STAND_PLACE : SoundEvents.ENTITY_ARMOR_STAND_BREAK;
    }

}
