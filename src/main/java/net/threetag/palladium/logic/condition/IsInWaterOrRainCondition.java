package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.logic.context.DataContextKeys;

public class IsInWaterOrRainCondition implements Condition {

    public static final IsInWaterOrRainCondition INSTANCE = new IsInWaterOrRainCondition();

    public static final MapCodec<IsInWaterOrRainCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, IsInWaterOrRainCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var entity = context.get(DataContextKeys.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWaterOrRain();
    }

    @Override
    public ConditionSerializer<IsInWaterOrRainCondition> getSerializer() {
        return ConditionSerializers.IS_IN_WATER_OR_RAIN.get();
    }

    public static class Serializer extends ConditionSerializer<IsInWaterOrRainCondition> {

        @Override
        public MapCodec<IsInWaterOrRainCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, IsInWaterOrRainCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Is in Water or Rain")
                    .setDescription("Checks if the entity is currently in water or rain.")
                    .addExampleObject(new IsInWaterOrRainCondition());
        }
    }
}
