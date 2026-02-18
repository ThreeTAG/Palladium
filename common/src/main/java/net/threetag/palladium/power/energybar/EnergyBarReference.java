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
import java.util.Optional;

public record EnergyBarReference(@Nullable ResourceLocation powerId, @NotNull String energyBarName) {

    public static EnergyBarReference fromString(String parse) {
        String[] s = parse.split("#", 2);

        if (s.length == 1) {
            return new EnergyBarReference(null, s[0]);
        } else {
            return new EnergyBarReference(new ResourceLocation(s[0]), s[1]);
        }
    }

    @Nullable
    public EnergyBar getEntry(LivingEntity entity) {
        return this.getEntry(entity, null);
    }

    @Nullable
    public EnergyBar getEntry(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
        if (this.powerId != null) {
            IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);
            Power power = PowerManager.getInstance(entity.level()).getPower(this.powerId);

            if (power != null && handler != null) {
                powerHolder = handler.getPowerHolder(power);
            } else {
                powerHolder = null;
            }
        }

        if (powerHolder != null) {
            return powerHolder.getEnergyBars().get(this.energyBarName);
        }

        return null;
    }

    public Optional<EnergyBar> optional(LivingEntity entity, @Nullable IPowerHolder powerHolder) {
        return Optional.ofNullable(this.getEntry(entity, powerHolder));
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeNullable(this.powerId, (buf1, resourceLocation) -> buf.writeResourceLocation(resourceLocation));
        buf.writeUtf(this.energyBarName);
    }

    public static EnergyBarReference fromBuffer(FriendlyByteBuf buf) {
        return new EnergyBarReference(buf.readNullable(FriendlyByteBuf::readResourceLocation), buf.readUtf());
    }

    @Override
    public String toString() {
        if (this.powerId == null) {
            return this.energyBarName;
        }
        return this.powerId + "#" + this.energyBarName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnergyBarReference that)) return false;
        return Objects.equals(this.powerId, that.powerId) && Objects.equals(this.energyBarName, that.energyBarName);
    }
}
