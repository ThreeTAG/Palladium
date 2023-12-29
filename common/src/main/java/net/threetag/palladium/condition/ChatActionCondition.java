package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.Objects;

public class ChatActionCondition extends ChatMessageCondition {

    public ChatActionCondition(String chatMessage, int cooldown) {
        super(chatMessage, cooldown);
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        var entry = context.get(DataContextType.ABILITY);

        if (entity == null || entry == null) {
            return false;
        }

        if (Objects.requireNonNull(entry).keyPressed) {
            entry.keyPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public void onChat(LivingEntity entity, AbilityEntry entry) {
        if (entry.cooldown == 0) {
            entry.keyPressed = true;

            if (this.cooldown != 0) {
                entry.startCooldown(entity, this.cooldown);
            }
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.CHAT_ACTION.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> COOLDOWN = new IntegerProperty("cooldown").configurable("Amount of ticks the ability wont be useable for after using it.");

        public Serializer() {
            this.withProperty(CHAT_MESSAGE, "Hello World");
            this.withProperty(COOLDOWN, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ChatActionCondition(this.getProperty(json, CHAT_MESSAGE), this.getProperty(json, COOLDOWN));
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "This condition will be active once, when a chat message has been sent.";
        }
    }
}
