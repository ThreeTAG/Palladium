package net.threetag.palladium.power.ability;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.util.icon.ItemIcon;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringArrayProperty;

import java.util.Objects;

public class CommandAbility extends Ability implements CommandSource {

    public static final PalladiumProperty<String[]> FIRST_TICK_COMMANDS = new StringArrayProperty("first_tick_commands").configurable("Sets the commands which get executed when gaining/activating the ability");
    public static final PalladiumProperty<String[]> LAST_TICK_COMMANDS = new StringArrayProperty("last_tick_commands").configurable("Sets the commands which get executed when losing/deactivating the ability");
    public static final PalladiumProperty<String[]> COMMANDS = new StringArrayProperty("commands").configurable("Sets the commands which get executed when using the ability");

    public CommandAbility() {
        this.withProperty(ICON, new ItemIcon(Blocks.COMMAND_BLOCK));
        this.withProperty(FIRST_TICK_COMMANDS, new String[]{});
        this.withProperty(LAST_TICK_COMMANDS, new String[]{});
        this.withProperty(COMMANDS, new String[]{"say Hello World"});
    }

    @Override
    public void firstTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level.getServer() != null && entry.getProperty(FIRST_TICK_COMMANDS) != null && entity.level instanceof ServerLevel serverLevel) {
            for (String command : Objects.requireNonNull(entry.getProperty(FIRST_TICK_COMMANDS))) {
                entity.level.getServer().getCommands().performPrefixedCommand(this.createCommandSourceStack(entity, serverLevel), command);
            }
        }
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level.getServer() != null && entry.getProperty(COMMANDS) != null && entity.level instanceof ServerLevel serverLevel) {
            for (String command : Objects.requireNonNull(entry.getProperty(COMMANDS))) {
                entity.level.getServer().getCommands().performPrefixedCommand(this.createCommandSourceStack(entity, serverLevel), command);
            }
        }
    }

    @Override
    public void lastTick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level.getServer() != null && entry.getProperty(LAST_TICK_COMMANDS) != null && entity.level instanceof ServerLevel serverLevel) {
            for (String command : Objects.requireNonNull(entry.getProperty(LAST_TICK_COMMANDS))) {
                entity.level.getServer().getCommands().performPrefixedCommand(this.createCommandSourceStack(entity, serverLevel), command);
            }
        }
    }

    public CommandSourceStack createCommandSourceStack(LivingEntity entity, ServerLevel serverLevel) {
        return new CommandSourceStack(this, entity.position(), entity.getRotationVector(),
                serverLevel, 2, entity.getName().getString(), entity.getDisplayName(), entity.level.getServer(), entity)
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
}
