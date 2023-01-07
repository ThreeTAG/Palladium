package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;

public class AbilityKeyPressedMessage extends MessageC2S {

    private final AbilityReference reference;
    private final boolean pressed;

    public AbilityKeyPressedMessage(AbilityReference reference, boolean pressed) {
        this.reference = reference;
        this.pressed = pressed;
    }

    public AbilityKeyPressedMessage(FriendlyByteBuf buf) {
        this.reference = AbilityReference.fromBuffer(buf);
        this.pressed = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.ABILITY_KEY_PRESSED;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        this.reference.toBuffer(buf);
        buf.writeBoolean(this.pressed);
    }

    @Override
    public void handle(MessageContext context) {
        AbilityEntry entry = this.reference.getEntry(context.getPlayer());

        if (entry != null && entry.isUnlocked()) {
            entry.keyPressed(context.getPlayer(), this.pressed);
        }
    }
}
