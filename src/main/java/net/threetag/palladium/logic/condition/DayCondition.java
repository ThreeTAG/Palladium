package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class DayCondition implements Condition {

    public static final DayCondition INSTANCE = new DayCondition();

    public static final MapCodec<DayCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, DayCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var level = context.getLevel();
        return level != null && !level.dimensionType().hasFixedTime() && level.getSkyDarken() < 4;
    }

    @Override
    public ConditionSerializer<DayCondition> getSerializer() {
        return ConditionSerializers.DAY.get();
    }

    public static class Serializer extends ConditionSerializer<DayCondition> {

        @Override
        public MapCodec<DayCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, DayCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, DayCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Day")
                    .setDescription("Check if it's currently daytime")
                    .addExampleObject(new DayCondition());
        }
    }
}
