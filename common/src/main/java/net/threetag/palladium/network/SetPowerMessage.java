package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.provider.PowerProvider;

public class SetPowerMessage extends BaseS2CMessage {

    private final int entityId;
    private final PowerProvider provider;
    private final ResourceLocation powerId;

    public SetPowerMessage(int entityId, PowerProvider provider, ResourceLocation powerId) {
        this.entityId = entityId;
        this.provider = provider;
        this.powerId = powerId;
    }

    public SetPowerMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.provider = PowerManager.PROVIDER_REGISTRY.get(buf.readResourceLocation());
        this.powerId = buf.readBoolean() ? buf.readResourceLocation() : null;
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SET_POWER;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeResourceLocation(PowerManager.PROVIDER_REGISTRY.getId(this.provider));
        buf.writeBoolean(this.powerId != null);
        if (this.powerId != null) {
            buf.writeResourceLocation(this.powerId);
        }
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (Minecraft.getInstance().level.getEntity(this.entityId) instanceof LivingEntity livingEntity) {
                PowerManager.getPowerHandler(livingEntity).setPowerHolder(this.provider, this.provider.createHolder(livingEntity, PowerManager.getInstance(livingEntity.level).getPower(this.powerId)));
            }
        });
    }
}
