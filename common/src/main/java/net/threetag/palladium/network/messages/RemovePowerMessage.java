package net.threetag.palladium.network.messages;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.MessageType;
import net.threetag.palladium.network.NetworkManager;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.PowerManager;

public class RemovePowerMessage extends BaseS2CMessage {

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
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeResourceLocation(this.powerId);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (Minecraft.getInstance().level.getEntity(this.entityId) instanceof LivingEntity livingEntity) {
                PowerManager.getPowerHandler(livingEntity).ifPresent(handler -> handler.removePowerHolder(this.powerId));
            }
        });
    }
}
