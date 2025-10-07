package net.threetag.palladium.power.energybar;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.threetag.palladium.logic.value.Value;
import net.threetag.palladium.util.PalladiumCodecs;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.Optional;

public final class EnergyBarConfiguration {

    public static final Codec<EnergyBarConfiguration> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    PalladiumCodecs.COLOR_CODEC.optionalFieldOf("color", Color.WHITE).forGetter(EnergyBarConfiguration::color),
                    Value.CODEC.optionalFieldOf("synced_value").forGetter(c -> Optional.ofNullable(c.syncedValue)),
                    Value.CODEC.fieldOf("max").forGetter(EnergyBarConfiguration::maxValue),
                    Codec.INT.optionalFieldOf("auto_increase_per_tick", 0).forGetter(EnergyBarConfiguration::autoIncrease),
                    Codec.INT.optionalFieldOf("auto_increase_interval", 1).forGetter(EnergyBarConfiguration::autoIncreaseInterval)
            ).apply(instance, (color, syncVal, max, incPerTick, incInterval) ->
                    new EnergyBarConfiguration(color, syncVal.orElse(null), max, incPerTick, incInterval))
    );

    private String key;
    private final Color color;
    private final @Nullable Value syncedValue;
    private final Value maxValue;
    private final int autoIncrease;
    private final int autoIncreaseInterval;

    public EnergyBarConfiguration(Color color, @Nullable Value syncedValue,
                                  Value maxValue, int autoIncrease, int autoIncreaseInterval) {
        this.color = color;
        this.syncedValue = syncedValue;
        this.maxValue = maxValue;
        this.autoIncrease = autoIncrease;
        this.autoIncreaseInterval = autoIncreaseInterval;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public Color color() {
        return this.color;
    }

    public @Nullable Value syncedValue() {
        return this.syncedValue;
    }

    public Value maxValue() {
        return this.maxValue;
    }

    public int autoIncrease() {
        return this.autoIncrease;
    }

    public int autoIncreaseInterval() {
        return this.autoIncreaseInterval;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (EnergyBarConfiguration) obj;
        return Objects.equals(this.color, that.color) &&
                Objects.equals(this.syncedValue, that.syncedValue) &&
                Objects.equals(this.maxValue, that.maxValue) &&
                this.autoIncrease == that.autoIncrease &&
                this.autoIncreaseInterval == that.autoIncreaseInterval;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, syncedValue, maxValue, autoIncrease, autoIncreaseInterval);
    }

    @Override
    public String toString() {
        return "EnergyBarConfiguration[" +
                "color=" + color + ", " +
                "syncedValue=" + syncedValue + ", " +
                "maxValue=" + maxValue + ", " +
                "autoIncrease=" + autoIncrease + ", " +
                "autoIncreaseInterval=" + autoIncreaseInterval + ']';
    }

}
