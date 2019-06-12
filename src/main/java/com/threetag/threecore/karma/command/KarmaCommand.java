package com.threetag.threecore.karma.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.threetag.threecore.karma.KarmaClass;
import com.threetag.threecore.karma.capability.CapabilityKarma;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class KarmaCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("karma").requires((player) -> {
            return player.hasPermissionLevel(2);
        }).then(Commands.literal("set").then(Commands.argument("players", EntityArgument.players()).then(Commands.argument("value", IntegerArgumentType.integer(CapabilityKarma.MIN, CapabilityKarma.MAX)).executes((c) -> {
            return setKarma(c.getSource(), EntityArgument.getPlayers(c, "players"), IntegerArgumentType.getInteger(c, "value"));
        })))).then(Commands.argument("player", EntityArgument.player()).executes((c) -> {
            return sendKarmaInfo(c.getSource(), EntityArgument.getPlayer(c, "player"));
        })));
    }

    private static int setKarma(CommandSource commandSource, Collection<ServerPlayerEntity> players, int karma) {
        Iterator iterator = players.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            PlayerEntity pl = (PlayerEntity) iterator.next();
            CapabilityKarma.setKarma(pl, karma);
            if (pl.getCapability(CapabilityKarma.KARMA).isPresent())
                i++;
        }

        if (i == 1) {
            commandSource.sendFeedback(new TranslationTextComponent("commands.karma.success.player.single", new Object[]{(players.iterator().next()).getDisplayName(), karma}), true);
        } else {
            commandSource.sendFeedback(new TranslationTextComponent("commands.karma.success.player.multiple", new Object[]{i, karma}), true);
        }

        return players.size();
    }

    public static int sendKarmaInfo(CommandSource commandSource, PlayerEntity player) {
        AtomicInteger i = new AtomicInteger();
        player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
            i.set(1);
            commandSource.sendFeedback(new TranslationTextComponent("commands.karma.players_karma", player.getDisplayName(), k.getKarma(), KarmaClass.fromKarma(k.getKarma()).getDisplayName()), true);
        });

        return i.get();
    }

}
