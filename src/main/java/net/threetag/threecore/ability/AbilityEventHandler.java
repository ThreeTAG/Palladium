package net.threetag.threecore.ability;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.capability.CapabilityAbilityContainer;
import net.threetag.threecore.network.SendPlayerAbilityContainerMessage;

@Mod.EventBusSubscriber(modid = ThreeCore.MODID)
public class AbilityEventHandler {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone e) {
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
    public static void onJoin(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof ServerPlayerEntity) {
            e.getEntity().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
                if (a instanceof CapabilityAbilityContainer)
                    ThreeCore.NETWORK_CHANNEL.sendTo(new SendPlayerAbilityContainerMessage(e.getEntity().getEntityId(), ((CapabilityAbilityContainer) a).getUpdateTag()), ((ServerPlayerEntity) e.getEntity()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
            });
        }
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        e.getTarget().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
            if (a instanceof CapabilityAbilityContainer)
                ThreeCore.NETWORK_CHANNEL.sendTo(new SendPlayerAbilityContainerMessage(e.getTarget().getEntityId(), ((CapabilityAbilityContainer) a).getUpdateTag()), ((ServerPlayerEntity) e.getPlayer()).connection.getNetworkManager(), NetworkDirection.PLAY_TO_CLIENT);
        });
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        AbilityHelper.getAbilityContainerList().forEach((f) -> {
            IAbilityContainer container = f.apply(e.getEntityLiving());
            if (container != null)
                container.tick(e.getEntityLiving());
        });
    }

    @SubscribeEvent
    public static void onChangeEquipment(LivingEquipmentChangeEvent e) {
        // Make sure to call lastTick when player unequips item with abilities on it. Otherwise players could e.g. keep attribute modifiers
        if (e.getEntityLiving() != null) {
            e.getFrom().getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent(a -> a.getAbilityMap().forEach((s, ability) -> ability.lastTick(e.getEntityLiving())));
        }
    }

    @SubscribeEvent
    public static void onPlayerVisibility(PlayerEvent.Visibility e) {
        for (InvisibilityAbility invisibilityAbility : AbilityHelper.getAbilitiesFromClass(e.getPlayer(), InvisibilityAbility.class)) {
            if (invisibilityAbility.getConditionManager().isEnabled()) {
                e.modifyVisibility(0D);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent e) {
        for (DamageImmunityAbility ability : AbilityHelper.getAbilitiesFromClass(e.getEntityLiving(), DamageImmunityAbility.class)) {
            if (ability.getConditionManager().isEnabled() && ability.isImmuneAgainst(e.getSource())) {
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent e) {
        for (DamageImmunityAbility ability : AbilityHelper.getAbilitiesFromClass(e.getEntityLiving(), DamageImmunityAbility.class)) {
            if (ability.getConditionManager().isEnabled() && ability.isImmuneAgainst(e.getSource())) {
                e.setCanceled(true);
                e.setAmount(0);
            }
        }
    }

}
