package net.threetag.threecore.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.capability.CapabilityAccessoires;

import java.util.function.Supplier;

public class ToggleAccessoireMessage {

    public Accessoire accessoire;

    public ToggleAccessoireMessage(Accessoire accessoire) {
        this.accessoire = accessoire;
    }

    public ToggleAccessoireMessage(PacketBuffer packetBuffer) {
        this.accessoire = packetBuffer.readRegistryIdSafe(Accessoire.class);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeRegistryId(this.accessoire);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> {
                    if (accessoireHolder.getActiveAccessoires().contains(this.accessoire)) {
                        accessoireHolder.disable(this.accessoire, player);
                    } else {
                        accessoireHolder.enable(this.accessoire, player);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
