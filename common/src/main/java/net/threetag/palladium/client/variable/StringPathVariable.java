package net.threetag.palladium.client.variable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.molang.ModifyStringFunction;
import team.unnamed.mocha.MochaEngine;

public abstract class StringPathVariable extends PathVariable {

    public final String molang;
    public final ModifyStringFunction function;

    public StringPathVariable(String molang) {
        this.molang = molang;

        if (this.molang != null && !this.molang.isEmpty() && !this.molang.isBlank()) {
            MochaEngine<?> mocha = MochaEngine.createStandard();
            this.function = mocha.compile(this.molang, ModifyStringFunction.class);
        } else {
            this.function = null;
        }
    }

    @Override
    public Object get(DataContext context) {
        String s = this.getString(context);

        if (this.function != null) {
            s = this.function.modify(s);
        }

        return s;
    }

    public abstract String getString(DataContext context);

    protected static <B extends StringPathVariable> RecordCodecBuilder<B, String> modifyFunctionCodec() {
        return Codec.STRING.optionalFieldOf("modify", "").forGetter(v -> v.molang);
    }

    public static abstract class StringSerializer<T extends StringPathVariable> extends PathVariableSerializer<T> {

        @Override
        public CodecDocumentationBuilder<PathVariable, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).addOptional("modify", TYPE_MOLANG, "MoLang function to modify the value.");
        }
    }
}
