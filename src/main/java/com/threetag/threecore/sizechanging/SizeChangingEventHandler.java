package com.threetag.threecore.sizechanging;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import com.threetag.threecore.sizechanging.capability.SizeChangingProvider;
import com.threetag.threecore.sizechanging.network.SyncSizeMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class SizeChangingEventHandler {

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (!e.getObject().getCapability(CapabilitySizeChanging.SIZE_CHANGING).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "size_changing"), new SizeChangingProvider(new CapabilitySizeChanging()));
        }
    }

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent e) {
        e.getEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent(sizeChanging -> sizeChanging.sync(e.getEntity()));
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

}
