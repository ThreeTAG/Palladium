package net.threetag.palladium.power.energybar;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.network.PalladiumNetwork;
import net.threetag.palladium.network.SyncEnergyBarPacket;
import net.threetag.palladium.power.PowerHolder;

public class EnergyBarInstance {

    private final PowerHolder powerHolder;
    private final EnergyBarConfiguration configuration;
    private final EnergyBarReference reference;
    private int value, maxValue;
    private int overriddenMaxValue = -1;

    public EnergyBarInstance(EnergyBarConfiguration configuration, PowerHolder powerHolder, EnergyBarReference reference) {
        this.configuration = configuration;
        this.powerHolder = powerHolder;
        this.reference = reference;
    }

    public void tick(LivingEntity entity) {
        if (this.configuration.syncedValue() != null) {
            var synced = this.configuration.syncedValue().asInt(entity);

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
            var syncedMax = this.configuration.maxValue().asInt(entity);
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

    public EnergyBarReference getReference() {
        return this.reference;
    }

    private void sync() {
        if (!this.powerHolder.getEntity().level().isClientSide) {
            var msg = new SyncEnergyBarPacket(this.powerHolder.getEntity().getId(), this.reference, this.value, this.maxValue);
            PalladiumNetwork.sendToTrackingAndSelf(this.powerHolder.getEntity(), msg);
        }
    }

    public CompoundTag save() {
        var nbt = new CompoundTag();
        nbt.putInt("value", this.value);
        nbt.putInt("max_value", this.maxValue);
        nbt.putInt("overridden_max_value", this.overriddenMaxValue);
        return nbt;
    }

    public void load(CompoundTag nbt) {
        this.set(nbt.getIntOr("value", 0));
        this.setMax(nbt.getIntOr("max_value", 0));
        this.overriddenMaxValue = nbt.getIntOr("overridden_max_value", -1);
    }

}
