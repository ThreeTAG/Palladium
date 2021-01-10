package net.threetag.threecore.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.accessoires.AccessoireSlot;
import net.threetag.threecore.capability.CapabilityAccessoires;
import net.threetag.threecore.client.gui.AccessoireScreen;

import java.util.*;
import java.util.function.Supplier;

public class SyncAccessoiresMessage {

    public int entityId;
    public Map<AccessoireSlot, Collection<Accessoire>> accessoires;

    public SyncAccessoiresMessage(int entityId, Map<AccessoireSlot, Collection<Accessoire>> accessoires) {
        this.entityId = entityId;
        this.accessoires = accessoires;
    }

    public SyncAccessoiresMessage(PacketBuffer buf) {
        this.entityId = buf.readInt();
        int amount = buf.readInt();
        this.accessoires = new HashMap<>();
        for (int i = 0; i < amount; i++) {
            AccessoireSlot slot = AccessoireSlot.getSlotByName(buf.readString(32767));
            List<Accessoire> accessoireList = new ArrayList<>();
            int slotAmount = buf.readInt();
            for (int j = 0; j < slotAmount; j++) {
                accessoireList.add(buf.readRegistryIdSafe(Accessoire.class));
            }
            this.accessoires.put(slot, accessoireList);
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.accessoires.size());

        this.accessoires.forEach((slot, accessoireList) -> {
            buf.writeString(slot.getName());
            buf.writeInt(accessoireList.size());
            for (Accessoire accessoire : accessoireList) {
                buf.writeRegistryId(accessoire);
            }
        });
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = net.minecraft.client.Minecraft.getInstance().world.getEntityByID(this.entityId);

            if (entity instanceof AbstractClientPlayerEntity) {
                entity.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent((k) -> {
                    k.clear((PlayerEntity) entity);
                    this.accessoires.forEach((slot, accessoireList) -> {
                        for (Accessoire accessoire : accessoireList) {
                            k.enable(slot, accessoire, (PlayerEntity) entity);
                        }
                    });
                    if (Minecraft.getInstance().currentScreen instanceof AccessoireScreen) {
                        ((AccessoireScreen) Minecraft.getInstance().currentScreen).accessoireList.refreshList();
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
