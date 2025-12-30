package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.multiverse.ClientMultiversalItemVariantsManager;
import net.threetag.palladium.multiverse.MultiversalItemVariants;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;

import java.util.Map;

public class SyncMultiversalItemVariantsMessage extends MessageS2C {

    private final Map<ResourceLocation, MultiversalItemVariants> byUniverseId;

    public SyncMultiversalItemVariantsMessage(Map<ResourceLocation, MultiversalItemVariants> byUniverseId) {
        this.byUniverseId = byUniverseId;
    }

    public SyncMultiversalItemVariantsMessage(FriendlyByteBuf buf) {
        this.byUniverseId = buf.readMap(FriendlyByteBuf::readResourceLocation, MultiversalItemVariants::fromNetwork);
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_MULTIVERSAL_ITEM_VARIANTS;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.byUniverseId, FriendlyByteBuf::writeResourceLocation,
                (b, v) -> v.toNetwork(b));
    }

    @Override
    public void handle(MessageContext context) {
        this.handleClient();
    }

    @Environment(EnvType.CLIENT)
    public void handleClient() {
        ClientMultiversalItemVariantsManager.updateEntries(this.byUniverseId);
    }
}
