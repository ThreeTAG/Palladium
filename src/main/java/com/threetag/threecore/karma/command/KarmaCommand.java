package com.threetag.threecore.karma.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.threetag.threecore.karma.KarmaClass;
import com.threetag.threecore.karma.capability.CapabilityKarma;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class KarmaCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("karma").requires((player) -> {
            return player.hasPermissionLevel(2);
        }).then(Commands.literal("set").then(Commands.argument("players", EntityArgument.multiplePlayers()).then(Commands.argument("value", IntegerArgumentType.integer(CapabilityKarma.MIN, CapabilityKarma.MAX)).executes((c) -> {
            return setKarma(c.getSource(), EntityArgument.getPlayers(c, "players"), IntegerArgumentType.getInteger(c, "value"));
        })))).then(Commands.argument("player", EntityArgument.singlePlayer()).executes((c) -> {
            return sendKarmaInfo(c.getSource(), EntityArgument.getOnePlayer(c, "player"));
        })));
    }

    private static int setKarma(CommandSource commandSource, Collection<EntityPlayerMP> players, int karma) {
        Iterator iterator = players.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            EntityPlayer pl = (EntityPlayer) iterator.next();
            CapabilityKarma.setKarma(pl, karma);
            if (pl.getCapability(CapabilityKarma.KARMA).isPresent())
                i++;
        }

        if (i == 1) {
            commandSource.sendFeedback(new TextComponentTranslation("commands.karma.success.player.single", new Object[]{(players.iterator().next()).getDisplayName(), karma}), true);
        } else {
            commandSource.sendFeedback(new TextComponentTranslation("commands.karma.success.player.multiple", new Object[]{i, karma}), true);
        }

        return players.size();
    }

    public static int sendKarmaInfo(CommandSource commandSource, EntityPlayer player) {
        AtomicInteger i = new AtomicInteger();
        player.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> {
            i.set(1);
            commandSource.sendFeedback(new TextComponentTranslation("commands.karma.players_karma", player.getDisplayName(), k.getKarma(), KarmaClass.fromKarma(k.getKarma()).getDisplayName()), true);
        });

        return i.get();
    }

}
