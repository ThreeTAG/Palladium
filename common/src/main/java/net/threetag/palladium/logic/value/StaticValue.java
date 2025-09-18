package net.threetag.palladium.logic.value;

import com.google.gson.JsonPrimitive;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class StaticValue extends Value {

    public static final MapCodec<StaticValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.PASSTHROUGH.fieldOf("value").forGetter(v -> null)
    ).apply(instance, StaticValue::new));

    public final Dynamic<?> object;

    public StaticValue(Dynamic<?> object) {
        this.object = object;
    }

    @Override
    public Object get(DataContext context) {
        return this.object.getValue();
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return null;
    }

    public static class Serializer extends ValueSerializer<StaticValue> {

        @Override
        public MapCodec<StaticValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, StaticValue> builder, HolderLookup.Provider provider) {
            builder.setName("Static Value").setDescription("Utilises a simple, static value")
                    .add("value", TYPE_ANY, "A simple, static value.")
                    .setExampleObject(new StaticValue(new Dynamic<>(JsonOps.INSTANCE, new JsonPrimitive(42))));
        }
    }
}
