package net.threetag.threecore.sizechanging.network;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.sizechanging.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.threedata.IThreeDataHolder;

import java.util.function.Supplier;

public class UpdateSizeData {

    public int entityId;
    public String dataKey;
    public CompoundNBT dataTag;

    public UpdateSizeData(int entityId, String dataKey, CompoundNBT dataTag) {
        this.entityId = entityId;
        this.dataKey = dataKey;
        this.dataTag = dataTag;
    }

    public UpdateSizeData(PacketBuffer buf) {
        this.entityId = buf.readInt();
        this.dataKey = buf.readString(32);
        this.dataTag = buf.readCompoundTag();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.entityId);
        buf.writeString(this.dataKey);
        buf.writeCompoundTag(this.dataTag);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = net.minecraft.client.Minecraft.getInstance().world.getEntityByID(this.entityId);

            if (entity != null) {
                entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING).ifPresent((sizeChanging) -> {
                    if (sizeChanging instanceof IThreeDataHolder) {
                        ((IThreeDataHolder) sizeChanging).setData(this.dataKey, this.dataTag);
                        sizeChanging.updateBoundingBox();
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
