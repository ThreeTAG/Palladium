package net.threetag.threecore.util.threedata.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncThreeDataMessage {

    public int entityId;
    public CompoundNBT data;

    public SyncThreeDataMessage(int entityId, CompoundNBT data) {
        this.entityId = entityId;
        this.data = data;
    }

    public SyncThreeDataMessage(PacketBuffer buf) {
        this.entityId = buf.readInt();
        this.data = buf.readCompoundTag();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityId);
        buf.writeCompoundTag(this.data);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = net.minecraft.client.Minecraft.getInstance().world.getEntityByID(this.entityId);

            if (entity != null) {
                entity.getCapability(CapabilityThreeData.THREE_DATA).ifPresent((threeData) -> {
                    if (threeData instanceof INBTSerializable) {
                        ((INBTSerializable) threeData).deserializeNBT(this.data);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
