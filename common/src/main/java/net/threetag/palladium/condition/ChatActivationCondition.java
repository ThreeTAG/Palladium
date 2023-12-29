package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

import java.util.Objects;

public class ChatActivationCondition extends ChatMessageCondition {

    public final int ticks;

    public ChatActivationCondition(String chatMessage, int ticks, int cooldown) {
        super(chatMessage, cooldown);
        this.ticks = ticks;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var entry = context.getAbility();

        if (entity == null || entry == null) {
            return false;
        }

        if (this.cooldown != 0 && Objects.requireNonNull(entry).activationTimer == 1) {
            entry.startCooldown(context.getLivingEntity(), this.cooldown);
        }

        return Objects.requireNonNull(entry).activationTimer > 0;
    }

    @Override
    public void onChat(LivingEntity entity, AbilityEntry entry) {
        if (entry.cooldown <= 0 && entry.activationTimer == 0) {
            entry.startActivationTimer(entity, this.ticks);
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.CHAT_ACTIVATION.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> TICKS = new IntegerProperty("ticks").configurable("The amount of ticks the ability will be active for");

        public Serializer() {
            this.withProperty(CHAT_MESSAGE, "Hello World");
            this.withProperty(ChatActionCondition.Serializer.COOLDOWN, 0);
            this.withProperty(TICKS, 60);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ChatActivationCondition(this.getProperty(json, CHAT_MESSAGE), this.getProperty(json, TICKS), this.getProperty(json, ChatActionCondition.Serializer.COOLDOWN));
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "This condition is used to activate the ability when a chat message was sent for a certain amount of ticks.";
        }
    }
}
