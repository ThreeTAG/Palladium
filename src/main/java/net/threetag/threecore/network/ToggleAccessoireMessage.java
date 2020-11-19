package net.threetag.threecore.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.accessoires.AccessoireSlot;
import net.threetag.threecore.capability.CapabilityAccessoires;

import java.util.Collection;
import java.util.function.Supplier;

public class ToggleAccessoireMessage {

    public AccessoireSlot slot;
    public Accessoire accessoire;

    public ToggleAccessoireMessage(AccessoireSlot slot, Accessoire accessoire) {
        this.slot = slot;
        this.accessoire = accessoire;
    }

    public ToggleAccessoireMessage(PacketBuffer packetBuffer) {
        this.slot = AccessoireSlot.getSlotByName(packetBuffer.readString());
        this.accessoire = packetBuffer.readRegistryIdSafe(Accessoire.class);
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeString(this.slot.getName());
        buf.writeRegistryId(this.accessoire);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                player.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent(accessoireHolder -> {
                    if (this.slot != null && this.accessoire != null) {
                        Collection<Accessoire> accessoires = accessoireHolder.getSlots().get(this.slot);
                        if (accessoires == null || !accessoires.contains(this.accessoire)) {
                            accessoireHolder.enable(this.slot, this.accessoire, player);
                        } else {
                            accessoireHolder.disable(this.slot, this.accessoire, player);
                        }
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
