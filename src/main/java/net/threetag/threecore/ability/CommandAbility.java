package net.threetag.threecore.ability;

import net.minecraft.block.Blocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.threedata.CommandListThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ThreeData;

public class CommandAbility extends Ability implements ICommandSource {

    public static final ThreeData<CommandListThreeData.CommandList> COMMANDS = new CommandListThreeData("commands").setSyncType(EnumSync.NONE).enableSetting("commands", "Sets the commands which get executed when using the ability");

    public CommandAbility() {
        super(AbilityType.COMMAND);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ICON, new ItemIcon(new ItemStack(Blocks.COMMAND_BLOCK)));
        this.dataManager.register(COMMANDS, new CommandListThreeData.CommandList().addCommand("/say Hello World"));
    }

    @Override
    public void action(LivingEntity entity) {
        if (entity.world.getServer() != null) {
            for (String command : this.get(COMMANDS).getCommands()) {
                entity.world.getServer().getCommandManager().handleCommand(new CommandSource(this, entity.getPositionVec(), entity.getPitchYaw(), entity.world instanceof ServerWorld ? (ServerWorld) entity.world : null, 4, entity.getName().getString(), entity.getDisplayName(), entity.world.getServer(), entity), command);
            }
        }
    }

    @Override
    public void sendMessage(ITextComponent component) {
        ThreeCore.LOGGER.error("Ability command error: " + component.getFormattedText());
    }

    @Override
    public boolean shouldReceiveFeedback() {
        return false;
    }

    @Override
    public boolean shouldReceiveErrors() {
        return true;
    }

    @Override
    public boolean allowLogging() {
        return false;
    }
}
