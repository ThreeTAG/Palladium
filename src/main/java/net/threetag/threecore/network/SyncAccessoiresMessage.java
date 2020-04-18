package net.threetag.threecore.network;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.accessoires.Accessoire;
import net.threetag.threecore.capability.CapabilityAccessoires;
import net.threetag.threecore.client.gui.AccessoireScreen;

import java.util.Collection;
import java.util.function.Supplier;

public class SyncAccessoiresMessage {

    public int entityId;
    public Collection<Accessoire> accessoires;

    public SyncAccessoiresMessage(int entityId, Collection<Accessoire> accessoires) {
        this.entityId = entityId;
        this.accessoires = accessoires;
    }

    public SyncAccessoiresMessage(PacketBuffer buf) {
        this.entityId = buf.readInt();
        int amount = buf.readInt();
        this.accessoires = Lists.newArrayList();
        for (int i = 0; i < amount; i++) {
            this.accessoires.add(buf.readRegistryIdSafe(Accessoire.class));
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.accessoires.size());

        for (Accessoire accessoire : this.accessoires) {
            buf.writeRegistryId(accessoire);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = net.minecraft.client.Minecraft.getInstance().world.getEntityByID(this.entityId);

            if (entity instanceof AbstractClientPlayerEntity) {
                entity.getCapability(CapabilityAccessoires.ACCESSOIRES).ifPresent((k) -> {
                    for (Accessoire accessoire : ImmutableList.copyOf(k.getActiveAccessoires())) {
                        k.disable(accessoire, (PlayerEntity) entity);
                        accessoire.remove((AbstractClientPlayerEntity) entity);
                    }
                    for (Accessoire accessoire : this.accessoires) {
                        k.enable(accessoire, (PlayerEntity) entity);
                        accessoire.apply((AbstractClientPlayerEntity) entity);
                    }
                    if (Minecraft.getInstance().currentScreen instanceof AccessoireScreen) {
                        ((AccessoireScreen) Minecraft.getInstance().currentScreen).accessoireList.refreshList();
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
