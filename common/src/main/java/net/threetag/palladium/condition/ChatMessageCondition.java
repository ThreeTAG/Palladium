package net.threetag.palladium.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

import java.util.Locale;

public abstract class ChatMessageCondition extends Condition {

    public static final PalladiumProperty<String> CHAT_MESSAGE = new StringProperty("chat_message").configurable("The chat message to look for, will check case insensitive.");

    public final String chatMessage;
    public final int cooldown;

    public ChatMessageCondition(String chatMessage, int cooldown) {
        this.chatMessage = chatMessage;
        this.cooldown = cooldown;
        PowerManager.CHECK_FOR_CHAT_MESSAGES.add(chatMessage.trim().toLowerCase(Locale.ROOT));
    }

    public abstract void onChat(LivingEntity entity, AbilityEntry entry);

    @Override
    public boolean handlesCooldown() {
        return this.cooldown > 0;
    }

}
