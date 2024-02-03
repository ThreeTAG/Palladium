package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.energybar.EnergyBar;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class EnergyBarCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String energyBarId;
    private final int min, max;

    public EnergyBarCondition(@Nullable ResourceLocation power, String energyBarId, int min, int max) {
        this.power = power;
        this.energyBarId = energyBarId;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        AtomicReference<IPowerHolder> holder = new AtomicReference<>(context.getPowerHolder());

        if (entity == null) {
            return false;
        }

        if (this.power != null) {
            PowerManager.getPowerHandler(entity).ifPresent(powerHandler -> {
                holder.set(powerHandler.getPowerHolders().get(this.power));
            });
        }

        if (holder.get() == null) {
            return false;
        }

        EnergyBar energyBar = holder.get().getEnergyBars().get(this.energyBarId);

        if (energyBar == null) {
            return false;
        } else {
            return this.min <= energyBar.get() && energyBar.get() <= this.max;
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ENERGY_BAR.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<ResourceLocation> POWER = new ResourceLocationProperty("power").configurable("ID of the power where is the desired energy bar is located. Can be null IF used for abilities, then it will look into the current power");
        public static final PalladiumProperty<String> ENERGY_BAR = new StringProperty("energy_bar").configurable("ID of the desired energy bar");
        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the energy in the energy bar");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the energy in the energy bar");

        public Serializer() {
            this.withProperty(POWER, null);
            this.withProperty(ENERGY_BAR, "energy_bar_name");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, Integer.MAX_VALUE);
        }

        @Override
        public Condition make(JsonObject json) {
            return new EnergyBarCondition(this.getProperty(json, POWER),
                    this.getProperty(json, ENERGY_BAR),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if energy bar has enough energy in it.";
        }
    }
}
