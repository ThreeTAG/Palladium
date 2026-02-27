package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.attachment.PalladiumAttachments;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class IsWallClimbingCondition implements Condition {

    public static final IsWallClimbingCondition INSTANCE = new IsWallClimbingCondition();

    public static final MapCodec<IsWallClimbingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsWallClimbingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.getEntity();
        return entity instanceof Player && entity.getData(PalladiumAttachments.IS_CLIMBING.get());
    }

    @Override
    public ConditionSerializer<IsWallClimbingCondition> getSerializer() {
        return ConditionSerializers.IS_WALL_CLIMBING.get();
    }

    public static class Serializer extends ConditionSerializer<IsWallClimbingCondition> {

        @Override
        public MapCodec<IsWallClimbingCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsWallClimbingCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsWallClimbingCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is Wall Climbing")
                    .setDescription("Checks if the entity is currently wall climbing.")
                    .addExampleObject(new IsWallClimbingCondition());
        }
    }
}
