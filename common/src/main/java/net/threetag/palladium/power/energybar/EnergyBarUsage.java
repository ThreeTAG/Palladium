package net.threetag.palladium.power.energybar;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.threetag.palladium.power.IPowerHolder;

public class EnergyBarUsage {

    private final String energyBar;
    private final int amount;

    public EnergyBarUsage(String energyBar, int amount) {
        this.energyBar = energyBar;
        this.amount = amount;
    }

    public void consume(IPowerHolder holder) {
        var energyBar = holder.getEnergyBars().get(this.energyBar);

        if (energyBar != null) {
            energyBar.add(-this.amount);
        }
    }

    public static EnergyBarUsage fromJson(JsonObject json) {
        return new EnergyBarUsage(GsonHelper.getAsString(json, "energy_bar"), GsonHelper.getAsInt(json, "amount"));
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("energy_bar", this.energyBar);
        json.addProperty("amount", this.amount);
        return json;
    }
}
