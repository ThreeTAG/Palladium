package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;
import org.jetbrains.annotations.Nullable;

public class CommandResultCondition extends Condition implements CommandSource {

    private final String command, comparison;
    private final int compare_to;
    private final boolean log;

    public CommandResultCondition(String command, String comparison, int compare_to, boolean log) {
        this.command = command;
        this.comparison = comparison;
        this.compare_to = compare_to;
        this.log = log;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        if (entity.level instanceof ServerLevel serverLevel) {
            var stack = new CommandSourceStack(this, entity.position(), entity.getRotationVector(),
                    serverLevel, 2, entity.getName().getString(), entity.getDisplayName(), entity.level.getServer(),
                    entity)
                    .withSuppressedOutput();

            if (!this.log) {
                stack = stack.withSuppressedOutput();
            }

            int result = serverLevel.getServer().getCommands().performPrefixedCommand(stack, command);

            return switch (comparison) {
                case ">=" -> (result >= compare_to);
                case "<=" -> (result <= compare_to);
                case ">" -> (result > compare_to);
                case "<" -> (result < compare_to);
                case "!=" -> (result != compare_to);
                case "==" -> (result == compare_to);
                default -> false;
            };
        } else {
            return false;
        }
    }

    @Override
    public ConditionContextType getContextType() {
        return ConditionContextType.ABILITIES;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.COMMAND_RESULT.get();
    }

    @Override
    public void sendSystemMessage(Component component) {
        if (this.log) {
            AddonPackLog.info("Command Result Condition Log: " + component.getString());
        }
    }

    @Override
    public boolean acceptsSuccess() {
        return this.log;
    }

    @Override
    public boolean acceptsFailure() {
        return this.log;
    }

    @Override
    public boolean shouldInformAdmins() {
        return this.log;
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> COMMAND = new StringProperty("command").configurable("The command output to compare the 'compare_to' int with");
        public static final PalladiumProperty<String> COMPARISON = new StringProperty("comparison").configurable("The comparison used between the 'command' and 'compare_to' fields, accepts >=, <=, >, <, !=, or == (note that the first number is from 'command' and the second from 'compare_to')");
        public static final PalladiumProperty<Integer> COMPARE_TO = new IntegerProperty("compare_to").configurable("The number being compared to the output of 'command'");
        public static final PalladiumProperty<Boolean> LOG = new BooleanProperty("log").configurable("If the command's output is sent to the entity or not (unless debugging, you probably want this false/unset)");

        public Serializer() {
            this.withProperty(COMMAND, "execute if entity @s");
            this.withProperty(COMPARISON, "==");
            this.withProperty(COMPARE_TO, 1);
            this.withProperty(LOG, false);
        }

        @Override
        public Condition make(JsonObject json) {
            return new CommandResultCondition(this.getProperty(json, COMMAND), this.getProperty(json, COMPARISON), this.getProperty(json, COMPARE_TO), this.getProperty(json, LOG));
        }

        @Override
        public String getDescription() {
            return "Executes a command and compares the output to a number";
        }
    }
}
