package net.threetag.palladium.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;

public class RemovePowerMessage extends MessageS2C {

    private final int entityId;
    private final ResourceLocation powerId;

    public RemovePowerMessage(int entityId, ResourceLocation powerId) {
        this.entityId = entityId;
        this.powerId = powerId;
    }

    public RemovePowerMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.powerId = buf.readResourceLocation();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.REMOVE_POWER;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeResourceLocation(this.powerId);
    }

    @Override
    public void handle(MessageContext context) {
        if (Minecraft.getInstance().level.getEntity(this.entityId) instanceof LivingEntity livingEntity) {
            PowerManager.getPowerHandler(livingEntity).ifPresent(handler -> handler.removePowerHolder(this.powerId));
        }
    }
}
