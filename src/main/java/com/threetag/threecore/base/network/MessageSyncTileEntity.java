package com.threetag.threecore.base.network;

import com.threetag.threecore.util.block.ITileEntityListener;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncTileEntity {

    public NBTTagCompound nbt;

    public MessageSyncTileEntity(NBTTagCompound nbtTagCompound) {
        this.nbt = nbtTagCompound;
    }

    public MessageSyncTileEntity(PacketBuffer buf) {
        this.nbt = buf.readCompoundTag();
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeCompoundTag(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            EntityPlayer player = Minecraft.getInstance().player;

            if (player.openContainer instanceof ITileEntityListener) {
                ((ITileEntityListener) player.openContainer).sync(this.nbt);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
