package net.threetag.threecore.network;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import net.threetag.threecore.scripts.events.LeftClickEmptyScriptEvent;
import net.threetag.threecore.scripts.events.RightClickEmptyScriptEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class EmptyHandInteractMessage
{

	public boolean left;

	public EmptyHandInteractMessage(boolean left)
	{
		this.left = left;
	}

	public EmptyHandInteractMessage(PacketBuffer buffer)
	{
		this.left = buffer.readBoolean();
	}

	public void toBytes(PacketBuffer buffer)
	{
		buffer.writeBoolean(this.left);
	}

	public void handle(Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() -> {
			ctx.get().getSender();
			if (this.left)
				new LeftClickEmptyScriptEvent(new PlayerInteractEvent.LeftClickEmpty(Objects.requireNonNull(ctx.get().getSender()))).fire();
			else
				new RightClickEmptyScriptEvent(new PlayerInteractEvent.RightClickEmpty(Objects.requireNonNull(ctx.get().getSender()), Hand.MAIN_HAND)).fire();
		});
		ctx.get().setPacketHandled(true);
	}
}
