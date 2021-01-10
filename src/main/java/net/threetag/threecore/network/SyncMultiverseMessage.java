package net.threetag.threecore.network;

import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.item.recipe.MultiverseManager;

import java.util.List;
import java.util.function.Supplier;

public class SyncMultiverseMessage {

    public List<String> universes;

    public SyncMultiverseMessage(List<String> universes) {
        this.universes = universes;
    }

    public SyncMultiverseMessage(PacketBuffer buf) {
        this.universes = Lists.newArrayList();
        int j = buf.readInt();
        for (int i = 0; i < j; i++) {
            this.universes.add(buf.readString(32767));
        }
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeInt(this.universes.size());
        for (String s : this.universes) {
            buf.writeString(s);
        }
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            MultiverseManager.CLIENT_UNIVERSES.clear();
            MultiverseManager.CLIENT_UNIVERSES.addAll(this.universes);
        });
        ctx.get().setPacketHandled(true);
    }

}
