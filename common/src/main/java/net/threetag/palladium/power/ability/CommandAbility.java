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
import java.util.UUID;

public class CommandAbility extends Ability implements CommandSource {

    public static final PalladiumProperty<String[]> COMMANDS = new StringArrayProperty("commands").configurable("Sets the commands which get executed when using the ability");

    public CommandAbility() {
        this.withProperty(ICON, new ItemIcon(Blocks.COMMAND_BLOCK));
        this.withProperty(COMMANDS, new String[]{"say Hello World"});
    }

    @Override
    public void tick(LivingEntity entity, AbilityEntry entry, IPowerHolder holder, boolean enabled) {
        if (enabled && entity.level.getServer() != null) {
            for (String command : Objects.requireNonNull(this.propertyManager.get(COMMANDS))) {
                entity.level.getServer().getCommands().performCommand(new CommandSourceStack(this, entity.position(), entity.getRotationVector(), entity.level instanceof ServerLevel ? (ServerLevel) entity.level : null, 4, entity.getName().getString(), entity.getDisplayName(), entity.level.getServer(), entity), command);
            }
        }
    }

    @Override
    public void sendMessage(Component component, UUID senderUUID) {

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
