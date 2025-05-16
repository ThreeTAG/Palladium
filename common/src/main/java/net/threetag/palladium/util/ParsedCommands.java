package net.threetag.palladium.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.threetag.palladium.Palladium;

import java.util.Collections;
import java.util.List;

public class ParsedCommands {

    public static final ParsedCommands EMPTY = new ParsedCommands(Collections.emptyList());
    public static final Codec<ParsedCommands> CODEC = PalladiumCodecs.listOrPrimitive(Codec.STRING).xmap(ParsedCommands::new, ParsedCommands::getLines);
    public static final StreamCodec<ByteBuf, ParsedCommands> STREAM_CODEC = ByteBufCodecs.collection(Utils::newList, ByteBufCodecs.STRING_UTF8).map(ParsedCommands::new, ParsedCommands::getLines);

    private final List<String> lines;
    private CommandFunction<CommandSourceStack> commandFunction;

    public ParsedCommands(List<String> lines) {
        this.lines = lines;
    }

    public ParsedCommands(String line) {
        this.lines = Collections.singletonList(line);
    }

    public CommandFunction<CommandSourceStack> getCommandFunction(MinecraftServer server) {
        if (this.commandFunction == null) {
            CommandSourceStack commandSourceStack = new CommandSourceStack(CommandSource.NULL, Vec3.ZERO, Vec2.ZERO, null, 2, "", CommonComponents.EMPTY, server, null);
            this.commandFunction = CommandFunction.fromLines(Palladium.id("parsed"), server.getCommands().getDispatcher(), commandSourceStack, this.lines);
        }
        return this.commandFunction;
    }

    public List<String> getLines() {
        return this.lines;
    }

}
