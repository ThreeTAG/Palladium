package net.threetag.palladium.power.energybar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.util.EntityDependentInteger;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class EnergyBarConfiguration {

    private final String name;
    private final Color color;
    @Nullable
    private final EntityDependentInteger syncedValue;
    private final EntityDependentInteger maxValue;
    private final int autoIncrease;
    private final int autoIncreaseInterval;

    public EnergyBarConfiguration(String name, Color color, @Nullable EntityDependentInteger syncedValue, EntityDependentInteger maxValue, int autoIncrease, int autoIncreaseInterval) {
        this.name = name;
        this.color = color;
        this.syncedValue = syncedValue;
        this.maxValue = maxValue;
        this.autoIncrease = autoIncrease;
        this.autoIncreaseInterval = autoIncreaseInterval;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    @Nullable
    public EntityDependentInteger getSyncedValue() {
        return this.syncedValue;
    }

    public EntityDependentInteger getMaxValue() {
        return this.maxValue;
    }

    public int getAutoIncrease() {
        return this.autoIncrease;
    }

    public int getAutoIncreaseInterval() {
        return this.autoIncreaseInterval;
    }

    public static EnergyBarConfiguration fromJson(String name, JsonObject json) {
        return new EnergyBarConfiguration(
                name, GsonUtil.getAsColor(json, "color", Color.WHITE),
                json.has("synced_value") ? EntityDependentInteger.fromJson(json.get("synced_value"), "synced_value") : null,
                EntityDependentInteger.fromJson(json.get("max"), "max"),
                GsonHelper.getAsInt(json, "auto_increase_per_tick", 0),
                GsonUtil.getAsIntMin(json, "auto_increase_interval", 1, 1)
        );
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        if (this.syncedValue != null) {
            json.add("synced_value", this.syncedValue.toJson());
        }
        json.add("max", this.maxValue.toJson());
        json.addProperty("auto_increase_per_tick", this.autoIncrease);
        json.addProperty("auto_increase_interval", this.autoIncreaseInterval);

        var color = new JsonArray();
        color.add(this.color.getRed());
        color.add(this.color.getGreen());
        color.add(this.color.getBlue());
        json.add("color", color);
        return json;
    }

    public static EnergyBarConfiguration fromBuffer(FriendlyByteBuf buf) {
        var name = buf.readUtf();
        var color = new Color(buf.readInt());
        EntityDependentInteger syncedValue = null;
        if (buf.readBoolean()) {
            syncedValue = EntityDependentInteger.fromBuffer(buf);
        }
        var max = EntityDependentInteger.fromBuffer(buf);
        var autoIncrease = buf.readInt();
        var autoIncreaseInterval = buf.readInt();
        return new EnergyBarConfiguration(name, color, syncedValue, max, autoIncrease, autoIncreaseInterval);
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
        buf.writeInt(this.color.getRGB());
        buf.writeBoolean(this.syncedValue != null);
        if (this.syncedValue != null) {
            this.syncedValue.toBuffer(buf);
        }
        this.maxValue.toBuffer(buf);
        buf.writeInt(this.autoIncrease);
        buf.writeInt(this.autoIncreaseInterval);
    }
}
