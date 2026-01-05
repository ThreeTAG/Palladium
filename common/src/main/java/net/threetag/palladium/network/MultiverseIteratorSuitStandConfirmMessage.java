package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.menu.MultiversalIteratorSuitStandMenu;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

public class MultiverseIteratorSuitStandConfirmMessage extends MessageC2S {

    private final int pageId;

    public MultiverseIteratorSuitStandConfirmMessage(FriendlyByteBuf buf) {
        this.pageId = buf.readInt();
    }

    public MultiverseIteratorSuitStandConfirmMessage(int pageId) {
        this.pageId = pageId;
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.MULTIVERSAL_ITERATOR_SUIT_STAND_CONFIRM;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.pageId);
    }

    @Override
    public void handle(MessageContext context) {
        if (context.getPlayer().containerMenu instanceof MultiversalIteratorSuitStandMenu menu) {
            menu.convert(this.pageId);
        }
    }
}
