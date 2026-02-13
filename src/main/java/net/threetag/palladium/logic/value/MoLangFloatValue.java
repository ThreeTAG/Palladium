package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class MoLangFloatValue extends FloatValue {

    public static final MapCodec<MoLangFloatValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("molang").forGetter(v -> v.molang)
    ).apply(instance, MoLangFloatValue::new));

    public MoLangFloatValue(String molang) {
        super(molang);
    }

    @Override
    public float getFloat(DataContext context) {
        return 0F;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.MOLANG_FLOAT.get();
    }

    public static class Serializer extends FloatSerializer<MoLangFloatValue> {

        @Override
        public MapCodec<MoLangFloatValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, MoLangFloatValue> builder, HolderLookup.Provider provider) {
            builder.setName("MoLang Float").setDescription("Allows MoLang usage to calculate an float")
                    .addExampleObject(new MoLangFloatValue("query.day ? 1.0 : 0.0"));
        }
    }
}
