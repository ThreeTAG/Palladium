package net.threetag.threecore.network;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.capability.CapabilitySizeChanging;
import net.threetag.threecore.util.threedata.IThreeDataHolder;
import net.threetag.threecore.util.threedata.ThreeData;

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
        this.dataKey = buf.readString(32767);
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
                        ThreeData<?> data = ((IThreeDataHolder) sizeChanging).getDataByName(this.dataKey);
                        ((IThreeDataHolder) sizeChanging).readValue(data, this.dataTag);
                        sizeChanging.updateBoundingBox();
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
