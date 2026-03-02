package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Pose;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.SettingType;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;
import net.threetag.palladium.util.PalladiumCodecs;

import java.util.Collections;
import java.util.List;

public record PoseCondition(List<Pose> poses) implements Condition {

    public static final MapCodec<PoseCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(PalladiumCodecs.listOrPrimitive(Pose.CODEC).fieldOf("pose").forGetter(PoseCondition::poses)
            ).apply(instance, PoseCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, PoseCondition> STREAM_CODEC = StreamCodec.composite(
            Pose.STREAM_CODEC.apply(ByteBufCodecs.list()), PoseCondition::poses, PoseCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return this.poses.contains(entity.getPose());
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
        public void addDocumentation(CodecDocumentationBuilder<Condition, PoseCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Pose")
                    .setDescription("Checks if the entity is in a specific pose.")
                    .add("pose",SettingType.enumList(Pose.values()), "The required pose(s).")
                    .addExampleObject(new PoseCondition(Collections.singletonList(Pose.CROUCHING)))
                    .addExampleObject(new PoseCondition(List.of(Pose.SLEEPING, Pose.SITTING)));
        }
    }
}
