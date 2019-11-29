package net.threetag.threecore.base.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.base.block.ConstructionTableBlock;
import net.threetag.threecore.base.inventory.AbstractConstructionTableContainer;

import java.util.function.Supplier;

public class OpenConstructionTableTabMessage {

    public ResourceLocation tabId;

    public OpenConstructionTableTabMessage(ResourceLocation tabId) {
        this.tabId = tabId;
    }

    public OpenConstructionTableTabMessage(PacketBuffer buffer) {
        this.tabId = new ResourceLocation(buffer.readString(64));
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeString(this.tabId.toString());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerEntity player = ctx.get().getSender();
            if (player != null) {
                ConstructionTableBlock.Tab tab = ConstructionTableBlock.getTabs().get(this.tabId);
                if (tab != null && player.openContainer instanceof AbstractConstructionTableContainer) {
                    ((AbstractConstructionTableContainer) player.openContainer).worldPosCallable.consume((world, pos) ->
                            player.openContainer(ConstructionTableBlock.getContainerProvider(world, pos, tab)));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
