package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import net.threetag.palladium.util.property.StringProperty;
import org.jetbrains.annotations.Nullable;

public class AbilityEnabledCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String abilityId;

    public AbilityEnabledCondition(@Nullable ResourceLocation power, String abilityId) {
        this.power = power;
        this.abilityId = abilityId;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityEntry dependency = null;
        if (this.power != null) {
            dependency = AbilityUtil.getEntry(entity, this.power, this.abilityId);
        } else if (holder != null) {
            dependency = holder.getAbilities().get(this.abilityId);
        }
        return dependency != null && dependency.isEnabled();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_ENABLED.get();
    }

    public static class Serializer extends ConditionSerializer {
        public static final PalladiumProperty<ResourceLocation> POWER = new ResourceLocationProperty("power").configurable("ID of the power where is the desired ability is located. Can be null IF used for abilities, then it will look into the current power");
        public static final PalladiumProperty<String> ABILITY = new StringProperty("ability").configurable("ID of the desired ability");

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability is enabled. If the power is not null, it will look for the ability in the specified power. If the power is null, it will look for the ability in the current power.";
        }

        public Serializer() {
            this.withProperty(POWER, null);
            this.withProperty(ABILITY, "ability_id");
        }

        @Override
        public Condition make(JsonObject json) {
            return new AbilityEnabledCondition(this.getProperty(json, POWER), this.getProperty(json, ABILITY));
        }

    }
}
