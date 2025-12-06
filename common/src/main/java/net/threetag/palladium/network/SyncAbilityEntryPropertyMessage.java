package net.threetag.palladium.network;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.client.screen.power.PowersScreen;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.NameChangeAbility;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import net.threetag.palladiumcore.util.PlayerUtil;

import java.util.Objects;

public class SyncAbilityEntryPropertyMessage extends MessageS2C {

    private final int entityId;
    private final AbilityReference reference;
    private final String propertyKey;
    private final CompoundTag tag;

    public SyncAbilityEntryPropertyMessage(int entityId, AbilityReference reference, String propertyKey, CompoundTag tag) {
        this.entityId = entityId;
        this.reference = reference;
        this.propertyKey = propertyKey;
        this.tag = tag;
    }

    public SyncAbilityEntryPropertyMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.reference = AbilityReference.fromBuffer(buf);
        this.propertyKey = buf.readUtf();
        this.tag = buf.readNbt();
    }

    @Override
    public MessageType getType() {
        return PalladiumNetwork.SYNC_ABILITY_ENTRY_PROPERTY;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        this.reference.toBuffer(buf);
        buf.writeUtf(this.propertyKey);
        buf.writeNbt(this.tag);
    }

    @Override
    public void handle(MessageContext context) {
        this.handleClient();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Environment(EnvType.CLIENT)
    public void handleClient() {
        var level = Objects.requireNonNull(Minecraft.getInstance().level);
        Entity entity = level.getEntity(this.entityId);
        if (entity instanceof LivingEntity livingEntity) {
            AbilityInstance entry = this.reference.getEntry(livingEntity);

            if (entry != null) {
                PalladiumProperty property = entry.getPropertyManager().getPropertyByName(this.propertyKey);

                if (property != null) {
                    entry.getPropertyManager().setRaw(property, property.fromNBT(this.tag.get(property.getKey()), entry.getPropertyManager().getDefault(property)));

                    if (Minecraft.getInstance().screen instanceof PowersScreen powers && powers.selectedTab != null) {
                        powers.selectedTab.populate();
                    }
                    if (property == NameChangeAbility.NAME_CACHED && entity instanceof Player player) {
                        PlayerUtil.refreshDisplayName(player);
                        var name = entry.getProperty(NameChangeAbility.NAME_CACHED);
                    }
                }
            }
        }
    }
}
