package net.threetag.palladium.network.forge;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.network.MessageC2S;
import net.threetag.palladium.network.MessageS2C;
import net.threetag.palladium.network.MessageType;
import net.threetag.palladium.network.NetworkManager;

import java.util.Optional;
import java.util.function.Supplier;

public class NetworkManagerImpl extends NetworkManager {

    private final SimpleChannel channel;

    public static NetworkManager create(ResourceLocation channelName) {
        return new NetworkManagerImpl(channelName);
    }

    public NetworkManagerImpl(ResourceLocation channelName) {
        super(channelName);
        this.channel = NetworkRegistry.newSimpleChannel(channelName, () -> "1.0.0", (s) -> true, (s) -> true);
        this.channel.registerMessage(0, ToServer.class, ToServer::toBytes, ToServer::new, ToServer::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        this.channel.registerMessage(1, ToClient.class, ToClient::toBytes, ToClient::new, ToClient::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    @Override
    public void sendToServer(MessageC2S message) {
        this.channel.sendToServer(new ToServer(message));
    }

    @Override
    public void sendToPlayer(ServerPlayer player, MessageS2C message) {
        this.channel.send(PacketDistributor.PLAYER.with(() -> player), new ToClient(message));
    }

    public class ToServer {

        private final MessageC2S message;

        public ToServer(MessageC2S message) {
            this.message = message;
        }

        public ToServer(FriendlyByteBuf buf) {
            var msgId = buf.readUtf();

            if (!NetworkManagerImpl.this.toServer.containsKey(msgId)) {
                Palladium.LOGGER.warn("Unknown message id received on server: " + msgId);
            }

            MessageType type = NetworkManagerImpl.this.toServer.get(msgId);
            this.message = (MessageC2S) type.getDecoder().decode(buf);
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(this.message.getType().getId());
            this.message.toBytes(buf);
        }

        public static void handle(ToServer msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(msg.message::handle);
            ctx.get().setPacketHandled(true);
        }

    }

    public class ToClient {

        private final MessageS2C message;

        public ToClient(MessageS2C message) {
            this.message = message;
        }

        public ToClient(FriendlyByteBuf buf) {
            var msgId = buf.readUtf();

            if (!NetworkManagerImpl.this.toClient.containsKey(msgId)) {
                Palladium.LOGGER.warn("Unknown message id received on client: " + msgId);
            }

            MessageType type = NetworkManagerImpl.this.toClient.get(msgId);
            this.message = (MessageS2C) type.getDecoder().decode(buf);
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(this.message.getType().getId());
            this.message.toBytes(buf);
        }

        public static void handle(ToClient msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(msg.message::handle);
            ctx.get().setPacketHandled(true);
        }

    }

}
