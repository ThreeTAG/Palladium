package net.threetag.palladium.power.energybar;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class EnergyBarReference {

    @NotNull
    private final ResourceLocation powerId;
    @NotNull
    private final String energyBarName;

    public EnergyBarReference(@NotNull ResourceLocation powerId, @NotNull String energyBarName) {
        this.powerId = powerId;
        this.energyBarName = energyBarName;
    }

    @NotNull
    public ResourceLocation getPowerId() {
        return this.powerId;
    }

    @NotNull
    public String getEnergyBarName() {
        return this.energyBarName;
    }

    @Nullable
    public EnergyBar getEntry(LivingEntity entity) {
        IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);
        Power power = PowerManager.getInstance(entity.level()).getPower(this.powerId);

        if (power != null && handler != null) {
            IPowerHolder holder = handler.getPowerHolder(power);

            if (holder != null) {
                return holder.getEnergyBars().get(this.energyBarName);
            }
        }

        return null;
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.powerId);
        buf.writeUtf(this.energyBarName);
    }

    public static EnergyBarReference fromBuffer(FriendlyByteBuf buf) {
        return new EnergyBarReference(buf.readResourceLocation(), buf.readUtf());
    }

    @Override
    public String toString() {
        return this.powerId + "#" + this.energyBarName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(powerId, energyBarName);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof EnergyBarReference reference)) {
            return false;
        } else {
            return this.powerId.equals(reference.powerId) && this.energyBarName.equals(reference.energyBarName);
        }
    }
}
