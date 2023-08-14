package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Pose;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PoseProperty;

public class PoseCondition extends Condition {

    public final Pose pose;

    public PoseCondition(Pose pose) {
        this.pose = pose;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

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
