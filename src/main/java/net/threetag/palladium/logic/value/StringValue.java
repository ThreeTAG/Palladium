package net.threetag.palladium.logic.value;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.client.renderer.entity.PalladiumMoLangQuery;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.util.molang.ModifyStringFunction;
import team.unnamed.mocha.MochaEngine;
import team.unnamed.mocha.runtime.binding.JavaObjectBinding;

public abstract class StringValue extends Value {

    public final String molang;
    public final ModifyStringFunction function;

    public StringValue(String molang) {
        this.molang = molang;

        if (this.molang != null && !this.molang.isEmpty() && !this.molang.isBlank()) {
            MochaEngine<?> mocha = MochaEngine.createStandard();
            mocha.scope().set("query", JavaObjectBinding.of(PalladiumMoLangQuery.class, PalladiumMoLangQuery.INSTANCE, null));
            this.function = mocha.compile(this.molang, ModifyStringFunction.class);
        } else {
            this.function = null;
        }
    }

    @Override
    public Object get(DataContext context) {
        String s = this.getString(context);

        if (this.function != null) {
            PalladiumMoLangQuery.setContext(context, 1F);
            s = this.function.modify(s);
        }

        return s;
    }

    public abstract String getString(DataContext context);

    protected static <B extends StringValue> RecordCodecBuilder<B, String> modifyFunctionCodec() {
        return Codec.STRING.optionalFieldOf("modify", "").forGetter(v -> v.molang);
    }

    public static abstract class StringSerializer<T extends StringValue> extends ValueSerializer<T> {

        @Override
        public CodecDocumentationBuilder<Value, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).addOptional("modify", TYPE_MOLANG, "MoLang function to modify the value.");
        }
    }
}
