package net.threetag.palladium.power.energybar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.SetEnergyBarMessage;
import net.threetag.palladium.power.IPowerHolder;

public class EnergyBar {

    private final IPowerHolder powerHolder;
    private final EnergyBarConfiguration configuration;
    private int value, maxValue;
    private int overriddenMaxValue = -1;

    public EnergyBar(IPowerHolder powerHolder, EnergyBarConfiguration configuration) {
        this.powerHolder = powerHolder;
        this.configuration = configuration;
    }

    public void tick(LivingEntity entity) {
        if (this.configuration.syncedValue() != null) {
            var synced = this.configuration.syncedValue().get(entity);

            if (this.value != synced) {
                this.set(synced);
            }
        } else {
            int increase = this.configuration.autoIncrease();

            if (increase != 0 && entity.tickCount % this.configuration.autoIncreaseInterval() == 0) {
                this.add(increase);
            }
        }

        if (this.overriddenMaxValue > 0) {
            if (this.maxValue != this.overriddenMaxValue) {
                this.setMax(this.overriddenMaxValue);
            }
        } else {
            var syncedMax = this.configuration.maxValue().get(entity);
            if (this.maxValue != syncedMax) {
                this.setMax(syncedMax);
            }
        }
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

    public void setOverriddenMaxValue(int max) {
        int prev = this.overriddenMaxValue;
        this.overriddenMaxValue = Math.max(max, -1);

        if (prev != this.overriddenMaxValue) {
            if (this.value > this.getMax()) {
                this.value = this.getMax();
            }

            this.sync();
        }
    }

    public void setMax(int max) {
        int prevValue = this.maxValue;
        this.maxValue = Math.max(1, max);

        if (prevValue != this.value) {
            this.sync();
        }
    }

    public int getMax() {
        return this.maxValue;
    }

    public EnergyBarConfiguration getConfiguration() {
        return this.configuration;
    }

    private void sync() {
        if (!this.powerHolder.getEntity().level().isClientSide) {
            var msg = new SetEnergyBarMessage(this.powerHolder.getEntity().getId(), new EnergyBarReference(this.powerHolder.getPower().getId(), this.configuration.name()), this.value, this.maxValue);

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
        nbt.putInt("MaxValue", this.maxValue);
        nbt.putInt("OverriddenMaxValue", this.overriddenMaxValue);
        return nbt;
    }

    public void fromNBT(CompoundTag nbt) {
        this.set(nbt.getInt("Value"));
        this.setMax(nbt.getInt("MaxValue"));
        this.overriddenMaxValue = nbt.getInt("OverriddenMaxValue");
    }

}
