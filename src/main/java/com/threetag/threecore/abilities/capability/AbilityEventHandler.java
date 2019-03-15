package com.threetag.threecore.abilities.capability;

import com.threetag.threecore.ThreeCore;
import com.threetag.threecore.abilities.Ability;
import com.threetag.threecore.abilities.AbilityHealing;
import com.threetag.threecore.abilities.AbilityHelper;
import com.threetag.threecore.abilities.network.MessageSendPlayerAbilityContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;

public class AbilityEventHandler {

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> e) {
        if (canUseAbilities(e.getObject()) && !e.getObject().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).isPresent()) {
            e.addCapability(new ResourceLocation(ThreeCore.MODID, "ability_container"), new AbilityContainerProvider(new CapabilityAbilityContainer()));
        }
    }

    public static boolean canUseAbilities(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof EntityPlayerMP) {
            e.getEntity().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
                if (a instanceof INBTSerializable)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSendPlayerAbilityContainer(e.getEntity().getEntityId(), (NBTTagCompound) ((INBTSerializable) a).serializeNBT()), ((EntityPlayerMP) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
            if (a instanceof INBTSerializable)
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSendPlayerAbilityContainer(e.getEntity().getEntityId(), (NBTTagCompound) ((INBTSerializable) a).serializeNBT()), ((EntityPlayerMP) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
    }

    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent e) {
        if(e.getEntityLiving() instanceof EntityPlayerMP)
        e.getEntity().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
            if (a instanceof INBTSerializable)
                ThreeCore.NETWORK_CHANNEL.sendTo(new MessageSendPlayerAbilityContainer(e.getEntity().getEntityId(), (NBTTagCompound) ((INBTSerializable) a).serializeNBT()), ((EntityPlayerMP) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
        AbilityHelper.getAbilityContainerList().forEach((f) -> {
            IAbilityContainer container = f.apply(e.getEntityLiving());
            if (container != null)
                container.tick(e.getEntityLiving());
        });
    }

}
