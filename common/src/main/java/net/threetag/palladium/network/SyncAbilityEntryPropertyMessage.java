package net.threetag.palladium.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;

public class SyncAbilityEntryPropertyMessage extends BaseS2CMessage {

    private final int entityId;
    private final ResourceLocation powerId;
    private final String abilityId;
    private final String propertyKey;
    private final CompoundTag tag;

    public SyncAbilityEntryPropertyMessage(int entityId, ResourceLocation powerId, String abilityId, String propertyKey, CompoundTag tag) {
        this.entityId = entityId;
        this.powerId = powerId;
        this.abilityId = abilityId;
        this.propertyKey = propertyKey;
        this.tag = tag;
    }

    public SyncAbilityEntryPropertyMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.powerId = buf.readResourceLocation();
        this.abilityId = buf.readUtf();
        this.propertyKey = buf.readUtf();
        this.tag = buf.readNbt();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_ABILITY_ENTRY_PROPERTY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeResourceLocation(this.powerId);
        buf.writeUtf(this.abilityId);
        buf.writeUtf(this.propertyKey);
        buf.writeNbt(this.tag);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(this.entityId);
            if (entity instanceof LivingEntity livingEntity) {
                IPowerHandler handler = PowerManager.getPowerHandler(livingEntity).orElse(null);
                Power power = PowerManager.getInstance(Minecraft.getInstance().level).getPower(this.powerId);

                if (power != null && handler != null) {
                    IPowerHolder holder = handler.getPowerHolder(power);

                    if (holder != null) {
                        AbilityEntry entry = holder.getAbilities().get(this.abilityId);

                        if (entry != null) {
                            PalladiumProperty property = entry.getPropertyManager().getPropertyByName(this.propertyKey);

                            if (property != null) {
                                entry.getPropertyManager().setRaw(property, property.fromNBT(this.tag.get(property.getKey()), entry.getPropertyManager().getDefault(property)));
                            }
                        }
                    }
                }
            }
        });
    }
}
