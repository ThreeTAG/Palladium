package net.threetag.threecore.karma.network;

import net.threetag.threecore.karma.client.KarmaToast;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class KarmaInfoMessage {

    public int karma;

    public KarmaInfoMessage(int karma) {
        this.karma = karma;
    }

    public KarmaInfoMessage(ByteBuf buf) {
        this.karma = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.karma);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> KarmaToast.addOrUpdate(net.minecraft.client.Minecraft.getInstance().getToastGui(), this.karma));
        ctx.get().setPacketHandled(true);
    }
}
