package net.threetag.palladium.client.variable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.util.molang.ModifyFloatFunction;
import team.unnamed.mocha.MochaEngine;

public abstract class FloatPathVariable extends PathVariable {

    public final String molang;
    public final ModifyFloatFunction function;

    public FloatPathVariable(String molang) {
        this.molang = molang;

        if (this.molang != null && !this.molang.isEmpty() && !this.molang.isBlank()) {
            MochaEngine<?> mocha = MochaEngine.createStandard();
            this.function = mocha.compile(this.molang, ModifyFloatFunction.class);
        } else {
            this.function = null;
        }
    }

    @Override
    public Object get(DataContext context) {
        float f = this.getFloat(context);

        if (this.function != null) {
            f = this.function.modify(f);
        }

        return f;
    }

    public abstract float getFloat(DataContext context);

    protected static <B extends FloatPathVariable> RecordCodecBuilder<B, String> modifyFunctionCodec() {
        return Codec.STRING.optionalFieldOf("modify", "").forGetter(v -> v.molang);
    }

    public static abstract class FloatSerializer<T extends FloatPathVariable> extends PathVariableSerializer<T> {

        @Override
        public CodecDocumentationBuilder<PathVariable, T> getDocumentation(HolderLookup.Provider provider) {
            return super.getDocumentation(provider).addOptional("modify", TYPE_MOLANG, "MoLang function to modify the value.");
        }
    }
}
