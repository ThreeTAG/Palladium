package com.threetag.threecore.abilities.network;

import com.threetag.threecore.abilities.client.gui.SuperpowerToast;
import com.threetag.threecore.util.render.IIcon;
import com.threetag.threecore.util.render.IconSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SendSuperpowerToastMessage {

    public ITextComponent name;
    public IIcon icon;

    public SendSuperpowerToastMessage(ITextComponent name, IIcon icon) {
        this.name = name;
        this.icon = icon;
    }

    public SendSuperpowerToastMessage(PacketBuffer buffer) {
        this.name = buffer.readTextComponent();
        this.icon = IconSerializer.deserialize(buffer.readCompoundTag());
    }

    public void toBytes(PacketBuffer buffer) {
        buffer.writeTextComponent(this.name);
        buffer.writeCompoundTag(this.icon.getSerializer().serializeExt(this.icon));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> Minecraft.getInstance().getToastGui().add(new SuperpowerToast(this.name, this.icon)));
        ctx.get().setPacketHandled(true);
    }

}
