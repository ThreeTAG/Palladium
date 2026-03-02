package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public class IsInRainCondition implements Condition {

    public static final IsInRainCondition INSTANCE = new IsInRainCondition();

    public static final MapCodec<IsInRainCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsInRainCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInRain();
    }

    @Override
    public ConditionSerializer<IsInRainCondition> getSerializer() {
        return ConditionSerializers.IS_IN_RAIN.get();
    }

    public static class Serializer extends ConditionSerializer<IsInRainCondition> {

        @Override
        public MapCodec<IsInRainCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsInRainCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is in Rain")
                    .setDescription("Checks if the entity is currently in rain.")
                    .addExampleObject(new IsInRainCondition());
        }
    }
}
