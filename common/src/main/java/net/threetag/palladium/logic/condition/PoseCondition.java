package net.threetag.palladium.logic.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Pose;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextType;

import java.util.Locale;

public record PoseCondition(Pose pose) implements Condition {

    public static final MapCodec<PoseCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(Codec.STRING.xmap(PoseCondition::poseFromName, p -> p.toString().toLowerCase(Locale.ROOT)).fieldOf("pose").forGetter(PoseCondition::pose)
            ).apply(instance, PoseCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PoseCondition> STREAM_CODEC = StreamCodec.composite(
            Pose.STREAM_CODEC, PoseCondition::pose, PoseCondition::new
    );

    private static Pose poseFromName(String name) {
        for (Pose pose : Pose.values()) {
            if (pose.toString().toLowerCase().equalsIgnoreCase(name)) {
                return pose;
            }
        }
        return null;
    }

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.getPose() == this.pose;
    }

    @Override
    public ConditionSerializer<PoseCondition> getSerializer() {
        return ConditionSerializers.POSE.get();
    }

    public static class Serializer extends ConditionSerializer<PoseCondition> {

        @Override
        public MapCodec<PoseCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PoseCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in a specific pose.";
        }
    }
}
