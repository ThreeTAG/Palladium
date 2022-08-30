package net.threetag.palladium.network.messages;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.MessageContext;
import net.threetag.palladium.network.MessageS2C;
import net.threetag.palladium.network.MessageType;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.Objects;

public class SyncAbilityStateMessage extends MessageS2C {

    private final int entityId;
    private final ResourceLocation power;
    private final String abilityKey;
    private final boolean unlocked, enabled;
    private final int maxCooldown, cooldown;
    private final int maxActivationTimer, activationTimer;

    public SyncAbilityStateMessage(int entityId, ResourceLocation power, String abilityKey, boolean unlocked, boolean enabled, int maxCooldown, int cooldown, int maxActivationTimer, int activationTimer) {
        this.entityId = entityId;
        this.power = power;
        this.abilityKey = abilityKey;
        this.unlocked = unlocked;
        this.enabled = enabled;
        this.maxCooldown = maxCooldown;
        this.cooldown = cooldown;
        this.maxActivationTimer = maxActivationTimer;
        this.activationTimer = activationTimer;
    }

    public SyncAbilityStateMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.power = buf.readResourceLocation();
        this.abilityKey = buf.readUtf();
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
        buf.writeResourceLocation(this.power);
        buf.writeUtf(this.abilityKey);
        buf.writeBoolean(this.unlocked);
        buf.writeBoolean(this.enabled);
        buf.writeInt(this.maxCooldown);
        buf.writeInt(this.cooldown);
        buf.writeInt(this.maxActivationTimer);
        buf.writeInt(this.activationTimer);
    }

    @Override
    public void handle(MessageContext context) {
        Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(this.entityId);

        if (entity instanceof LivingEntity livingEntity) {
            PowerManager.getPowerHandler(livingEntity).ifPresent(handler -> {
                IPowerHolder powerHolder = handler.getPowerHolder(PowerManager.getInstance(entity.level).getPower(this.power));

                if (powerHolder != null) {
                    AbilityEntry entry = powerHolder.getAbilities().get(this.abilityKey);

                    if (entry != null) {
                        entry.setClientState(livingEntity, powerHolder, this.unlocked, this.enabled, this.maxCooldown, this.cooldown, this.maxActivationTimer, this.activationTimer);
                    }
                }
            });
        }
    }
}
