package net.threetag.palladium.power.energybar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.threetag.palladium.util.json.GsonUtil;

import java.awt.*;

public class EnergyBarConfiguration {

    private final String name;
    private final Color color;
    private final int maxValue;
    private final int autoIncrease;

    public EnergyBarConfiguration(String name, Color color, int maxValue, int autoIncrease) {
        this.name = name;
        this.color = color;
        this.maxValue = maxValue;
        this.autoIncrease = autoIncrease;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public int getAutoIncrease() {
        return this.autoIncrease;
    }

    public static EnergyBarConfiguration fromJson(String name, JsonObject json) {
        return new EnergyBarConfiguration(name, GsonUtil.getAsColor(json, "color", Color.WHITE), GsonUtil.getAsIntMin(json, "max", 1), GsonUtil.getAsIntMin(json, "auto_increase_per_tick", 0, 0));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("max", this.maxValue);
        json.addProperty("auto_increase_per_tick", this.autoIncrease);

        var color = new JsonArray();
        color.add(this.color.getRed());
        color.add(this.color.getGreen());
        color.add(this.color.getBlue());
        json.add("color", color);
        return json;
    }

    public static EnergyBarConfiguration fromBuffer(FriendlyByteBuf buf) {
        return new EnergyBarConfiguration(buf.readUtf(), new Color(buf.readInt()), buf.readInt(), buf.readInt());
    }

    public void toBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
        buf.writeInt(this.color.getRGB());
        buf.writeInt(this.maxValue);
        buf.writeInt(this.autoIncrease);
    }
}
