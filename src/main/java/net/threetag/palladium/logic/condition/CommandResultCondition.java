package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.threetag.palladium.addonpack.log.AddonPackLog;
import net.threetag.palladium.util.ParsedCommands;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;

import java.util.Objects;

public record CommandResultCondition(ParsedCommands command, String comparison, int compareTo,
                                     boolean log) implements Condition, CommandSource {

    public static final MapCodec<CommandResultCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(
                    ParsedCommands.CODEC.optionalFieldOf("command", ParsedCommands.EMPTY).forGetter(CommandResultCondition::command),
                    Codec.STRING.fieldOf("comparison").forGetter(CommandResultCondition::comparison),
                    Codec.INT.fieldOf("compare_to").forGetter(CommandResultCondition::compareTo),
                    Codec.BOOL.optionalFieldOf("log", false).forGetter(CommandResultCondition::log)
            ).apply(instance, CommandResultCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, CommandResultCondition> STREAM_CODEC = StreamCodec.composite(
            ParsedCommands.STREAM_CODEC, CommandResultCondition::command,
            ByteBufCodecs.STRING_UTF8, CommandResultCondition::comparison,
            ByteBufCodecs.VAR_INT, CommandResultCondition::compareTo,
            ByteBufCodecs.BOOL, CommandResultCondition::log,
            CommandResultCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        if (entity.level() instanceof ServerLevel serverLevel) {
            var stack = new CommandSourceStack(this, entity.position(), entity.getRotationVector(),
                    serverLevel, PermissionSet.ALL_PERMISSIONS, entity.getName().getString(), Objects.requireNonNull(entity.getDisplayName()), serverLevel.getServer(),
                    entity)
                    .withSuppressedOutput();

            if (!this.log) {
                stack = stack.withSuppressedOutput();
            }

            serverLevel.getServer().getFunctions().execute(this.command.getCommandFunction(entity.level().getServer()), stack.withSuppressedOutput().withMaximumPermission(PermissionSet.ALL_PERMISSIONS));

            // TODO
//            return switch (comparison) {
//                case ">=" -> (result >= compareTo);
//                case "<=" -> (result <= compareTo);
//                case ">" -> (result > compareTo);
//                case "<" -> (result < compareTo);
//                case "!=" -> (result != compareTo);
//                case "==" -> (result == compareTo);
//                default -> false;
//            };
            return false;
        } else {
            return false;
        }
    }

    @Override
    public ConditionSerializer<CommandResultCondition> getSerializer() {
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

    public static class Serializer extends ConditionSerializer<CommandResultCondition> {

        @Override
        public MapCodec<CommandResultCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CommandResultCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public ConditionEnvironment getContextEnvironment() {
            return ConditionEnvironment.DATA;
        }

        @Override
        public String getDocumentationDescription() {
            return "Executes a command and compares the output to a number.";
        }
    }
}
