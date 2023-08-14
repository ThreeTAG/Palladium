package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.entity.FlightHandler;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;

public class SetFlyingStateMessage extends MessageC2S {

    private final boolean flying;

    public SetFlyingStateMessage(boolean flying) {
        this.flying = flying;
    }

    public SetFlyingStateMessage(FriendlyByteBuf buf) {
        this.flying = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SET_FLYING_STATE;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.flying);
    }

    @Override
    public void handle(MessageContext context) {
        if (context.getPlayer() instanceof PalladiumPlayerExtension extension) {
            var flight = extension.palladium$getFlightHandler();
            if (this.flying) {
                var flightType = FlightHandler.getAvailableFlightType(context.getPlayer());

                if (flightType.isNotNull()) {
                    flight.setFlightType(flightType);
                }
            } else {
                flight.setFlightType(FlightHandler.FlightType.NONE);
            }

            context.getPlayer().getAbilities().flying = false;
        }
    }
}
