package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.ClientPowerManager;
import net.threetag.palladium.power.Power;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;

import java.util.HashMap;
import java.util.Map;

public class SyncPowersMessage extends MessageS2C {

    private final Map<ResourceLocation, Power> powers;

    public SyncPowersMessage(Map<ResourceLocation, Power> powers) {
        this.powers = powers;
    }

    public SyncPowersMessage(FriendlyByteBuf buf) {
        this.powers = new HashMap<>();
        int amount = buf.readInt();

        for (int i = 0; i < amount; i++) {
            ResourceLocation id = buf.readResourceLocation();
            this.powers.put(id, Power.fromBuffer(id, buf));
        }
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_POWERS;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.powers.size());
        this.powers.forEach((id, power) -> {
            buf.writeResourceLocation(id);
            power.toBuffer(buf);
        });
    }

    @Override
    public void handle(MessageContext context) {
        ClientPowerManager.updatePowers(this.powers);
    }
}
