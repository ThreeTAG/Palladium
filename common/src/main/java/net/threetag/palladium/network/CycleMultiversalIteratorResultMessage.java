package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.menu.MultiversalIteratorMenu;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

public class CycleMultiversalIteratorResultMessage extends MessageC2S {

    public CycleMultiversalIteratorResultMessage(FriendlyByteBuf buf) {

    }

    public CycleMultiversalIteratorResultMessage() {

    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.CYCLE_MULTIVERSAL_ITERATOR_RESULT;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(MessageContext context) {
        if (context.getPlayer().containerMenu instanceof MultiversalIteratorMenu menu) {
            menu.cycleResult();
        }
    }
}
