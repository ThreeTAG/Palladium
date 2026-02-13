package net.threetag.palladium.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.Permissions;
import net.neoforged.neoforge.network.PacketDistributor;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.network.OpenScreenPacket;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ScreenCommand {

    public static final PermissionCheck PERMISSION_CHECK = new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);

    public static final String TRANS_SHOWED_SCREEN_SINGLE = "commands.palladium.screen.success.single";
    public static final String TRANS_SHOWED_SCREEN_MULTIPLE = "commands.palladium.screen.success.multiple";

    public static final SuggestionProvider<SharedSuggestionProvider> AVAILABLE_SCREENS = SuggestionProviders.register(
            Palladium.id("available_screens"),
            (context, builder) -> SharedSuggestionProvider.suggestResource(Palladium.PROXY.getAvailableScreenIds(), builder)
    );

    public static void register(LiteralArgumentBuilder<CommandSourceStack> builder, CommandBuildContext context) {
        builder.then(Commands.literal("screen").requires(Commands.hasPermission(PERMISSION_CHECK))
                .then(Commands.argument("screen", IdentifierArgument.id())
                        .suggests(SuggestionProviders.cast(AVAILABLE_SCREENS))
                        .executes(c -> openScreen(c.getSource(), IdentifierArgument.getId(c, "screen"), null))
                        .then(Commands.argument("players", EntityArgument.players())
                                .executes(c -> openScreen(c.getSource(), IdentifierArgument.getId(c, "screen"), EntityArgument.getPlayers(c, "players"))))));
    }

    private static int openScreen(CommandSourceStack source, Identifier screenId, @Nullable Collection<ServerPlayer> players) throws CommandSyntaxException {
        if (players == null) {
            players = Collections.singletonList(source.getPlayerOrException());
        }

        for (ServerPlayer player : players) {
            PacketDistributor.sendToPlayer(player, new OpenScreenPacket(screenId));
        }

        List<ServerPlayer> finalPlayers = new ArrayList<>(players);
        if (players.size() == 1) {
            source.sendSuccess(() -> Component.translatableEscape(TRANS_SHOWED_SCREEN_SINGLE, screenId, finalPlayers.getFirst().getDisplayName()), true);
        } else {
            source.sendSuccess(() -> Component.translatableEscape(TRANS_SHOWED_SCREEN_MULTIPLE, screenId, finalPlayers.size()), true);
        }

        return 1;
    }

}
