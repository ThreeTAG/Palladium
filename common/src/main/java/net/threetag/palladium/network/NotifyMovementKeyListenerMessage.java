package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.util.property.BooleanProperty;
import net.threetag.palladium.util.property.PalladiumProperties;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;

public class NotifyMovementKeyListenerMessage extends MessageC2S {

    private final int type;
    private final boolean active;

    public NotifyMovementKeyListenerMessage(int type, boolean active) {
        this.type = type;
        this.active = active;
    }

    public NotifyMovementKeyListenerMessage(FriendlyByteBuf buf) {
        this.type = buf.readInt();
        this.active = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.NOTIFY_MOVEMENT_KEY_LISTENER;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.type);
        buf.writeBoolean(this.active);
    }

    @Override
    public void handle(MessageContext context) {
        PalladiumProperty<Boolean> property = null;
        switch (this.type) {
            case 0 -> property = PalladiumProperties.JUMP_KEY_DOWN;
            case 1 -> property = PalladiumProperties.LEFT_KEY_DOWN;
            case 2 -> property = PalladiumProperties.RIGHT_KEY_DOWN;
            case 3 -> property = PalladiumProperties.FORWARD_KEY_DOWN;
            case 4 -> property = PalladiumProperties.BACKWARDS_KEY_DOWN;
        }

        if(property != null) {
            property.set(context.getPlayer(), this.active);
        }
    }
}
