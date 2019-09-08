package com.threetag.threecore.sizechanging.network;

import com.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncSizeMessage {

    public int entityId;
    public CompoundNBT data;

    public SyncSizeMessage(int entityId, CompoundNBT data) {
        this.entityId = entityId;
        this.data = data;
    }

    public SyncSizeMessage(PacketBuffer buf) {
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
                entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent((sizeChanging) -> {
                    if (sizeChanging instanceof INBTSerializable) {
                        ((INBTSerializable) sizeChanging).deserializeNBT(this.data);
                        sizeChanging.updateBoundingBox(entity);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
