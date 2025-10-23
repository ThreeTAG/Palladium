package net.threetag.palladium.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.Event;

public class RegisterPalladiumCommandsEvent extends Event {

    private final LiteralArgumentBuilder<CommandSourceStack> builder;
    private final CommandBuildContext context;

    public RegisterPalladiumCommandsEvent(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context) {
        this.builder = builder;
        this.context = context;
    }

    public LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return this.builder;
    }

    public CommandBuildContext getBuildContext() {
        return this.context;
    }
}
