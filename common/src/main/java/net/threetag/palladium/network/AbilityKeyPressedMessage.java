package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;

public class AbilityKeyPressedMessage extends BaseC2SMessage {

    private final ResourceLocation provider;
    private final String abilityKey;
    private final boolean pressed;

    public AbilityKeyPressedMessage(ResourceLocation provider, String abilityKey, boolean pressed) {
        this.provider = provider;
        this.abilityKey = abilityKey;
        this.pressed = pressed;
    }

    public AbilityKeyPressedMessage(FriendlyByteBuf buf) {
        this.provider = buf.readResourceLocation();
        this.abilityKey = buf.readUtf();
        this.pressed = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.ABILITY_KEY_PRESSED;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.provider);
        buf.writeUtf(this.abilityKey);
        buf.writeBoolean(this.pressed);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        IPowerHolder holder = PowerManager.getPowerHandler(context.getPlayer()).getPowerHolder(this.provider);

        if (holder != null) {
            AbilityEntry entry = holder.getAbilities().get(this.abilityKey);

            if (entry != null) {
                entry.keyPressed(context.getPlayer(), this.pressed);
            }
        }
    }
}
