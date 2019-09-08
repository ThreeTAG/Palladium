package net.threetag.threecore.abilities.capability;

import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.AbilityHelper;
import net.threetag.threecore.abilities.FlightAbility;
import net.threetag.threecore.abilities.HealingAbility;
import net.threetag.threecore.abilities.IAbilityContainer;
import net.threetag.threecore.abilities.condition.ToggleCondition;
import net.threetag.threecore.abilities.network.SendPlayerAbilityContainerMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
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
        return entity instanceof LivingEntity;
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.RightClickItem e) {
        if (!e.getEntity().world.isRemote && e.getItemStack().getItem() == Items.STICK) {
            System.out.println("mal schauen");
            e.getEntityPlayer().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(a -> {
                a.clearAbilities(e.getEntityLiving());
                System.out.println("awd");
                a.addAbility(e.getEntityLiving(), "healing", new HealingAbility());
                FlightAbility flight = new FlightAbility();
                flight.getConditionManager().addCondition(new ToggleCondition(flight));
                a.addAbility(e.getEntityLiving(), "flight", flight);
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
        if (e.getEntity() instanceof ServerPlayerEntity) {
            e.getEntity().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
                if (a instanceof CapabilityAbilityContainer)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new SendPlayerAbilityContainerMessage(e.getEntity().getEntityId(), (CompoundNBT) ((CapabilityAbilityContainer) a).getUpdateTag()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }

    @SubscribeEvent
    public void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
            if (a instanceof CapabilityAbilityContainer)
                ThreeCore.NETWORK_CHANNEL.sendTo(new SendPlayerAbilityContainerMessage(e.getTarget().getEntityId(), (CompoundNBT) ((CapabilityAbilityContainer) a).getUpdateTag()), ((ServerPlayerEntity) e.getEntityPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
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
