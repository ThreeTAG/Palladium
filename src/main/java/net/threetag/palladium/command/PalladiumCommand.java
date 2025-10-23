package net.threetag.palladium.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.common.NeoForge;

public class PalladiumCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("palladium");
        NeoForge.EVENT_BUS.post(new RegisterPalladiumCommandsEvent(root, context));
        dispatcher.register(root);
    }

}
