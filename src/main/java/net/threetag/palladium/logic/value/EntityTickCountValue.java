package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class EntityTickCountValue extends IntegerValue {

    public static final MapCodec<EntityTickCountValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, EntityTickCountValue::new));

    public EntityTickCountValue(String molang) {
        super(molang);
    }

    @Override
    public int getInteger(DataContext context) {
        var entity = context.getEntity();
        return entity != null ? entity.tickCount : 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.ENTITY_TICK_COUNT.get();
    }

    public static class Serializer extends IntSerializer<EntityTickCountValue> {

        @Override
        public MapCodec<EntityTickCountValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, EntityTickCountValue> builder, HolderLookup.Provider provider) {
            builder.setName("Entity Tick Count").setDescription("Returns the tick count of the entity.")
                    .addExampleObject(new EntityTickCountValue("value * 2"));
        }
    }
}
