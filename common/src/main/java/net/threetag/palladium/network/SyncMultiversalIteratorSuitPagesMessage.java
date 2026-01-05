package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.menu.MultiversalIteratorSuitStandMenu;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SyncMultiversalIteratorSuitPagesMessage extends MessageS2C {

    private final List<MultiversalIteratorSuitStandMenu.IterationPage> pages;

    public SyncMultiversalIteratorSuitPagesMessage(List<MultiversalIteratorSuitStandMenu.IterationPage> pages) {
        this.pages = pages;
    }

    public SyncMultiversalIteratorSuitPagesMessage(FriendlyByteBuf buf) {
        this.pages = buf.readList(MultiversalIteratorSuitStandMenu.IterationPage::new);
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.SYNC_MULTIVERSAL_ITERATOR_SUIT_PAGES;
    }

    @Override
    public void toBytes(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeCollection(this.pages, (b, p) -> p.toNetwork(b));
    }

    @Override
    public void handle(MessageContext messageContext) {
        if (messageContext.getPlayer().containerMenu instanceof MultiversalIteratorSuitStandMenu menu) {
            menu.setPages(this.pages);
        }
    }
}
