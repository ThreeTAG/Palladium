package net.threetag.palladium.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.energybar.EnergyBarReference;
import net.threetag.palladiumcore.network.MessageContext;
import net.threetag.palladiumcore.network.MessageS2C;
import net.threetag.palladiumcore.network.MessageType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SetEnergyBarMessage extends MessageS2C {

    private final int entityId;
    private final EnergyBarReference reference;
    private final int value, maxValue;

    public SetEnergyBarMessage(int entityId, EnergyBarReference reference, int value, int maxValue) {
        this.entityId = entityId;
        this.reference = reference;
        this.value = value;
        this.maxValue = maxValue;
    }

    public SetEnergyBarMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.reference = EnergyBarReference.fromBuffer(buf);
        this.value = buf.readInt();
        this.maxValue = buf.readInt();
    }

    @Override
    public @NotNull MessageType getType() {
        return PalladiumNetwork.SET_ENERGY_BAR;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        this.reference.toBuffer(buf);
        buf.writeInt(this.value);
        buf.writeInt(this.maxValue);
    }

    @Override
    public void handle(MessageContext context) {
        Entity entity = Objects.requireNonNull(Minecraft.getInstance().level).getEntity(this.entityId);

        if (entity instanceof LivingEntity living) {
            EnergyBar energyBar = this.reference.getEntry(living);

            if (energyBar != null) {
                energyBar.set(this.value);
                energyBar.setMax(this.maxValue);
            }
        }
    }
}