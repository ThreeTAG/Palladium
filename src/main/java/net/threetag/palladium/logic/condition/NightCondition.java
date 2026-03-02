package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class NightCondition implements Condition {

    public static final NightCondition INSTANCE = new NightCondition();

    public static final MapCodec<NightCondition> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, NightCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public boolean test(DataContext context) {
        var level = context.getLevel();
        return level != null && (!level.dimensionType().hasFixedTime() && level.getSkyDarken() >= 4);
    }

    @Override
    public ConditionSerializer<NightCondition> getSerializer() {
        return ConditionSerializers.NIGHT.get();
    }

    public static class Serializer extends ConditionSerializer<NightCondition> {

        @Override
        public MapCodec<NightCondition> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Condition, NightCondition> builder, HolderLookup.Provider provider) {
            builder.setName("Night")
                    .setDescription("Check if it's currently nighttime")
                    .addExampleObject(new NightCondition());
        }
    }
}
