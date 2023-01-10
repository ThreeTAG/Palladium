package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;

import java.util.Objects;

public class SyncAbilityStateMessage extends MessageS2C {

    private final int entityId;
    private final AbilityReference reference;
    private final boolean unlocked, enabled;
    private final int maxCooldown, cooldown;
    private final int maxActivationTimer, activationTimer;

    public SyncAbilityStateMessage(int entityId, AbilityReference reference, boolean unlocked, boolean enabled, int maxCooldown, int cooldown, int maxActivationTimer, int activationTimer) {
        this.entityId = entityId;
        this.reference = reference;
        this.unlocked = unlocked;
        this.enabled = enabled;
        this.maxCooldown = maxCooldown;
        this.cooldown = cooldown;
        this.maxActivationTimer = maxActivationTimer;
        this.activationTimer = activationTimer;
    }

    public SyncAbilityStateMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.reference = AbilityReference.fromBuffer(buf);
        this.unlocked = buf.readBoolean();
        this.enabled = buf.readBoolean();
        this.maxCooldown = buf.readInt();
        this.cooldown = buf.readInt();
        this.maxActivationTimer = buf.readInt();
        this.activationTimer = buf.readInt();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_ABILITY_STATE;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        this.reference.toBuffer(buf);
        buf.writeBoolean(this.unlocked);
        buf.writeBoolean(this.enabled);
        buf.writeInt(this.maxCooldown);
        buf.writeInt(this.cooldown);
        buf.writeInt(this.maxActivationTimer);
        buf.writeInt(this.activationTimer);
    }

    @Override
    public void handle(MessageContext context) {
        this.handleClient();
    }

    @Environment(EnvType.CLIENT)
    public void handleClient() {
        Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(this.entityId);

        if (entity instanceof LivingEntity livingEntity) {
            AbilityEntry entry = this.reference.getEntry(livingEntity);

            if (entry != null) {
                entry.setClientState(livingEntity, entry.getHolder(), this.unlocked, this.enabled, this.maxCooldown, this.cooldown, this.maxActivationTimer, this.activationTimer);
            }
        }
    }
}
