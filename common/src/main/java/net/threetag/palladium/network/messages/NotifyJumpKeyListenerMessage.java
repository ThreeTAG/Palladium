package net.threetag.palladium.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.network.MessageC2S;
import net.threetag.palladium.network.MessageContext;
import net.threetag.palladium.network.MessageType;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.util.property.PalladiumProperties;

public class NotifyJumpKeyListenerMessage extends MessageC2S {

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
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.active);
    }

    @Override
    public void handle(MessageContext context) {
        PalladiumProperties.JUMP_KEY_DOWN.set(context.getPlayer(), this.active);
    }
}
