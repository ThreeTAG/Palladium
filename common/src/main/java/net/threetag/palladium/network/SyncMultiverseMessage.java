package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.multiverse.ClientMultiversalManager;
import net.threetag.palladium.multiverse.Universe;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;

import java.util.HashMap;
import java.util.Map;

public class SyncMultiverseMessage extends MessageS2C {

    private final Map<ResourceLocation, Universe> universes;

    public SyncMultiverseMessage(Map<ResourceLocation, Universe> universes) {
        this.universes = universes;
    }

    public SyncMultiverseMessage(FriendlyByteBuf buf) {
        this.universes = new HashMap<>();
        int amount = buf.readInt();

        for (int i = 0; i < amount; i++) {
            ResourceLocation id = buf.readResourceLocation();
            this.universes.put(id, Universe.fromNetwork(id, buf));
        }
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_MULTIVERSE;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.universes.size());
        this.universes.forEach((id, power) -> {
            buf.writeResourceLocation(id);
            power.toNetwork(buf);
        });
    }

    @Override
    public void handle(MessageContext context) {
        this.handleClient();
    }

    @Environment(EnvType.CLIENT)
    public void handleClient() {
        ClientMultiversalManager.updateUniverses(this.universes);
    }
}
