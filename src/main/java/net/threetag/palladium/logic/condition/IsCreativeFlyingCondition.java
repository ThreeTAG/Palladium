package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class IsCreativeFlyingCondition implements Condition {

    public static final IsCreativeFlyingCondition INSTANCE = new IsCreativeFlyingCondition();

    public static final MapCodec<IsCreativeFlyingCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsCreativeFlyingCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.getEntity();
        return entity instanceof Player player && player.getAbilities().flying;
    }

    @Override
    public ConditionSerializer<IsCreativeFlyingCondition> getSerializer() {
        return ConditionSerializers.IS_CREATIVE_FLYING.get();
    }

    public static class Serializer extends ConditionSerializer<IsCreativeFlyingCondition> {

        @Override
        public MapCodec<IsCreativeFlyingCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, IsCreativeFlyingCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsCreativeFlyingCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is Creative Flying")
                    .setDescription("Checks if the entity is currently flying in creative mode.")
                    .addExampleObject(new IsCreativeFlyingCondition());
        }
    }
}
