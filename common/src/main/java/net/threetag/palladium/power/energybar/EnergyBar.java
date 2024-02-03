package net.threetag.palladium.power.energybar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.threetag.palladium.network.SetEnergyBarMessage;
import net.threetag.palladium.power.IPowerHolder;

public class EnergyBar {

    private final IPowerHolder powerHolder;
    private final EnergyBarConfiguration configuration;
    private int value;
    private int maxValue = -1;

    public EnergyBar(IPowerHolder powerHolder, EnergyBarConfiguration configuration) {
        this.powerHolder = powerHolder;
        this.configuration = configuration;
    }

    public void set(int value) {
        int prevValue = this.value;
        this.value = Mth.clamp(value, 0, this.getMax());

        if (prevValue != this.value) {
            this.sync();
        }
    }

    public int get() {
        return this.value;
    }

    public int add(int value) {
        int prevValue = this.value;
        this.value = Mth.clamp(this.value + value, 0, this.getMax());

        if (prevValue != this.value) {
            this.sync();
        }

        return this.value;
    }

    public void setMax(int max) {
        int prev = this.maxValue;
        this.maxValue = Math.max(max, -1);

        if (prev != this.maxValue) {
            if (this.value > this.getMax()) {
                this.value = this.getMax();
            }

            this.sync();
        }
    }

    public int getMax() {
        return this.maxValue > 0 ? this.maxValue : this.getConfiguration().getMaxValue();
    }

    public EnergyBarConfiguration getConfiguration() {
        return this.configuration;
    }

    private void sync() {
        if (!this.powerHolder.getEntity().level().isClientSide) {
            var msg = new SetEnergyBarMessage(this.powerHolder.getEntity().getId(), new EnergyBarReference(this.powerHolder.getPower().getId(), this.configuration.getName()), this.value, this.maxValue);

            if (this.powerHolder.getEntity() instanceof ServerPlayer player) {
                msg.sendToTrackingAndSelf(player);
            } else {
                msg.sendToTracking(this.powerHolder.getEntity());
            }
        }
    }

    public CompoundTag toNBT() {
        var nbt = new CompoundTag();
        nbt.putInt("Value", this.value);

        if (this.maxValue > 0) {
            nbt.putInt("MaxValue", this.maxValue);
        }

        return nbt;
    }

    public void fromNBT(CompoundTag nbt) {
        this.set(nbt.getInt("Value"));

        if (nbt.contains("MaxValue")) {
            this.setMax(nbt.getInt("MaxValue"));
        }
    }

}
