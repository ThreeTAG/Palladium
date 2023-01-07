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
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OpenAbilityBuyScreenMessage extends MessageS2C {

    private final ResourceLocation powerId;
    private final String abilityId;
    private final AbilityConfiguration.UnlockData unlockData;
    private final boolean available;

    public OpenAbilityBuyScreenMessage(ResourceLocation powerId, String abilityId, AbilityConfiguration.UnlockData unlockData, boolean available) {
        this.powerId = powerId;
        this.abilityId = abilityId;
        this.unlockData = unlockData;
        this.available = available;
    }

    public OpenAbilityBuyScreenMessage(FriendlyByteBuf buf) {
        this.powerId = buf.readResourceLocation();
        this.abilityId = buf.readUtf();
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
        buf.writeResourceLocation(this.powerId);
        buf.writeUtf(this.abilityId);
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
            var level = Objects.requireNonNull(Minecraft.getInstance().level);
            IPowerHandler handler = PowerManager.getPowerHandler(Minecraft.getInstance().player).orElse(null);
            Power power = PowerManager.getInstance(level).getPower(this.powerId);

            if (power != null && handler != null) {
                IPowerHolder holder = handler.getPowerHolder(power);

                if (holder != null) {
                    AbilityEntry entry = holder.getAbilities().get(this.abilityId);

                    if (entry != null) {
                        powersScreen.openOverlayScreen(new BuyAbilityScreen(this.powerId, entry, this.unlockData, this.available, powersScreen));
                    }
                }

            }
        }
    }
}