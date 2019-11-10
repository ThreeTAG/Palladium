package net.threetag.threecore.util.entity.armorstand;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;
import net.threetag.threecore.ThreeCore;

public class ArmorStandPoseCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("armorstandpose").then(Commands.literal("reload").executes(context -> {
            ServerPlayerEntity player = context.getSource().asPlayer();
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SendArmorStandCommandMessage("reload"));
            return 1;
        })).then(Commands.argument("pose", StringArgumentType.word()).executes(context -> {
            ServerPlayerEntity player = context.getSource().asPlayer();
            ThreeCore.NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SendArmorStandCommandMessage(StringArgumentType.getString(context, "pose")));
            return 1;
        })));
    }

}
