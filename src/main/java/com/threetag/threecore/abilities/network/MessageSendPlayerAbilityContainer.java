package com.threetag.threecore.abilities.network;

import com.threetag.threecore.abilities.capability.CapabilityAbilityContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSendPlayerAbilityContainer {

    private int entityID;
    public NBTTagCompound nbt;

    public MessageSendPlayerAbilityContainer(int entityID, NBTTagCompound nbt) {
        this.entityID = entityID;
        this.nbt = nbt;
    }

    public MessageSendPlayerAbilityContainer(PacketBuffer buffer) {
        this.entityID = buffer.readInt();
        this.nbt = buffer.readCompoundTag();
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeInt(this.entityID);
        buffer.writeCompoundTag(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().world.getEntityByID(this.entityID);
            if (entity != null) {
                entity.getCapability(CapabilityAbilityContainer.ABILITY_CONTAINER).ifPresent((a) -> {
                    if (a instanceof INBTSerializable) {
                        ((INBTSerializable) a).deserializeNBT(this.nbt);
                    }
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
