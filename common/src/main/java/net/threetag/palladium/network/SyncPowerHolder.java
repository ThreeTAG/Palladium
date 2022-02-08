package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.PowerManager;

public class SyncPowerHolder extends BaseS2CMessage {

    private final int entityId;
    private final CompoundTag data;

    public SyncPowerHolder(int entityId, CompoundTag data) {
        this.entityId = entityId;
        this.data = data;
    }

    public SyncPowerHolder(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.data = buf.readAnySizeNbt();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_POWER_HOLDER;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeNbt(this.data);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            if (context.getPlayer().level.getEntity(this.entityId) instanceof LivingEntity entity) {
                PowerManager.getPowerHolder(entity).fromNBT(this.data);
            }
        });
    }
}
