package net.threetag.palladium.power.ability;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.power.energybar.EnergyBarUsage;
import net.threetag.palladium.util.ParsedCommands;

import java.util.List;
import java.util.Objects;

public class CommandAbility extends Ability implements CommandSource {

    public static final MapCodec<CommandAbility> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ParsedCommands.CODEC.optionalFieldOf("commands", ParsedCommands.EMPTY).forGetter(ab -> ab.commands),
                    ParsedCommands.CODEC.optionalFieldOf("first_tick_commands", ParsedCommands.EMPTY).forGetter(ab -> ab.firstTick),
                    ParsedCommands.CODEC.optionalFieldOf("last_tick_commands", ParsedCommands.EMPTY).forGetter(ab -> ab.lastTick),
                    propertiesCodec(), stateCodec(), energyBarUsagesCodec()
            ).apply(instance, CommandAbility::new));

    public final ParsedCommands commands, firstTick, lastTick;

    public CommandAbility(ParsedCommands commands, ParsedCommands firstTick, ParsedCommands lastTick, AbilityProperties properties, AbilityStateManager conditions, List<EnergyBarUsage> energyBarUsages) {
        super(properties, conditions, energyBarUsages);
        this.commands = commands;
        this.firstTick = firstTick;
        this.lastTick = lastTick;
    }

    @Override
    public AbilitySerializer<CommandAbility> getSerializer() {
        return AbilitySerializers.COMMAND.get();
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityInstance<?> entry) {
        if (entity.level().getServer() != null && entity.level() instanceof ServerLevel serverLevel) {
            var source = this.createCommandSourceStack(entity, serverLevel);
            Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(this.firstTick.getCommandFunction(entity.level().getServer()), source.withSuppressedOutput().withMaximumPermission(2));
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityInstance<?> entry, boolean enabled) {
        if (enabled && entity.level().getServer() != null && entity.level() instanceof ServerLevel serverLevel) {
            var source = this.createCommandSourceStack(entity, serverLevel);
            Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(this.commands.getCommandFunction(entity.level().getServer()), source.withSuppressedOutput().withMaximumPermission(2));
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityInstance<?> entry) {
        if (entity.level().getServer() != null && entity.level() instanceof ServerLevel serverLevel) {
            var source = this.createCommandSourceStack(entity, serverLevel);
            Objects.requireNonNull(entity.level().getServer()).getFunctions().execute(this.lastTick.getCommandFunction(entity.level().getServer()), source.withSuppressedOutput().withMaximumPermission(2));
        }
    }

    public CommandSourceStack createCommandSourceStack(LivingEntity entity, ServerLevel serverLevel) {
        return new CommandSourceStack(this, entity.position(), entity.getRotationVector(),
                serverLevel, 2, entity.getName().getString(), entity.getDisplayName(), Objects.requireNonNull(entity.level().getServer()), entity)
                .withSuppressedOutput();
    }

    @Override
    public void sendSystemMessage(Component component) {

    }

    @Override
    public boolean acceptsSuccess() {
        return false;
    }

    @Override
    public boolean acceptsFailure() {
        return false;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }

    public static class Serializer extends AbilitySerializer<CommandAbility> {

        @Override
        public MapCodec<CommandAbility> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Ability, CommandAbility> builder, HolderLookup.Provider provider) {
            builder
                    .setDescription("An ability that executes commands.")
                    .setExampleObject(new CommandAbility(new ParsedCommands("say Tick"), new ParsedCommands("say First Tick"), new ParsedCommands("say Last Tick"), AbilityProperties.BASIC, AbilityStateManager.EMPTY, List.of()))
                    .addOptional("commands", TYPE_STRING_ARRAY, "The commands that are executed during each tick of the ability being active.")
                    .addOptional("first_tick_commands", TYPE_STRING_ARRAY, "The commands that are executed upon activating the ability.")
                    .addOptional("last_tick_commands", TYPE_STRING_ARRAY, "The commands that are executed on the last tick of the ability being active.");
        }
    }
}
