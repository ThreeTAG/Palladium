package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.menu.TailoringMenu;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

public class TailoringJeiTransferMessage extends MessageC2S {

    private final int sourceSlot;
    private final int targetSlot;
    private final int count;

    public TailoringJeiTransferMessage(int sourceSlot, int targetSlot, int count) {
        this.sourceSlot = sourceSlot;
        this.targetSlot = targetSlot;
        this.count = count;
    }

    public TailoringJeiTransferMessage(FriendlyByteBuf buf) {
        this.sourceSlot = buf.readInt();
        this.targetSlot = buf.readInt();
        this.count = buf.readInt();
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.TAILORING_JEI_TRANSFER;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.sourceSlot);
        buf.writeInt(this.targetSlot);
        buf.writeInt(this.count);
    }

    @Override
    public void handle(MessageContext context) {
        var player = context.getPlayer();

        // If the source and target slots are different, perform the transfer
        if (this.sourceSlot != this.targetSlot) {
            if (player.containerMenu instanceof TailoringMenu menu) {
                ItemStack sourceStack = menu.getSlot(this.sourceSlot).safeTake(count, count, player);

                if (!sourceStack.isEmpty()) {
                    menu.getSlot(this.targetSlot).safeInsert(sourceStack);
                } else {
                    Palladium.LOGGER.error("Tailoring JEI Transfer: Source slot {} is empty", this.sourceSlot);
                }
            }
        }
    }
}
