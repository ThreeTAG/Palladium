package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.screen.power.BuyAbilityScreen;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OpenAbilityBuyScreenMessage extends MessageS2C {

    private final AbilityReference reference;
    private final AbilityConfiguration.UnlockData unlockData;
    private final boolean available;

    public OpenAbilityBuyScreenMessage(AbilityReference reference, AbilityConfiguration.UnlockData unlockData, boolean available) {
        this.reference = reference;
        this.unlockData = unlockData;
        this.available = available;
    }

    public OpenAbilityBuyScreenMessage(FriendlyByteBuf buf) {
        this.reference = AbilityReference.fromBuffer(buf);
        this.unlockData = new AbilityConfiguration.UnlockData(buf);
        this.available = buf.readBoolean();
    }

    @NotNull
    @Override
    public MessageType getType() {
        return PalladiumNetwork.OPEN_ABILITY_BUY_SCREEN;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        this.reference.toBuffer(buf);
        this.unlockData.toBuffer(buf);
        buf.writeBoolean(this.available);
    }

    @Override
    public void handle(MessageContext context) {
        this.handleClient(context);
    }

    @Environment(EnvType.CLIENT)
    public void handleClient(MessageContext context) {
        if (Minecraft.getInstance().screen instanceof PowersScreen powersScreen) {
            AbilityEntry entry = this.reference.getEntry(Minecraft.getInstance().player);

            if (entry != null) {
                powersScreen.openOverlayScreen(new BuyAbilityScreen(this.reference, this.unlockData, this.available, powersScreen));
            }
        }
    }
}