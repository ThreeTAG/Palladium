package net.threetag.palladium.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.network.MessageC2S;
import net.threetag.palladium.network.MessageContext;
import net.threetag.palladium.network.MessageType;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;

public class AbilityKeyPressedMessage extends MessageC2S {

    private final ResourceLocation power;
    private final String abilityKey;
    private final boolean pressed;

    public AbilityKeyPressedMessage(ResourceLocation power, String abilityKey, boolean pressed) {
        this.power = power;
        this.abilityKey = abilityKey;
        this.pressed = pressed;
    }

    public AbilityKeyPressedMessage(FriendlyByteBuf buf) {
        this.power = buf.readResourceLocation();
        this.abilityKey = buf.readUtf();
        this.pressed = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.ABILITY_KEY_PRESSED;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.power);
        buf.writeUtf(this.abilityKey);
        buf.writeBoolean(this.pressed);
    }

    @Override
    public void handle(MessageContext context) {
        IPowerHandler handler = PowerManager.getPowerHandler(context.getPlayer()).orElse(null);

        if (handler == null) {
            return;
        }

        IPowerHolder holder = handler.getPowerHolder(PowerManager.getInstance(context.getPlayer().level).getPower(this.power));

        if (holder != null) {
            AbilityEntry entry = holder.getAbilities().get(this.abilityKey);

            if (entry != null) {
                entry.keyPressed(context.getPlayer(), this.pressed);
            }
        }
    }
}
