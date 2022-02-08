package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ClientPowerManager;
import net.threetag.palladium.power.Power;

import java.util.HashMap;
import java.util.Map;

public class SyncPowersMessage extends BaseS2CMessage {

    private final Map<ResourceLocation, Power> powers;

    public SyncPowersMessage(Map<ResourceLocation, Power> powers) {
        this.powers = powers;
    }

    public SyncPowersMessage(FriendlyByteBuf buf) {
        this.powers = new HashMap<>();
        int amount = buf.readInt();

        for (int i = 0; i < amount; i++) {
            ResourceLocation id = buf.readResourceLocation();
            powers.put(id, Power.fromBuffer(id, buf));
        }
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_POWERS;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.powers.size());
        this.powers.forEach((id, power) -> {
            buf.writeResourceLocation(id);
            power.toBuffer(buf);
        });
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> ClientPowerManager.updatePowers(this.powers));
    }
}
