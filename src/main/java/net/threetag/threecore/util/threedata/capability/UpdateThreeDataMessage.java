package net.threetag.threecore.util.threedata.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.util.threedata.ThreeData;

import java.util.function.Supplier;

public class UpdateThreeDataMessage {

    public int entityId;
    public String dataKey;
    public CompoundNBT dataTag;

    public UpdateThreeDataMessage(int entityId, String dataKey, CompoundNBT dataTag) {
        this.entityId = entityId;
        this.dataKey = dataKey;
        this.dataTag = dataTag;
    }

    public UpdateThreeDataMessage(PacketBuffer buf) {
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
                entity.getCapability(CapabilityThreeData.THREE_DATA).ifPresent((threeDataHolder) -> {
                    ThreeData<?> data = threeDataHolder.getDataByName(this.dataKey);
                    threeDataHolder.readValue(data, this.dataTag);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
