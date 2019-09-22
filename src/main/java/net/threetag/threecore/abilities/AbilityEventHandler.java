package net.threetag.threecore.abilities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.abilities.capability.AbilityContainerProvider;
import net.threetag.threecore.abilities.capability.CapabilityAbilityContainer;
import net.threetag.threecore.abilities.network.SendPlayerAbilityContainerMessage;

public class AbilityEventHandler {

    public static boolean canUseAbilities(Entity entity) {
        return entity instanceof LivingEntity;
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

    @SubscribeEvent
    public void onChangeEquipment(LivingEquipmentChangeEvent e) {
        // Make sure to call lastTick when player unequips item with abilities on it. Otherwise players could e.g. keep attribute modifiers
        e.getFrom().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(a -> a.getAbilityMap().forEach((s, ability) -> ability.lastTick(e.getEntityLiving())));
    }

    @SubscribeEvent
    public void onRenderLivingPre(PlayerEvent.Visibility e) {
        for (InvisibilityAbility invisibilityAbility : AbilityHelper.getAbilitiesFromClass(e.getPlayer(), InvisibilityAbility.class)) {
            if (invisibilityAbility.getConditionManager().isEnabled()) {
                e.modifyVisibility(0D);
                return;
            }
        }
    }

}
