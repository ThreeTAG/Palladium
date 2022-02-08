package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;

public class SyncAbilityStateMessage extends BaseS2CMessage {

    private final int entityId;
    private final String abilityKey;
    private final boolean unlocked, enabled;

    public SyncAbilityStateMessage(int entityId, String abilityKey, boolean unlocked, boolean enabled) {
        this.entityId = entityId;
        this.abilityKey = abilityKey;
        this.unlocked = unlocked;
        this.enabled = enabled;
    }

    public SyncAbilityStateMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.abilityKey = buf.readUtf();
        this.unlocked = buf.readBoolean();
        this.enabled = buf.readBoolean();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_ABILITY_STATE;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeUtf(this.abilityKey);
        buf.writeBoolean(this.unlocked);
        buf.writeBoolean(this.enabled);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Entity entity = context.getPlayer().level.getEntity(this.entityId);

            if (entity instanceof LivingEntity livingEntity) {
                // TODO different power contexts
                IPowerHolder powerHolder = PowerManager.getPowerHolder(livingEntity);
                AbilityEntry entry = powerHolder.getAbilities().get(this.abilityKey);

                if (entry != null) {
                    entry.setClientState(livingEntity, powerHolder, this.unlocked, this.enabled);
                }
            }
        });
    }
}
