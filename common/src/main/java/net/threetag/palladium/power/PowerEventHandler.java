package net.threetag.palladium.power;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.ChatEvent;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilitySerializers;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.FireAspectAbility;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PowerEventHandler implements ChatEvent.Received, EntityEvent.LivingHurt {

    public static final List<String> CHECK_FOR_CHAT_MESSAGES = new ArrayList<>();

    public static void init() {
        var instance = new PowerEventHandler();
        ChatEvent.RECEIVED.register(instance);
        EntityEvent.LIVING_HURT.register(instance);
    }

    @Override
    public EventResult received(@Nullable ServerPlayer player, Component component) {
        var msg = component.getString().trim();

        if (CHECK_FOR_CHAT_MESSAGES.contains(msg.toLowerCase(Locale.ROOT))) {
            for (AbilityInstance<?> entry : AbilityUtil.getInstances(player)) {
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
        return EventResult.pass();
    }

    @Override
    public EventResult hurt(LivingEntity livingEntity, DamageSource damageSource, float amount) {
        if (damageSource.getEntity() instanceof LivingEntity sourceEntity && AbilityUtil.isTypeEnabled(sourceEntity, AbilitySerializers.FIRE_ASPECT.get())) {
            boolean hasAddedExistingFire = false;
            int fireSeconds = 0;
            for (AbilityInstance<FireAspectAbility> instance : AbilityUtil.getEnabledInstances(sourceEntity, AbilitySerializers.FIRE_ASPECT.get())) {
                int time = Math.max(instance.getAbility().time, 0);
                if (!hasAddedExistingFire && instance.getAbility().shouldStackTime) {
                    fireSeconds = Math.min(fireSeconds + (livingEntity.getRemainingFireTicks() / 20), instance.getAbility().maxTime);
                    hasAddedExistingFire = true;
                }
                fireSeconds += time;
            }
            livingEntity.setRemainingFireTicks(fireSeconds);
        }

        return EventResult.pass();
    }
}
