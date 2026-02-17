package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

public class MoLangStringValue extends StringValue {

    private static final String BLANK = "";

    public static final MapCodec<MoLangStringValue> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("molang").forGetter(v -> v.molang)
    ).apply(instance, MoLangStringValue::new));

    public MoLangStringValue(String molang) {
        super(molang);
    }

    @Override
    public String getString(DataContext context) {
        return BLANK;
    }

    @Override
    public ValueSerializer<?> getSerializer() {
        return ValueSerializers.MOLANG_STRING.get();
    }

    public static class Serializer extends StringSerializer<MoLangStringValue> {

        @Override
        public MapCodec<MoLangStringValue> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Value, MoLangStringValue> builder, HolderLookup.Provider provider) {
            builder.setName("MoLang String").setDescription("Allows MoLang usage to assemble a string")
                    .addExampleObject(new MoLangStringValue("query.day() ? 'day' : 'night'"));
        }
    }
}
