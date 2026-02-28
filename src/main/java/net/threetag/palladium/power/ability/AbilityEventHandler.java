package net.threetag.palladium.power.ability;

import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.attachment.PalladiumAttachments;
import net.threetag.palladium.component.PalladiumDataComponents;
import net.threetag.palladium.util.PlayerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@EventBusSubscriber(modid = Palladium.MOD_ID)
public class AbilityEventHandler {

    public static final List<String> CHECK_FOR_CHAT_MESSAGES = new ArrayList<>();

    @SubscribeEvent
    static void nameFormat(PlayerEvent.NameFormat e) {
        AbilityUtil.getEnabledInstances(e.getEntity(), AbilitySerializers.NAME_CHANGE.get())
                .stream()
                .filter(ab -> ab.get(PalladiumDataComponents.Abilities.NAME_CHANGE_CACHED.get()) != null)
                .findFirst()
                .ifPresent(ability -> {
                    e.setDisplayname(Objects.requireNonNull(ability.get(PalladiumDataComponents.Abilities.NAME_CHANGE_CACHED.get())));
                });
    }

    @SubscribeEvent
    static void onLivingVisibility(LivingEvent.LivingVisibilityEvent e) {
        e.modifyVisibility(OpacityChanging.getMobVisibilityMultiplier(e.getEntity(), 1F));
    }

    @SubscribeEvent
    static void chatReceived(ServerChatEvent e) {
        var msg = e.getMessage().getString().trim();

        if (CHECK_FOR_CHAT_MESSAGES.contains(msg.toLowerCase(Locale.ROOT))) {
            for (AbilityInstance<?> entry : AbilityUtil.getInstances(e.getPlayer())) {
                // TODO
//                for (Condition condition : entry.getAbility().getStateManager().getUnlockingHandler()) {
//                    if (condition instanceof ChatMessageCondition chat && chat.chatMessage.trim().equalsIgnoreCase(msg)) {
//                        chat.onChat(player, entry);
//                    }
//                }
//                for (Condition condition : entry.getAbility().getStateManager().getActivationHandler()) {
//                    if (condition instanceof ChatMessageCondition chat && chat.chatMessage.trim().equalsIgnoreCase(msg)) {
//                        chat.onChat(player, entry);
//                    }
//                }
            }
        }
    }

    @SubscribeEvent
    static void onHurt(LivingIncomingDamageEvent e) {
        // Fire Aspect Ability
        if (e.getSource().getEntity() instanceof LivingEntity sourceEntity && AbilityUtil.isTypeEnabled(sourceEntity, AbilitySerializers.FIRE_ASPECT.get())) {
            boolean hasAddedExistingFire = false;
            int fireSeconds = 0;
            for (AbilityInstance<FireAspectAbility> instance : AbilityUtil.getEnabledInstances(sourceEntity, AbilitySerializers.FIRE_ASPECT.get())) {
                int time = Math.max(instance.getAbility().time, 0);
                if (!hasAddedExistingFire && instance.getAbility().shouldStackTime) {
                    fireSeconds = Math.min(fireSeconds + (e.getEntity().getRemainingFireTicks() / 20), instance.getAbility().maxTime);
                    hasAddedExistingFire = true;
                }
                fireSeconds += time;
            }
            e.getEntity().setRemainingFireTicks(fireSeconds);
        }

        // Intangibility Ability
        if (e.getSource().is(DamageTypes.IN_WALL) && AbilityUtil.isTypeEnabled(e.getEntity(), AbilitySerializers.INTANGIBILITY.value())) {
            e.setAmount(0);
            e.setCanceled(true);
        }

        // Damage Immunity Ability
        for (AbilityInstance<DamageImmunityAbility> instance : AbilityUtil.getEnabledInstances(e.getEntity(), AbilitySerializers.DAMAGE_IMMUNITY.get())) {
            if (DamageImmunityAbility.isImmuneAgainst(instance, e.getSource())) {
                e.setAmount(0);
                e.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    static void onTick(EntityTickEvent.Pre e) {
        if (e.getEntity() instanceof Player player && !player.level().isClientSide()) {
            var flying = PlayerUtil.isFlying(player);
            player.setData(PalladiumAttachments.IS_CLIMBING.get(),
                    !flying
                            && (!player.onGround() || player.isCrouching())
                            && AbilityUtil.isTypeEnabled(player, AbilitySerializers.WALL_CLIMBING.get())
                            && player.level().findSupportingBlock(player, player.getBoundingBox().inflate(0.2F, 0F, 0.2F)).isPresent()
            );
        }
    }

}
