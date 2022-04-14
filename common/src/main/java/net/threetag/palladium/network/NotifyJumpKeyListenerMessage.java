package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.entity.FlightHandler;

public class NotifyJumpKeyListenerMessage extends BaseC2SMessage {

    private final boolean active;

    public NotifyJumpKeyListenerMessage(boolean active) {
        this.active = active;
    }

    public NotifyJumpKeyListenerMessage(FriendlyByteBuf buf) {
        this.active = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.NOTIFY_JUMP_KEY_LISTENER;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.active);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> FlightHandler.JUMP_KEY_DOWN.set(context.getPlayer(), this.active));
    }
}
