package net.threetag.threecore.abilities.network;

import net.threetag.threecore.util.render.IIcon;
import net.threetag.threecore.util.render.IconSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.abilities.client.gui.SuperpowerToast;

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
        DistExecutor.callWhenOn(Dist.CLIENT, () -> () -> ctx.get().enqueueWork(this::showToast));
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void showToast() {
        Minecraft.getInstance().getToastGui().add(new SuperpowerToast(this.name, this.icon));
    }


}
