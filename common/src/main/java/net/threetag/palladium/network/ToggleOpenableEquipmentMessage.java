package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.item.Openable;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

public class ToggleOpenableEquipmentMessage extends MessageC2S {

    public ToggleOpenableEquipmentMessage() {
    }

    public ToggleOpenableEquipmentMessage(FriendlyByteBuf buf) {
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.TOGGLE_OPENABLE_EQUIPMENT;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {

    }

    @Override
    public void handle(MessageContext context) {
        var opened = false;

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            var stack = context.getPlayer().getItemBySlot(slot);

            if (!stack.isEmpty() && stack.getItem() instanceof Openable openable) {
                opened = opened || openable.isOpen(stack);
            }
        }

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            var stack = context.getPlayer().getItemBySlot(slot);

            if (!stack.isEmpty() && stack.getItem() instanceof Openable openable) {
                openable.setOpen(context.getPlayer(), stack, slot, !opened);
            }
        }
    }
}
