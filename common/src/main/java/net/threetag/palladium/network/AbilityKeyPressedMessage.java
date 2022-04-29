package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;

public class AbilityKeyPressedMessage extends BaseC2SMessage {

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
    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.power);
        buf.writeUtf(this.abilityKey);
        buf.writeBoolean(this.pressed);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            IPowerHandler handler = PowerManager.getPowerHandler(context.getPlayer()).orElse(null);

            if(handler == null) {
                return;
            }

            IPowerHolder holder = handler.getPowerHolder(PowerManager.getInstance(context.getPlayer().level).getPower(this.power));

            if (holder != null) {
                AbilityEntry entry = holder.getAbilities().get(this.abilityKey);

                if (entry != null) {
                    entry.keyPressed(context.getPlayer(), this.pressed);
                }
            }
        });
    }
}
