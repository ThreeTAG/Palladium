package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.accessory.Accessory;
import net.threetag.palladium.accessory.AccessorySlot;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;

import java.util.Collection;
import java.util.Objects;

public class ToggleAccessoryMessage extends MessageC2S {

    public AccessorySlot slot;
    public Accessory accessory;

    public ToggleAccessoryMessage(AccessorySlot slot, Accessory accessory) {
        this.slot = slot;
        this.accessory = accessory;
    }

    public ToggleAccessoryMessage(FriendlyByteBuf buf) {
        this.slot = AccessorySlot.getSlotByName(buf.readUtf());
        this.accessory = Accessory.REGISTRY.get(buf.readResourceLocation());
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.TOGGLE_ACCESSORY;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.slot.getName());
        buf.writeResourceLocation(Objects.requireNonNull(Accessory.REGISTRY.getKey(this.accessory)));
    }

    @Override
    public void handle(MessageContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            Accessory.getPlayerData(player).ifPresent(data -> {
                if (this.slot != null && this.accessory != null) {
                    Collection<Accessory> accessories = data.getSlots().get(this.slot);
                    if (accessories == null || !accessories.contains(this.accessory)) {
                        data.enable(this.slot, this.accessory, player);
                    } else {
                        data.disable(this.slot, this.accessory, player);
                    }
                }
            });
        }
    }
}
