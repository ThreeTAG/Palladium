package com.threetag.threecore.karma.network;

import com.threetag.threecore.karma.client.KarmaToast;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageKarmaInfo {

    public int karma;

    public MessageKarmaInfo(int karma) {
        this.karma = karma;
    }

    public MessageKarmaInfo(ByteBuf buf) {
        this.karma = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.karma);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> KarmaToast.addOrUpdate(Minecraft.getInstance().getToastGui(), this.karma));
        ctx.get().setPacketHandled(true);
    }
}
