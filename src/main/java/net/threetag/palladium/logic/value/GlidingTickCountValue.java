package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class GlidingTickCountValue extends IntegerValue {

    public static final MapCodec<GlidingTickCountValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, GlidingTickCountValue::new));

    public GlidingTickCountValue(String molang) {
        super(molang);
    }

    @Override
    public int getInteger(DataContext context) {
        var entity = context.getLivingEntity();
        return entity != null ? entity.getFallFlyingTicks() : 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.GLIDING_TICK_COUNT.get();
    }

    public static class Serializer extends IntSerializer<GlidingTickCountValue> {

        @Override
        public MapCodec<GlidingTickCountValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, GlidingTickCountValue> builder, HolderLookup.Provider provider) {
            builder.setName("Gliding Tick Count").setDescription("Returns the amount of ticks since the entity is gliding (using elytra or ability).")
                    .addExampleObject(new GlidingTickCountValue(""))
                    .addExampleObject(new GlidingTickCountValue("this * 2"));
        }
    }
}
