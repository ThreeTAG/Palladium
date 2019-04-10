package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.AbilityFlight;
import com.threetag.threecore.abilities.AbilityHealing;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.IAbilityContainer;
import com.threetag.threecore.abilities.network.MessageSendPlayerAbilityContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class AbilityEventHandler {

    public static boolean canUseAbilities(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent e) {
        if (e.getItemStack().getItem() == Items.STICK) {
            e.getEntityPlayer().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(a -> {
                a.clearAbilities(e.getEntityLiving());
                a.addAbility(e.getEntityLiving(), "healing", new AbilityHealing());
                a.addAbility(e.getEntityLiving(), "flight", new AbilityFlight());
            });
        }
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (canUseAbilities(e.getObject()) && !e.getObject().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "ability_container"), new AbilityContainerProvider(new CapabilityAbilityContainer()));
        }
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone e) {
        e.getOriginal().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
            if (a instanceof INBTSerializable) {
                e.getEntityLiving().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a2) -> {
                    if (a2 instanceof INBTSerializable) {
                        ((INBTSerializable) a2).deserializeNBT(((INBTSerializable) a).serializeNBT());
                    }
                });
            }
        });
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityPlayerMP) {
            e.getEntity().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
                if (a instanceof CapabilityAbilityContainer)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSendPlayerAbilityContainer(e.getEntity().getEntityId(), (NBTTagCompound) ((CapabilityAbilityContainer) a).getUpdateTag()), ((EntityPlayerMP) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
            if (a instanceof CapabilityAbilityContainer)
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSendPlayerAbilityContainer(e.getTarget().getEntityId(), (NBTTagCompound) ((CapabilityAbilityContainer) a).getUpdateTag()), ((EntityPlayerMP) e.getEntityPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
    }

    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent e) {
        AbilityHelper.getAbilityContainerList().forEach((f) -> {
            IAbilityContainer container = f.apply(e.getEntityLiving());
            if (container != null)
                container.tick(e.getEntityLiving());
        });
    }

}
