package net.threetag.threecore.ability;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.threetag.threecore.util.icon.ItemIcon;
import net.threetag.threecore.util.threedata.CommandListThreeData;
import net.threetag.threecore.util.threedata.EnumSync;
import net.threetag.threecore.util.threedata.ThreeData;

public class CommandAbility extends Ability {

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
}
