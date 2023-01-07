package net.threetag.palladium.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.condition.BuyableCondition;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladiumcore.network.MessageC2S;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageType;

public class BuyAbilityUnlockMessage extends MessageC2S {

    private final ResourceLocation powerId;
    private final String abilityId;

    public BuyAbilityUnlockMessage(ResourceLocation powerId, String abilityId) {
        this.powerId = powerId;
        this.abilityId = abilityId;
    }

    public BuyAbilityUnlockMessage(FriendlyByteBuf buf) {
        this.powerId = buf.readResourceLocation();
        this.abilityId = buf.readUtf();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.BUY_ABILITY_UNLOCK;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.powerId);
        buf.writeUtf(this.abilityId);
    }

    @Override
    public void handle(MessageContext context) {
        IPowerHandler handler = PowerManager.getPowerHandler(context.getPlayer()).orElse(null);
        Power power = PowerManager.getInstance(context.getPlayer().level).getPower(this.powerId);

        if (power != null && handler != null) {
            IPowerHolder holder = handler.getPowerHolder(power);

            if (holder != null) {
                AbilityEntry entry = holder.getAbilities().get(this.abilityId);

                if (entry != null) {
                    var buyableCondition = entry.getConfiguration().findBuyCondition();

                    if (buyableCondition != null && !entry.getProperty(BuyableCondition.BOUGHT) && buyableCondition.isAvailable(context.getPlayer())) {
                        buyableCondition.buy(context.getPlayer(), entry);
                    }
                }
            }
        }
    }
}
