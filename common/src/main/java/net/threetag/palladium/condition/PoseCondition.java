package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PoseProperty;
import org.jetbrains.annotations.Nullable;

public class PoseCondition extends Condition {

    public final Pose pose;

    public PoseCondition(Pose pose) {
        this.pose = pose;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return entity.getPose() == this.pose;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.POSE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Pose> POSE = new PoseProperty("pose").configurable("Determines the pose the entity must be in");

        public Serializer() {
            this.withProperty(POSE, Pose.CROUCHING);
        }

        @Override
        public Condition make(JsonObject json) {
            return new PoseCondition(this.getProperty(json, POSE));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in a specific pose.";
        }
    }
}
