package net.threetag.threecore.sizechanging;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.threetag.threecore.sizechanging.capability.SizeChangingProvider;
import net.threetag.threecore.sizechanging.network.SyncSizeMessage;

public class SizeChangingEventHandler {

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (!e.getObject().getCapability(CapabilitySizeChanging.SIZE_CHANGING).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "size_changing"), new SizeChangingProvider(new CapabilitySizeChanging(e.getObject())));
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent e) {
        e.getEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            if (e.getEntity() instanceof ServerPlayerEntity && sizeChanging instanceof INBTSerializable)
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncSizeMessage(e.getEntity().getEntityId(), (CompoundNBT) ((INBTSerializable) sizeChanging).serializeNBT()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });

        Entity thrower = null;
        if (e.getEntity() instanceof ThrowableEntity)
            thrower = ((ThrowableEntity) e.getEntity()).getThrower();
        else if (e.getEntity() instanceof AbstractArrowEntity)
            thrower = ((AbstractArrowEntity) e.getEntity()).getShooter();

        if (thrower != null) {
            copyScale(thrower, e.getEntity());
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
            if (sizeChanging instanceof INBTSerializable && e.getPlayer() instanceof ServerPlayerEntity) {
                ThreeCore.NETWORK_CHANNEL.sendTo(new SyncSizeMessage(e.getTarget().getEntityId(), (CompoundNBT) ((INBTSerializable) sizeChanging).serializeNBT()), ((ServerPlayerEntity) e.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            }
        });
    }

    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing e) {
        if (e.getEntity() instanceof LivingEntity) {
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(SizeManager.SIZE_WIDTH);
            ((LivingEntity) e.getEntity()).getAttributes().registerAttribute(SizeManager.SIZE_HEIGHT);
        }
    }

    @SubscribeEvent
    public void onItemToss(ItemTossEvent e) {
        copyScale(e.getPlayer(), e.getEntityItem());
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent e) {
        e.getDrops().forEach(entity -> {
            copyScale(e.getEntityLiving(), entity);
        });
    }

    @SubscribeEvent
    public void visibility(PlayerEvent.Visibility e) {
        e.getPlayer().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> e.modifyVisibility(sizeChanging.getScale()));
    }

    public static void copyScale(Entity source, Entity entity) {
        source.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging1 -> {
            entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> {
                sizeChanging.setSizeDirectly(sizeChanging1.getSizeChangeType(), sizeChanging1.getScale());
            });
        });
    }

}
