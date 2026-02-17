package net.threetag.palladium.logic.value;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class EntityHealthValue extends FloatValue {

    public static final MapCodec<EntityHealthValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            modifyFunctionCodec()
    ).apply(instance, EntityHealthValue::new));

    public EntityHealthValue(String molang) {
        super(molang);
    }

    @Override
    public float getFloat(DataContext context) {
        var entity = context.getLivingEntity();
        return entity != null ? entity.getHealth() : 0;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.ENTITY_HEALTH.get();
    }

    public static class Serializer extends FloatSerializer<EntityHealthValue> {

        @Override
        public MapCodec<EntityHealthValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, EntityHealthValue> builder, HolderLookup.Provider provider) {
            builder.setName("Entity Health").setDescription("Returns the health of the entity.")
                    .addExampleObject(new EntityHealthValue("this * 2"));
        }
    }
}
