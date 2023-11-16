package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.PropertyManager;

public class ChatToggleCondition extends ChatMessageCondition {

    public ChatToggleCondition(String chatMessage, int cooldown) {
        super(chatMessage, cooldown);
    }

    @Override
    public void init(LivingEntity entity, AbilityEntry entry, PropertyManager manager) {
        entry.startCooldown(entity, this.cooldown);
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        var entry = context.get(DataContextType.ABILITY);

        if (entity == null || entry == null) {
            return false;
        }

        if (this.cooldown != 0 && entry.cooldown == 0) {
            entry.keyPressed = false;
        }
        return entry.keyPressed;
    }

    @Override
    public void onChat(LivingEntity entity, AbilityEntry entry) {
        entry.keyPressed = !entry.keyPressed;
    }

    @Override
    public CooldownType getCooldownType() {
        return CooldownType.DYNAMIC;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.CHAT_TOGGLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public Serializer() {
            this.withProperty(CHAT_MESSAGE, "Hello World");
            this.withProperty(HeldCondition.Serializer.COOLDOWN, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ChatToggleCondition(this.getProperty(json, CHAT_MESSAGE), this.getProperty(json, HeldCondition.Serializer.COOLDOWN));
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Toggles the ability on and off after a chat message was sent.";
        }
    }
}
