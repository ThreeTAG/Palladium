package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

public class HasTagCondition extends Condition {

    private final String tag;

    public HasTagCondition(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        return entity.getTags().contains(this.tag);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HAS_TAG.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> TAG = new StringProperty("tag").configurable("The tag the entity must have.");

        public Serializer() {
            this.withProperty(TAG, "example_tag");
        }

        @Override
        public Condition make(JsonObject json) {
            return new HasTagCondition(this.getProperty(json, TAG));
        }

        @Override
        public String getDescription() {
            return "Checks if the entity has a specific tag. These tags are added to entities via /tag command";
        }
    }
}
