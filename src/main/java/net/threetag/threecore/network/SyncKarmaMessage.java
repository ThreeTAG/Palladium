package net.threetag.threecore.network;

import net.threetag.threecore.capability.CapabilityKarma;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncKarmaMessage {

    public int entityId;
    public int karma;

    public SyncKarmaMessage(int entityId, int karma) {
        this.entityId = entityId;
        this.karma = karma;
    }

    public SyncKarmaMessage(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.karma = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.karma);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = net.minecraft.client.Minecraft.getInstance().world.getEntityByID(this.entityId);

            if (entity != null) {
                entity.getCapability(CapabilityKarma.KARMA).ifPresent((k) -> k.setKarma(this.karma));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
